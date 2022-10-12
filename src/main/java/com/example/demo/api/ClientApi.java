package com.example.demo.api;

import com.example.demo.presentation.presenter.Client;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ClientApi {
    @GET("http://localhost:3000/client")
    Call<Client> getClientApi();
}
