package com.example.farmtech_mobile.api;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.farmtech_mobile.api.ApiService;

public class RetrofitClient {
        private static final String BASE_URL = "https://9a94-2804-4230-21e-a7a-c1cb-ba52-e66d-4ed9.ngrok-free.app/api/"; // Substitua com a URL da sua API
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
