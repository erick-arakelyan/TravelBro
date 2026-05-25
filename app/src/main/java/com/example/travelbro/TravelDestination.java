package com.example.travelbro;

public class TravelDestination {
    public String country;
    public String city;
    public String emoji;
    public String description;
    public String highlights;
    public String bestTime;
    public int matchScore;

    public TravelDestination(String country, String city, String emoji,
                              String description, String highlights,
                              String bestTime, int matchScore) {
        this.country = country;
        this.city = city;
        this.emoji = emoji;
        this.description = description;
        this.highlights = highlights;
        this.bestTime = bestTime;
        this.matchScore = matchScore;
    }
}
