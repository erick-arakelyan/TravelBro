package com.erikarakelyan.travelbro;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface DuffelApiService {
    @POST("air/offer_requests")
    Call<DuffelOfferResponse> searchFlights(
            @Header("Authorization") String token,
            @Header("Duffel-Version") String version,
            @Header("Accept") String accept,
            @Header("Content-Type") String contentType,
            @Body RequestBody body
    );
}
