package com.applex.zephyr_task_2.Interfaces;

import com.applex.zephyr_task_2.Models.DataModel;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

    String BASE_URL = "https://run.mocky.io/v3/";

    @GET("a0528e65-80c9-4172-9231-876a622f25ef")
    Call<DataModel> getBooksList();
}