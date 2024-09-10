package com.example.servicesolidit.Network;

import android.util.Log;

import com.example.servicesolidit.Utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            try {
                retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }catch (Exception e){
                e.printStackTrace();
                Log.e("ERROR", "Falla del retrofit: " + e);
            }
        }

        return retrofit;

    }
}
