package com.example.farmtech_mobile.api;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.farmtech_mobile.api.ApiService;

public class RetrofitClient {
        private static final String BASE_URL = "http://10.0.0.219:5147/api/"; // Substitua com a URL da sua API
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
