package com.example.servicesolidit.ProviderInformationFlow;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servicesolidit.FullScreenImageDialog;
import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Constants;
import com.example.servicesolidit.Utils.Dtos.Requests.CreateCommentRequest;
import com.example.servicesolidit.Utils.Dtos.Requests.RelationalImagesRequestDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Comments.CommentsDto;
import com.example.servicesolidit.Utils.Dtos.Responses.ImagesRelational.RelationalImagesResponseDto;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Objects;


public class VisitProvider extends Fragment implements VisitProviderView{

    private VisitProviderPresenter presenter;
    private int idProviderToLoad, idLogged;
    private ImageView imageExampleOnCarousell;
    private Button btnInfProfile;
    private TextView tvNoImagesFound;
    private Button btnTryToStartReview;
    private EditText etCreateCommetn;
    private ProgressBar progressVisitProvider;
    private RecyclerView rvObtainComments;
    private VisitProviderAdapter adapter;
    private LinearLayout containerCommentSection;
    private ArrayList<CommentsDto> commentatiosList = new ArrayList<>();
    private TextView itemNoCommentsView;
    private String URL_TO_PRINT_IMAGE;

    public VisitProvider(int idProviderToLoad) {
        this.idProviderToLoad = idProviderToLoad;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_visit_provider, container, false);
        btnInfProfile = view.findViewById(R.id.btn_inf_profile_provider);
        imageExampleOnCarousell = view.findViewById(R.id.imageExampleOnCarousell);
        tvNoImagesFound = view.findViewById(R.id.tvNoImagesFound);
        itemNoCommentsView = view.findViewById(R.id.itemNoCommentsView);
        this.btnTryToStartReview = view.findViewById(R.id.btnTryToStartReview);
        this.etCreateCommetn = view.findViewById(R.id.etCreateCommetn);

        adapter = new VisitProviderAdapter(commentatiosList);
        rvObtainComments = view.findViewById(R.id.rvObtainComments);
        rvObtainComments.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvObtainComments.setAdapter(adapter);

        this.progressVisitProvider = view.findViewById(R.id.progressVisitProvider);
        this.containerCommentSection = view.findViewById(R.id.containerCommentSection);

        idLogged = getIdUserLogged();

        this.presenter = new VisitProviderPresenter(this);

        onShowProgress();
        RelationalImagesRequestDto requestDto = new RelationalImagesRequestDto();
        requestDto.setTable("Providers");
        requestDto.setFuncionalida("cat");
        requestDto.setIdUsedOn(String.valueOf(this.idProviderToLoad));
        this.presenter.getComments(this.idProviderToLoad);
        this.presenter.getProviderImagesByComments(requestDto);

        Log.i("VisitProvider", "Intenta buscar una interacción entre logged: " + idLogged + " y el provider selected: " + idProviderToLoad);
        this.enableCommentsSection(idLogged, idProviderToLoad);

        btnTryToStartReview.setOnClickListener(v->{
            String comment = etCreateCommetn.getText().toString();
            if(!comment.isEmpty()){
                CreateCommentRequest request = new CreateCommentRequest();
                request.setCalification("5");
                request.setComment(comment);
                request.setUrlLocation("No-Content");
                this.presenter.createComment(idLogged, idProviderToLoad, request);
                onShowProgress();
            }
        });

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
        imageExampleOnCarousell.setOnClickListener(v -> {
            // Lanza el diálogo con la imagen
            if(this.URL_TO_PRINT_IMAGE != "not-image"){
                FullScreenImageDialog dialog = FullScreenImageDialog.newInstance(this.URL_TO_PRINT_IMAGE);
                dialog.show(requireActivity().getSupportFragmentManager(), "FullscreenImageDialog");
            }
        });
        return view;
    }

    private void enableCommentsSection(int idLogged, int idProviderToLoad) {
        onShowProgress();
        Log.i("VisitProvider", "onEnableCommentSelection entre " + idLogged + " y " + idProviderToLoad);
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

            this.URL_TO_PRINT_IMAGE = Constants.BASE_URL + "images/print/"+url;
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
        Log.i("VisitProvider", "enableCommentsSection: " + enableToComment);
        if(enableToComment){
            this.containerCommentSection.setVisibility(View.VISIBLE);
        }else {
            this.containerCommentSection.setVisibility(View.GONE);
        }
        onHideProgress();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onSuccessObtainComments(ArrayList<CommentsDto> response) {
        Gson g = new Gson();
        Log.i("VisitProvider", "Comentarios: " +g.toJson(response));
        if(!response.isEmpty()){
            this.itemNoCommentsView.setVisibility(View.GONE);
            this.rvObtainComments.setVisibility(View.VISIBLE);
            this.commentatiosList.clear();
            this.commentatiosList = response;
            adapter = new VisitProviderAdapter(this.commentatiosList);
            this.rvObtainComments.setAdapter(adapter);
            Log.i("VisitProvider", "Comentarios deberían verse ");
        }else{
            this.rvObtainComments.setVisibility(View.GONE);
            this.itemNoCommentsView.setVisibility(View.VISIBLE);
            Log.i("VisitProvider", "Comentarios NO deberían verse ");
        }
        adapter.notifyDataSetChanged();
        onHideProgress();
    }

    @Override
    public void onErrorObtainComments(String s) {
        this.rvObtainComments.setVisibility(View.GONE);
        this.itemNoCommentsView.setVisibility(View.VISIBLE);
        onHideProgress();
    }

    @Override
    public void onCommentCreatedSuccess(String message) {
        Toast.makeText(requireContext(), "Comentario enviado....", Toast.LENGTH_SHORT).show();
        onShowProgress();
        this.presenter.getComments(this.idProviderToLoad);
        this.etCreateCommetn.setText("");
    }

    @Override
    public void onCommentCreatedError(String ocurriUnError) {
        Log.i("VisitProvider", "Error al enviar comentario: " + ocurriUnError);
        Toast.makeText(requireContext(), "Ocurrió un error al enviar tu comentario", Toast.LENGTH_SHORT).show();
    }


}