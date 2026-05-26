package com.erikarakelyan.travelbro;

public class ForecastDay {
    public final String date;
    public final String emoji;
    public final String description;
    public final int tempMax;
    public final int tempMin;

    public ForecastDay(String date, String emoji, String description,
                       int tempMax, int tempMin) {
        this.date = date;
        this.emoji = emoji;
        this.description = description;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
    }
}
