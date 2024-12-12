package com.example.servicesolidit.ProviderInformationFlow;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Constants;
import com.example.servicesolidit.Utils.Models.Requests.RelationalImagesRequestDto;
import com.example.servicesolidit.Utils.Models.Responses.Appointment.AppointmentItemResponse;
import com.example.servicesolidit.Utils.Models.Responses.Appointment.AppointmentListResponse;
import com.example.servicesolidit.Utils.Models.Responses.ImagesRelational.RelationalImagesResponseDto;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;


public class VisitProvider extends Fragment implements VisitProviderView{

    private VisitProviderPresenter presenter;
    private int idProviderToLoad, idLogged;
    private ImageView imageExampleOnCarousell;
    private Button btnInfProfile;
    private TextView tvNoImagesFound;
    private Button btnTryToStartReview;

    public VisitProvider(int idProviderToLoad) {
        this.idProviderToLoad = idProviderToLoad;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_visit_provider, container, false);
        btnInfProfile = view.findViewById(R.id.btn_inf_profile_provider);
        btnTryToStartReview = view.findViewById(R.id.btnTryToStartReview);
        imageExampleOnCarousell = view.findViewById(R.id.imageExampleOnCarousell);
        tvNoImagesFound = view.findViewById(R.id.tvNoImagesFound);
        idLogged = getIdUserLogged();

        this.presenter = new VisitProviderPresenter(this);

        onShowProgress();
        RelationalImagesRequestDto requestDto = new RelationalImagesRequestDto();
        requestDto.setTable("providers");
        requestDto.setFuncionalida("comments");
        requestDto.setIdUsedOn(String.valueOf(this.idProviderToLoad));
        this.presenter.getProviderImagesByComments(requestDto);

        this.presenter.getAppointments(idLogged);

        Log.i("VisitProvider", "Intenta buscar una interacción entre logged: " + idLogged + " y el provider selected: " + idProviderToLoad);
            //this.presenter.tryToStartReview();


        btnInfProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerToProvider fragmentProviderView = new CustomerToProvider(idProviderToLoad);
                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragmentProviderView);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        Log.i("VisitProviderClass", "idProvider recibido:  " + idProviderToLoad);

        return view;
    }

    public int getIdUserLogged(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);
        int idUserLogged = sharedPreferences.getInt(Constants.GET_LOGGED_USER_ID, 0);
        return idUserLogged;
    }

    @Override
    public void onSuccessGetImageInformation(RelationalImagesResponseDto response) {
        onHideProgress();
        if(response.getImageName() != null){
            //Print image
            tvNoImagesFound.setVisibility(View.GONE);
            imageExampleOnCarousell.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(Constants.BASE_URL + "images/print/"+response.getImageName())
                    .placeholder(R.drawable.load)
                    .error(R.drawable.lost)
                    .into(imageExampleOnCarousell);
        }else{
            tvNoImagesFound.setVisibility(View.VISIBLE);
            imageExampleOnCarousell.setVisibility(View.GONE);
            onErrorGetImageInformation("Ocurrió un error al obtener la imagen: " + response.getMessage());
        }
    }

    @Override
    public void onErrorGetImageInformation(String error) {
        Log.i("VisitProviderClass","Error: " + error);
        onHideProgress();
    }

    @Override
    public void onShowProgress() {
        Log.i("VisitProviderClass","OnShowProgress");

    }

    @Override
    public void onHideProgress() {
        Log.i("VisitProviderClass","OnHideProgress");

    }

    @Override
    public void onSuccessObtainResponse(AppointmentListResponse result) {
        if(result != null){
            if(result.regreso != null){
                if(result.regreso.isEmpty()){
                    Log.i("VisitProvider", "No se encontró un appointment relacionado al user logged: " + idLogged);
                }else{
                    Gson gson = new Gson();
                    Log.i("VisitProvider", "Se econtró lo siguiente: " + gson.toJson(result));
                    boolean showButton = validateIfThereIsAppointmentBetweenLoggedAndSelected(result, idLogged, idProviderToLoad);
                    if(showButton){
                        this.btnTryToStartReview.setVisibility(View.VISIBLE);
                    }else{
                        this.btnTryToStartReview.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private boolean validateIfThereIsAppointmentBetweenLoggedAndSelected(AppointmentListResponse regreso, int idLogged, int idProviderToLoad) {
        boolean result = false;
        for (AppointmentItemResponse item : regreso.getRegreso()) {
            if(item.getIdProvider() == idProviderToLoad){
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public void onErrorObtainResponse(String s) {
        Log.i("VisitProvider", "Error: " + s);
    }
}