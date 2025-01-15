package com.example.servicesolidit.Network;

import com.example.servicesolidit.Utils.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitLocationsClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(final String bearerToken) {
        if (retrofit == null) {
            // Crear un Interceptor para agregar el encabezado Authorization
            Interceptor authInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException, IOException {
                    Request originalRequest = chain.request();
                    Request.Builder builder = originalRequest.newBuilder()
                            .header("Authorization", "Bearer " + bearerToken)
                            .method(originalRequest.method(), originalRequest.body());
                    Request modifiedRequest = builder.build();
                    return chain.proceed(modifiedRequest);
                }
            };

            // Crear un OkHttpClient con el Interceptor
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .build();

            // Crear el cliente Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.universal-tutorial.com/api/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
