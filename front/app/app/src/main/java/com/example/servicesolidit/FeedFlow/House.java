package com.example.servicesolidit.FeedFlow;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servicesolidit.Utils.Dtos.Responses.Feed.ProviderImageFeedResponseDto;
import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Constants;
import com.example.servicesolidit.ProviderInformationFlow.VisitProvider;
import com.example.servicesolidit.Utils.Dtos.Responses.ImagesRelational.ImagesInformationResponseDto;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class House extends Fragment implements HomeView, CardAdapter.OnCardClickListener {

    private RecyclerView recyclerView;
    private CardAdapter adapter;
    private List<CardModel> cardList;
    private HomePresenter presenter;
    private ProgressBar progressBar;
    private TextView noItemView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_house, container, false);
        presenter = new HomePresenter(this);

        noItemView = view.findViewById(R.id.noItemsView);
        progressBar = view.findViewById(R.id.houseProgressBar);
        recyclerView = view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 2);

        recyclerView.setLayoutManager(gridLayoutManager);

        cardList = new ArrayList<>();
        adapter = new CardAdapter(cardList, this.getContext(), this);
        recyclerView.setAdapter(adapter);
        showProgres();
        presenter.feed("0", "10");
        ReadShared();
        return view;
    }

    public void ReadShared(){
        try {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);
            int userIdLogged = sharedPreferences.getInt(Constants.GET_LOGGED_USER_ID, 0);
            Log.i("HouseClass", "El valor id del user almacenado es: " + userIdLogged);
        }catch (Exception e){
            Log.i("HouseClass", "Error on read shared: " + e.getMessage());
        }
    }

    @Override
    public void showProgres() {
        progressBar.setVisibility(View.VISIBLE);
        Log.i("HouseClass", "Should show progress");
    }

    @Override
    public void hideProgess() {
        progressBar.setVisibility(View.GONE);
        Log.i("HouseClass", "Should hide progress");
    }

    @Override
    public void onFeedSuccess(ArrayList<ProviderImageFeedResponseDto> feedResponse) {
        if(!feedResponse.isEmpty()){
            recyclerView.setVisibility(View.VISIBLE);
            noItemView.setVisibility(View.GONE);
            Log.i("HouseClass", "SomeFound");
            printFeed(feedResponse);
        }else{
            //Load emtpy view elements
            Log.i("HouseClass", "NotFoud");
            noItemView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        hideProgess();
    }

    @Override
    public void onFeedError(String error) {
        Log.i("HomeClass", "Should show error: " + error);
    }

    public void printFeed(ArrayList<ProviderImageFeedResponseDto> feedResponse){
        Log.i("HouseClass", "Feed Loaded with: " + feedResponse.size());
        cardList = this.getCardListFromResponse(feedResponse);
        adapter = new CardAdapter(cardList, this.getContext(),this);
        recyclerView.setAdapter(adapter);
    }

    private List<CardModel> getCardListFromResponse(ArrayList<ProviderImageFeedResponseDto> feedResponse) {
        List<CardModel> listToPrint = new ArrayList<CardModel>();

        if(feedResponse.isEmpty()){
            Toast.makeText(requireContext(), "No se encontraron vendedores", Toast.LENGTH_SHORT).show();
        }else {
            for (ProviderImageFeedResponseDto item : feedResponse) {
                CardModel modelFromResponse = new CardModel();
                Gson gson = new Gson();
                Log.i("HouseClass", "Info cargada: " + gson.toJson(item));
                modelFromResponse.setLocation(item.getProvedor().getAddressToFeed() != null && item.getProvedor().getAddressToFeed().getLocalidad() != null
                        ? item.getProvedor().getAddressToFeed().getLocalidad()
                        : "Negocio de confianza");
                modelFromResponse.setNameBussines(item.getProvedor().getWorkshopName());
                modelFromResponse.setDescription("Con " + item.getProvedor().getExperienceYearsToFeed() + " años de experiencia");
                modelFromResponse.setIdProvider(item.getProvedor().getIdProvider());
                String photoToPrint = getPhotoToPrintOnFeed(item.getImagen().getImageName());
                modelFromResponse.setImageUrl(Constants.BASE_URL + "images/print/" + photoToPrint);
                listToPrint.add(modelFromResponse);
            }
        }
        return listToPrint;
    }

    private String getPhotoToPrintOnFeed(ArrayList<ImagesInformationResponseDto> imageName) {
        String urlLocation = "no-photo";
        if(!imageName.isEmpty()){
            urlLocation = imageName.get(0).getUrlLocation();
        }
        return  urlLocation;
    }

    public void onCardClick(int idProvider){
        Log.i("HouseClass", "Seleccionado el provider con id: " + idProvider);
        VisitProvider visitProvider = new VisitProvider(idProvider);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, visitProvider);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}