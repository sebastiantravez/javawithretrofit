package com.example.demo.config;

import com.example.demo.api.ClientApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class RestConfiguration {

    @Primary
    @Bean
    public Retrofit retrofit(OkHttpClient okHttpClient, ObjectMapper objectMapper,
                             @Value("http://localhost:8080") String internalUrl) {

        return new Retrofit.Builder()
                .baseUrl(internalUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(okHttpClient)
                .build();
    }

    @Bean
    public OkHttpClient getHttpClientBuilder() {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(100, 5L, TimeUnit.MINUTES))
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    @Bean
    public ClientApi clientApi(Retrofit retrofit) {
        return retrofit.create(ClientApi.class);
    }
}
