package com.example.demo.config;


import com.example.demo.api.ClientApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retrofit.RetryCallAdapter;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class RestConfiguration {

    private Retry hardRetry() {
        Retry retryConfig = Retry.of("hardRetry", RetryConfig.<Response<String>>custom()
                .intervalFunction(IntervalFunction.ofExponentialBackoff(Duration.ofMillis(100L), 2))
                .maxAttempts(3)
                .retryOnResult(response -> response.code() == 404 ||
                        response.code() == 502 ||
                        response.code() == 429 ||
                        response.code() == 504 ||
                        response.code() == 401 ||
                        response.code() == 429)
                .retryExceptions(RuntimeException.class,
                        Exception.class,
                        IOException.class,
                        SocketTimeoutException.class,
                        ConnectException.class,
                        UnknownHostException.class)
                .failAfterMaxAttempts(false)
                .build());
        return RetryRegistry.of(retryConfig.getRetryConfig()).retry("hardRetry");
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(5000L, TimeUnit.MILLISECONDS)
                .readTimeout(5000L, TimeUnit.MILLISECONDS)
                .build();
    }

    private Retrofit.Builder retrofitBase(String baseUrl, OkHttpClient okHttpClient, ObjectMapper objectMapper){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(okHttpClient);
    }

    @Bean
    public Retrofit retrofit(OkHttpClient okHttpClient, ObjectMapper objectMapper) {
        return retrofitBase("http://localhost:8080", okHttpClient, objectMapper)
                .addCallAdapterFactory(RetryCallAdapter.of(hardRetry()))
                .build();
    }

    @Bean
    public ClientApi clientApi(Retrofit retrofit) {
        return retrofit.create(ClientApi.class);
    }
}
