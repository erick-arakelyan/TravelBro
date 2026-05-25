package com.example.travelbro;

import java.util.HashMap;
import java.util.Map;

public class CountryCodeHelper {

    private static final Map<String, String> MAP = new HashMap<>();

    static {
        MAP.put("армения", "AM");
        MAP.put("россия", "RU");
        MAP.put("сша", "US");
        MAP.put("великобритания", "GB");
        MAP.put("германия", "DE");
        MAP.put("франция", "FR");
        MAP.put("италия", "IT");
        MAP.put("испания", "ES");
        MAP.put("оаэ", "AE");
        MAP.put("япония", "JP");
        MAP.put("австралия", "AU");
        MAP.put("канада", "CA");
        MAP.put("нидерланды", "NL");
        MAP.put("бельгия", "BE");
        MAP.put("швеция", "SE");
        MAP.put("польша", "PL");
        MAP.put("турция", "TR");
        MAP.put("мексика", "MX");
        MAP.put("бразилия", "BR");
        MAP.put("португалия", "PT");
        MAP.put("австрия", "AT");
        MAP.put("норвегия", "NO");
        MAP.put("дания", "DK");
        MAP.put("финляндия", "FI");
        MAP.put("ирландия", "IE");
        MAP.put("швейцария", "CH");
        MAP.put("чехия", "CZ");
        MAP.put("венгрия", "HU");
        MAP.put("румыния", "RO");
        MAP.put("грузия", "GE");
        MAP.put("израиль", "IL");
        MAP.put("китай", "CN");
        MAP.put("индия", "IN");
        MAP.put("корея", "KR");
        MAP.put("сингапур", "SG");
        MAP.put("аргентина", "AR");
        // английские названия (на случай ввода)
        MAP.put("armenia", "AM");
        MAP.put("russia", "RU");
        MAP.put("usa", "US");
        MAP.put("united states", "US");
        MAP.put("uk", "GB");
        MAP.put("united kingdom", "GB");
        MAP.put("germany", "DE");
        MAP.put("france", "FR");
        MAP.put("italy", "IT");
        MAP.put("spain", "ES");
        MAP.put("japan", "JP");
        MAP.put("australia", "AU");
        MAP.put("canada", "CA");
        MAP.put("turkey", "TR");
        MAP.put("georgia", "GE");
        MAP.put("israel", "IL");
        MAP.put("china", "CN");
        MAP.put("india", "IN");
    }

    /** Возвращает ISO-2 код или null если не найдено */
    public static String getCode(String countryName) {
        if (countryName == null || countryName.trim().isEmpty()) return null;
        return MAP.get(countryName.trim().toLowerCase());
    }
}