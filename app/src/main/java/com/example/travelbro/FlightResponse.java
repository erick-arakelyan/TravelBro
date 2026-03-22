package com.example.travelbro;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class FlightResponse {

    @SerializedName("success")
    public boolean success;

    @SerializedName("data")
    public Map<String, FlightData> data;

    public static class FlightData {
        @SerializedName("price")
        public int price;

        @SerializedName("airline")
        public String airline;

        @SerializedName("flight_number")
        public int flightNumber;

        @SerializedName("departure_at")
        public String departureAt;

        @SerializedName("return_at")
        public String returnAt;

        @SerializedName("destination")
        public String destination;

        @SerializedName("origin")
        public String origin;
    }
}
