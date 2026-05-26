package com.erikarakelyan.travelbro;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DestinationsAdapter extends RecyclerView.Adapter<DestinationsAdapter.ViewHolder> {

    private final Context context;
    private final List<DestinationItem> items = new ArrayList<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public DestinationsAdapter(Context context) {
        this.context = context;
    }

    public void loadPopularDestinations() {
        items.clear();
        String[] popular = {"Париж", "Токио", "Рим", "Барселона", "Стамбул", "Дубай"};
        for (String name : popular) {
            items.add(new DestinationItem(name, "Популярное", "🌍", "Загрузка..."));
        }
        notifyDataSetChanged();

        for (int i = 0; i < items.size(); i++) {
            final int index = i;
            final String cityName = items.get(i).name;
            executor.execute(() -> {
                String desc = fetchWikiSummary(cityName);
                mainHandler.post(() -> {
                    if (index < items.size()) {
                        items.get(index).description = desc;
                        notifyItemChanged(index);
                    }
                });
            });
        }
    }

    public void searchDestinations(String query) {
        if (query.trim().isEmpty()) return;
        items.clear();
        notifyDataSetChanged();

        executor.execute(() -> {
            try {
                List<DestinationItem> finalResults = new ArrayList<>();

                // 1. ШАГ: Ищем ТОЧНОЕ совпадение (Direct Match)
                String directTitle = query.trim();
                String directDesc = fetchWikiSummary(directTitle);

                // Если Wikipedia нашла конкретную статью по этому слову
                if (!directDesc.contains("не найдено") && !directDesc.equals("Загрузка...")) {
                    finalResults.add(new DestinationItem(
                            directTitle.substring(0, 1).toUpperCase() + directTitle.substring(1),
                            "Точное совпадение",
                            "⭐",
                            directDesc));
                }

                // 2. ШАГ: Ищем похожие туристические места
                String travelQuery = query + " достопримечательности";
                String encoded = URLEncoder.encode(travelQuery, "UTF-8");
                String urlStr = "https://ru.wikipedia.org/w/api.php?action=query&format=json&prop=extracts"
                        + "&generator=search&gsrsearch=" + encoded + "&gsrlimit=10&exintro=1&explaintext=1&exsentences=3";

                JSONObject response = fetchJson(urlStr);
                if (response != null && response.has("query")) {
                    JSONObject pages = response.getJSONObject("query").getJSONObject("pages");
                    Iterator<String> keys = pages.keys();

                    while(keys.hasNext()) {
                        JSONObject page = pages.getJSONObject(keys.next());
                        String title = page.getString("title");
                        String extract = page.optString("extract", "");

                        // Добавляем только если это не дубликат точного совпадения
                        if (!title.equalsIgnoreCase(directTitle) && isTouristTarget(extract, title)) {
                            finalResults.add(new DestinationItem(title, "Направление", "📍", extract));
                        }
                    }
                }

                mainHandler.post(() -> {
                    items.addAll(finalResults);
                    notifyDataSetChanged();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private boolean isTouristTarget(String text, String title) {
        String content = (text + " " + title).toLowerCase();
        return content.contains("город") || content.contains("страна") ||
                content.contains("остров") || content.contains("турист") ||
                content.contains("достопримечательность") || content.contains("курорт") ||
                content.contains("музей") || content.contains("парк");
    }

    private String fetchWikiSummary(String title) {
        try {
            String encoded = URLEncoder.encode(title, "UTF-8");
            String urlStr = "https://ru.wikipedia.org/api/rest_v1/page/summary/" + encoded;
            JSONObject obj = fetchJson(urlStr);
            if (obj != null && obj.has("extract")) {
                return obj.getString("extract");
            }
        } catch (Exception e) {}
        return "Информация не найдена.";
    }

    private JSONObject fetchJson(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "TravelBro/1.0");
            if (conn.getResponseCode() == 200) {
                BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) sb.append(line);
                return new JSONObject(sb.toString());
            }
        } catch (Exception e) {}
        return null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_destination, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        DestinationItem item = items.get(position);
        h.tvEmoji.setText(item.emoji);
        h.tvName.setText(item.name);
        h.tvCountry.setText(item.country);
        h.tvDesc.setText(item.description);
        h.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DestinationDetailActivity.class);
            intent.putExtra("city", item.name);
            intent.putExtra("description", item.description);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmoji, tvName, tvCountry, tvDesc;
        ViewHolder(View v) {
            super(v);
            tvEmoji = v.findViewById(R.id.tv_destination_emoji);
            tvName = v.findViewById(R.id.tv_destination_name);
            tvCountry = v.findViewById(R.id.tv_destination_country);
            tvDesc = v.findViewById(R.id.tv_destination_desc);
        }
    }

    static class DestinationItem {
        String name, country, emoji, description;
        DestinationItem(String n, String c, String e, String d) {
            name = n; country = c; emoji = e; description = d;
        }
    }
}