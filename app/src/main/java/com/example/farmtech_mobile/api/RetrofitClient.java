package com.example.farmtech_mobile.api;



import static android.provider.Settings.System.getString;

import static androidx.appcompat.graphics.drawable.DrawableContainerCompat.Api21Impl.getResources;

import android.content.Context;
import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.farmtech_mobile.R;
import com.example.farmtech_mobile.api.ApiService;

public class RetrofitClient {
        private static final String BASE_URL = "";//inserir url da api
        private static Retrofit retrofit = null;

        public static Retrofit getClient() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }

        public static ApiService getApiService() {
            return getClient().create(ApiService.class);
        }
}
