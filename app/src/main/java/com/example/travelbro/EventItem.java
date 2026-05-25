package com.example.travelbro;

public class EventItem {
    public String title, location, city, country, date, emoji;

    public EventItem(String title, String location, String city, String country, String date, String emoji) {
        this.title = title;
        this.location = location;
        this.city = city;
        this.country = country;
        this.date = date;
        this.emoji = emoji;
    }
}