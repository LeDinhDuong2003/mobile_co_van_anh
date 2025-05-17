// app/src/main/java/com/example/mobileproject/api/RetrofitClient.java
package com.example.mobileproject.api;

import retrofit2.Retrofit;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static ApiService apiService;

    public static ApiService getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://mockapi.example.com/")
                    .build();

            MockRetrofit mockRetrofit = new MockRetrofit.Builder(retrofit)
                    .networkBehavior(NetworkBehavior.create())
                    .build();

            BehaviorDelegate<ApiService> delegate = mockRetrofit.create(ApiService.class);
            apiService = new MockApiService(delegate);
        }
        return apiService;
    }
}