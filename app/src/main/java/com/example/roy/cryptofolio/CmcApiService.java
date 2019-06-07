package com.example.roy.cryptofolio;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CmcApiService {
    String BASE_URL = "https://api.coingecko.com/api/v3/";
    /**
     * Create a retrofit client.
     */
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Headers({
            "Accepts: application/json"
    })
    @GET("coins/markets?vs_currency=usd")
    Call<List<CryptoCurrency>> getCryptoCurrency(@Query("per_page") int perPage,
                                                 @Query("page") int page);
}

