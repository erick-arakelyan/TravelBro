package com.example.travelbro;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlightApiService {
    @GET("v2/prices/latest")
    Call<FlightResponse> getCheapTickets(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("depart_date") String departDate,
            @Query("currency") String currency,
            @Query("token") String token,
            @Query("limit") int limit
    );
}