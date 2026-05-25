package com.example.travelbro;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DuffelOfferResponse {

    @SerializedName("data")
    public Data data;

    public static class Data {
        @SerializedName("offers")
        public List<DuffelOffer> offers;

        @SerializedName("id")
        public String id;
    }
}