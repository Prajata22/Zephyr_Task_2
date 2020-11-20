package com.applex.zephyr_task_2.Utilities;

import com.applex.zephyr_task_2.Interfaces.APIInterface;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private static RetrofitHelper instance = null;
    private final APIInterface apiInterface;

    private RetrofitHelper() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(APIInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(APIInterface.class);
    }

    public static synchronized RetrofitHelper getInstance() {
        if (instance == null) {
            instance = new RetrofitHelper();
        }
        return instance;
    }

    public APIInterface getApiInterface() {
        return apiInterface;
    }
}