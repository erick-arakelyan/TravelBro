package com.example.travelbro;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DuffelOffer {

    @SerializedName("id")
    public String id;

    @SerializedName("passengers")
    public List<PassengerRef> passengerIds;

    public static class PassengerRef {
        @SerializedName("id")
        public String id;
    }
    @SerializedName("total_amount")
    public String totalAmount;

    @SerializedName("total_currency")
    public String totalCurrency;

    @SerializedName("slices")
    public List<Slice> slices;

    @SerializedName("owner")
    public Owner owner;

    public static class Owner {
        @SerializedName("name")
        public String name;

        @SerializedName("iata_code")
        public String iataCode;
    }

    public static class Slice {
        @SerializedName("segments")
        public List<Segment> segments;
    }

    public static class Segment {
        @SerializedName("departing_at")
        public String departingAt;

        @SerializedName("arriving_at")
        public String arrivingAt;

        @SerializedName("origin")
        public Airport origin;

        @SerializedName("destination")
        public Airport destination;

        @SerializedName("duration")
        public String duration;
    }

    public static class Airport {
        @SerializedName("iata_code")
        public String iataCode;

        @SerializedName("city_name")
        public String cityName;
    }
}