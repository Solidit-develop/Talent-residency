package com.example.servicesolidit.Search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Models.Responses.SearchProvider.SearchProviderResponseDto;
import com.google.gson.Gson;

import java.util.List;

public class Search extends Fragment implements SearchProviderView{
    private EditText etSearchPriovider;
    private ImageView imgSearchButton;
    private RecyclerView recyclreViewSearch;
    private SearchPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        presenter = new SearchPresenter(this);
        etSearchPriovider = view.findViewById(R.id.etSearchProvider);
        imgSearchButton = view.findViewById(R.id.buttonSearch);
        recyclreViewSearch = view.findViewById(R.id.recyclerViewSearchprovider);

        imgSearchButton.setOnClickListener(v->{
            String item = etSearchPriovider.getText().toString();
            Toast.makeText(requireContext(), "Buscar " + item, Toast.LENGTH_SHORT).show();
            presenter.searchProvider(item);
        });



        return view;
    }

    @Override
    public void onResultFound(List<SearchProviderResponseDto> response) {
        Gson gson = new Gson();
        if(!response.isEmpty()){
            Log.i("SearchClass", "Se encontró: " + gson.toJson(response.get(0)));
        }else{
            Log.i("SearchClass", "No se encontraron negocios que coincidan");
        }
    }

    @Override
    public void onErrorResult(String error) {
        Log.i("SearchClass", "Ocurrió un error: " + error);
    }

    @Override
    public void onShowProgress() {

    }

    @Override
    public void onHideProgress() {

    }
}