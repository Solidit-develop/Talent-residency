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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Constants;
import com.example.servicesolidit.Utils.Dtos.Requests.RelationalImagesRequestDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Appointment.AppointmentItemResponse;
import com.example.servicesolidit.Utils.Dtos.Responses.Appointment.AppointmentListResponse;
import com.example.servicesolidit.Utils.Dtos.Responses.ImagesRelational.RelationalImagesResponseDto;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


public class VisitProvider extends Fragment implements VisitProviderView{

    private VisitProviderPresenter presenter;
    private int idProviderToLoad, idLogged;
    private ImageView imageExampleOnCarousell;
    private Button btnInfProfile;
    private TextView tvNoImagesFound;
    private Button btnTryToStartReview;
    private ProgressBar progressVisitProvider;

    private LinearLayout containerCommentSection;

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
        this.progressVisitProvider = view.findViewById(R.id.progressVisitProvider);
        this.containerCommentSection = view.findViewById(R.id.containerCommentSection);

        idLogged = getIdUserLogged();

        this.presenter = new VisitProviderPresenter(this);

        onShowProgress();
        RelationalImagesRequestDto requestDto = new RelationalImagesRequestDto();
        requestDto.setTable("Providers");
        requestDto.setFuncionalida("cat");
        requestDto.setIdUsedOn(String.valueOf(this.idProviderToLoad));
        this.presenter.getProviderImagesByComments(requestDto);

        Log.i("VisitProvider", "Intenta buscar una interacción entre logged: " + idLogged + " y el provider selected: " + idProviderToLoad);
        this.enableCommentsSection(idLogged, idProviderToLoad);


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

    private void enableCommentsSection(int idLogged, int idProviderToLoad) {
        onShowProgress();
        this.presenter.enableCommentsSection(idLogged, idProviderToLoad);
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
            Gson f = new Gson();
            Log.i("VisitProvider", "Debería imprimir imagen: " + f.toJson(response));
            tvNoImagesFound.setVisibility(View.GONE);
            imageExampleOnCarousell.setVisibility(View.VISIBLE);
            String url =  !response.getImageName().isEmpty() ? response.getImageName().get(0).getUrlLocation() : "not-found-image";
            Picasso.get()
                    .load(Constants.BASE_URL + "images/print/"+url)
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
        this.progressVisitProvider.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideProgress() {
        Log.i("VisitProviderClass","OnHideProgress");
        this.progressVisitProvider.setVisibility(View.GONE);
    }

    @Override
    public void onErrorEnableCommentsSection(String ocurrioUnError) {
        Log.i("VisitProvider", "ErrorOnEnableCommentsSection: " + ocurrioUnError);
        onHideProgress();
    }

    @Override
    public void enableCommentsSection(boolean enableToComment) {
        if(enableToComment){
            this.containerCommentSection.setVisibility(View.VISIBLE);
        }else {
            this.containerCommentSection.setVisibility(View.GONE);
        }
        onHideProgress();
    }


}