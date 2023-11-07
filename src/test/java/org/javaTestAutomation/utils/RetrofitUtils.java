package org.javaTestAutomation.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {

    public static Retrofit getRetrofit(String url) {

        var retrofitBuilder = new Retrofit.Builder();

        retrofitBuilder
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create());

        return retrofitBuilder.build();
    }
}
