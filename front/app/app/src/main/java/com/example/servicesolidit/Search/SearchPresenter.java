package com.example.servicesolidit.Search;

import android.util.Log;

import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.example.servicesolidit.Utils.Dtos.Responses.SearchProvider.SearchProviderResponseDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenter {
    private ApiService service;
    private SearchProviderView view;

    public SearchPresenter(SearchProviderView view){
        this.view = view;
        this.service = RetrofitClient.getClient().create(ApiService.class);
    }

    public void searchProvider(String item){
        Call<List<SearchProviderResponseDto>> call = service.searchProvider(item);
        call.enqueue(new Callback<List<SearchProviderResponseDto>>() {
            @Override
            public void onResponse(Call<List<SearchProviderResponseDto>> call, Response<List<SearchProviderResponseDto>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    List<SearchProviderResponseDto> result = response.body();
                    Log.i("SearchPresenter", "Se encontraron " + result.size() + " resultados");
                    view.onResultFound(result);
                }else{
                    Log.i("SearchPresenter", "Ocurrió un error" + response.isSuccessful());
                }
            }

            @Override
            public void onFailure(Call<List<SearchProviderResponseDto>> call, Throwable t) {
                view.onErrorResult(t.getMessage());
            }
        });

    }

}
