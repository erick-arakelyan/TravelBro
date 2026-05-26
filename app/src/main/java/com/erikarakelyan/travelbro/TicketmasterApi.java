package com.erikarakelyan.travelbro;

import android.os.Handler;
import android.os.Looper;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TicketmasterApi {

    private static final String API_KEY = "MIfFMq4OGXQTMFEQa3zSRGi3kbvkHGt2"; // ticketmaster API key
    private static final String BASE = "https://app.ticketmaster.com/discovery/v2/events.json";
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface EventsCallback {
        void onSuccess(List<EventItem> events);
        void onError(String message);
    }

    /** Поиск по GPS-координатам, радиус 50 км */
    public void searchByLocation(double lat, double lon, EventsCallback callback) {
        String url = BASE + "?apikey=" + API_KEY
                + "&latlong=" + lat + "," + lon
                + "&radius=50&unit=km&size=20&sort=date,asc";
        fetchEvents(url, callback);
    }

    /** Поиск по коду страны (ISO 2) */
    public void searchByCountry(String countryCode, EventsCallback callback) {
        String url = BASE + "?apikey=" + API_KEY
                + "&countryCode=" + countryCode
                + "&size=20&sort=date,asc";
        fetchEvents(url, callback);
    }

    /** Поиск по городу + опциональный код страны */
    public void searchByCity(String city, String countryCode, EventsCallback callback) {
        String url = BASE + "?apikey=" + API_KEY
                + "&city=" + encode(city)
                + (countryCode != null ? "&countryCode=" + countryCode : "")
                + "&size=20&sort=date,asc";
        fetchEvents(url, callback);
    }

    private void fetchEvents(String urlStr, EventsCallback callback) {
        executor.execute(() -> {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                int code = conn.getResponseCode();
                if (code != 200) {
                    mainHandler.post(() -> callback.onError("HTTP " + code));
                    return;
                }

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
                br.close();

                List<EventItem> items = parse(sb.toString());
                mainHandler.post(() -> callback.onSuccess(items));

            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    private List<EventItem> parse(String json) {
        List<EventItem> result = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(json);
            if (!root.has("_embedded")) return result;

            JSONArray events = root.getJSONObject("_embedded").getJSONArray("events");

            for (int i = 0; i < events.length(); i++) {
                JSONObject ev = events.getJSONObject(i);

                String title = ev.optString("name", "Без названия");
                String date = "";
                String venue = "";
                String city = "";
                String country = "";

                // Дата
                if (ev.has("dates")) {
                    JSONObject dates = ev.getJSONObject("dates");
                    if (dates.has("start")) {
                        date = dates.getJSONObject("start").optString("localDate", "");
                    }
                }

                // Место проведения
                if (ev.has("_embedded")) {
                    JSONObject emb = ev.getJSONObject("_embedded");
                    if (emb.has("venues") && emb.getJSONArray("venues").length() > 0) {
                        JSONObject v = emb.getJSONArray("venues").getJSONObject(0);
                        venue = v.optString("name", "");
                        city = v.has("city")
                                ? v.getJSONObject("city").optString("name", "") : "";
                        country = v.has("country")
                                ? v.getJSONObject("country").optString("name", "") : "";
                    }
                }

                // Эмодзи по категории
                String emoji = "🎫";
                if (ev.has("classifications") && ev.getJSONArray("classifications").length() > 0) {
                    String segment = ev.getJSONArray("classifications")
                            .getJSONObject(0)
                            .optJSONObject("segment") != null
                            ? ev.getJSONArray("classifications")
                            .getJSONObject(0)
                            .getJSONObject("segment")
                            .optString("name", "") : "";
                    emoji = getEmoji(segment);
                }

                result.add(new EventItem(title, venue, city, country, date, emoji));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getEmoji(String segment) {
        if (segment == null) return "🎫";
        switch (segment.toLowerCase()) {
            case "music": return "🎵";
            case "sports": return "⚽";
            case "arts & theatre": return "🎭";
            case "film": return "🎬";
            case "family": return "👨‍👩‍👧";
            default: return "🎫";
        }
    }

    private String encode(String s) {
        try {
            return java.net.URLEncoder.encode(s, "UTF-8");
        } catch (Exception e) {
            return s;
        }
    }
}