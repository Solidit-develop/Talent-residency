package com.example.servicesolidit.Search;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.servicesolidit.FeedFlow.CardAdapter;
import com.example.servicesolidit.ProviderInformationFlow.VisitProvider;
import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Dtos.Responses.SearchProvider.SearchProviderDto;
import com.example.servicesolidit.Utils.Dtos.Responses.SearchProvider.SearchProviderResponseDto;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Search extends Fragment implements SearchProviderView, CardAdapter.OnCardClickListener {
    private EditText etSearchPriovider;
    private ImageView imgSearchButton;
    private RecyclerView recyclreViewSearch;
    private SearchPresenter presenter;
    private SearchProviderAdapter adapter;

    private ArrayList<SearchProviderDto> providersFound;
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
            onShowProgress();
        });

        this.providersFound = new ArrayList<>();
        adapter = new SearchProviderAdapter(this.providersFound, this);
        recyclreViewSearch.setAdapter(adapter);
        recyclreViewSearch.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResultFound(SearchProviderResponseDto response) {
        Gson gson = new Gson();
        this.providersFound.clear();
        if(!response.getResponse().isEmpty()){
            this.providersFound.addAll(response.getResponse());
            Log.i("SearchClass", "Se encontró: " + response.getResponse().size());
        }else{
            Log.i("SearchClass", "No se encontraron negocios que coincidan");
        }
        onHideProgress();
        adapter.notifyDataSetChanged();
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

    @Override
    public void onCardClick(int idProvider) {
        Log.i("SearchClass", "Seleccionado el provider con id: " + idProvider);
        VisitProvider visitProvider = new VisitProvider(idProvider);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, visitProvider);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}