package com.example.servicesolidit.ProfileFlow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.servicesolidit.FeedFlow.House;
import com.example.servicesolidit.ProfileFlow.Presenter.RegisterPresenter;
import com.example.servicesolidit.ProfileFlow.View.RegisterBussinesView;
import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Dtos.Requests.UpdateToProviderRequestDto;
import com.example.servicesolidit.Utils.Dtos.Requests.UploadRelationalImageDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoProfileDto;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class RegisterBussines extends Fragment implements RegisterBussinesView {

    private int idProviderGenerated = -1;
    private String idImageLoaded = "not-init";
    private static final String[] PERMISSIONS_BEFORE_API33 = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final String[] PERMISSIONS_API33_AND_ABOVE = {
            android.Manifest.permission.READ_MEDIA_IMAGES
    };

    private ActivityResultLauncher<String[]> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> pickImageLauncher;


    private TextInputLayout tilBussinesName, tilSkills, tilPhoneNumber, tilExperiencia, tilEmailUser;
    private Button btnRegister;
    private final UserInfoProfileDto user;
    private RegisterPresenter presenter;
    private ProgressBar progressBar;

    private static final int REQUEST_IMAGE_PICK = 1;
    private ImageView imagePreview;
    Button selectImageButton;
    private Uri selectedImageUri;

    public RegisterBussines(UserInfoProfileDto userLogged){
        this.user = userLogged;
        Log.i("RegisterBussines", "UserLogged: " + userLogged.getNameUser());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_register_bussines, container, false);
        tilBussinesName = view.findViewById(R.id.tilBussinesNameRegister);
        tilSkills = view.findViewById(R.id.tilBussinesSkillsRegister);
        tilPhoneNumber = view.findViewById(R.id.tilBussinesPhoneRegister);
        tilExperiencia = view.findViewById(R.id.tilBussinesExperienceRegister);
        tilEmailUser = view.findViewById(R.id.tilBussinesEmailRegister);
        progressBar = view.findViewById(R.id.progressItemOnRegisterBussines);
        btnRegister = view.findViewById(R.id.btnRegisterBussines);
        imagePreview = view.findViewById(R.id.imagePreview);
        selectImageButton = view.findViewById(R.id.selectImageButton);

        selectImageButton.setOnClickListener(v -> handleImageSelection());

        this.presenter = new RegisterPresenter(this);
        tilEmailUser.getEditText().setText(this.user.getEmail());
        tilEmailUser.getEditText().setEnabled(false);

        // Set up permission request launcher
        setupPermissionLauncher();

        // Set up image picker launcher
        setupImagePickerLauncher();


        btnRegister.setOnClickListener(v->{
            showProgress();
            fullFormToUpdateProfile();
        });
        return view;
    }

    private void setupPermissionLauncher() {
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    boolean allGranted = true;
                    for (boolean granted : result.values()) {
                        if (!granted) {
                            allGranted = false;
                            break;
                        }
                    }
                    if (allGranted) {
                        openGallery();
                    } else {
                        Toast.makeText(getContext(), "Permisos denegados. No se puede acceder a la galería.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupImagePickerLauncher() {
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    getActivity();
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            imagePreview.setImageURI(selectedImageUri);
                        }
                    } else {
                        Toast.makeText(getContext(), "No se seleccionó ninguna imagen.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleImageSelection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                requestPermissionLauncher.launch(PERMISSIONS_API33_AND_ABOVE);
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                requestPermissionLauncher.launch(PERMISSIONS_BEFORE_API33);
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    public void fullFormToUpdateProfile(){
        if (validateInput()){
            String nameBussines = tilBussinesName.getEditText().getText().toString().trim();
            String skills = tilSkills.getEditText().getText().toString().trim();
            String phoneNumber = tilPhoneNumber.getEditText().getText().toString().trim();
            String experiencia = tilExperiencia.getEditText().getText().toString().trim();
            String emailUserLogged = this.user.getEmail();

            UpdateToProviderRequestDto requestDto = new UpdateToProviderRequestDto();

            requestDto.setWorkshopName(nameBussines);
            requestDto.setSkills(this.getSkills(skills));
            requestDto.setWorkshopPhone(phoneNumber);
            requestDto.setExperience(experiencia);
            requestDto.setEmail(emailUserLogged);
            requestDto.setLocalidad(this.user.getIdAddress().getLocalidad());
            requestDto.setNameState("");
            requestDto.setNameTown(this.user.getIdAddress().getTown().getNameTown());
            requestDto.setStr1(this.user.getIdAddress().getStreet1());
            requestDto.setStr2(this.user.getIdAddress().getStreet2());
            requestDto.setZipCode("0000");
            requestDto.setDescription("");


            Log.i("RegisterBussines", "Request created");
            this.presenter.convertUserToProvider(requestDto);
        }else{
            Log.i("RegisterBussines", "Request not valid");
        }
        hideProgress();
    }

    private String getPathFromUri(Context context, Uri uri) {
        String path = null;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            if (idx != -1) {
                path = cursor.getString(idx);
            }
            cursor.close();
        }
        return path;
    }

    private void uploadImage(Uri imageUri) {
        File file = new File(getPathFromUri(requireContext(), imageUri));
        if (file == null) {
            Toast.makeText(requireContext(), "Error al procesar la imagen", Toast.LENGTH_SHORT).show();
            hideProgress();
            return;
        }

        // Crear MultipartBody para el archivo
        RequestBody filePart = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), filePart);

        this.presenter.loadImageToProvider(imagePart);
    }

    private ArrayList<String> getSkills(String skill) {
        ArrayList<String> skills = new ArrayList<>();
        skills.add(skill);
        return skills;
    }

    private boolean validateInput() {
        boolean isValid = true;
        isValid &= validarCampoVacio(this.tilBussinesName, "Campo requerido");
        isValid &= validarCampoVacio(this.tilSkills, "Campo requerido");
        isValid &= validarCampoVacio(this.tilPhoneNumber, "Campo requerido");
        isValid &= validarCampoVacio(this.tilExperiencia, "Campo requerido");
        isValid &= validarCampoVacio(this.tilEmailUser, "Campo requerido");
        return isValid;
    }

    /**
     * Método auxiliar para validar si un campo está vacío
     *
     * @param inputLayout El TextInputLayout que contiene el campo a validar
     * @param mensajeError El mensaje a mostrar si el campo está vacío
     * @return boolean - true si el campo no está vacío, false si está vacío
     */
    private boolean validarCampoVacio(TextInputLayout inputLayout, String mensajeError) {
        String texto = inputLayout.getEditText().getText().toString().trim();
        if (texto.isEmpty()) {
            inputLayout.setError(mensajeError);
            return false;
        } else {
            inputLayout.setError(null);
            return true;
        }
    }

    @Override
    public void showProgress() {
        this.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        this.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onRegisterSuccess(String responseMessage) {
        if (selectedImageUri != null) {
            Log.i("RegisterBussines", "Should uploadImage");
            uploadImage(selectedImageUri);
        } else {
            hideProgress();
            Log.i("RegisterBussines", "Should not uploadImage");
            Toast.makeText(requireContext(), "Selecciona una imagen primero", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRegisterFails(String s) {
        Log.i("RegisterBussines", "onRegisterFails: " + s);
    }

    @Override
    public void onImageUploadSuccess(String idImage) {
        Log.i("RegisterBussines", "Id generated: " + idImage);
        this.idImageLoaded = idImage;
        getProviderIsFromUserId(getLoggedIdAsUser());
    }

    private int getLoggedIdAsUser() {
        return this.user.getIdUser();
    }

    /**
     * method to get provider id using user logged id
     * @param userId is the user logged
     */
    public void getProviderIsFromUserId(int userId){
       this.presenter.getProviderInformation(userId);
    }

    @Override
    public void onImageUploadError(String s) {
        Log.i("RegisterBussines", "ErrorOnImageUploadError: " + s);
    }

    @Override
    public void onSuccessGetInformationAsProvider(ProviderResponseDto result) {
        // Get Provider generated to path param
        this.idProviderGenerated = result.getIdProvidersss();
        // Use idImage as urlLocation

        // Use cat as profile
        String funcionality = "cat";
        UploadRelationalImageDto request = new UploadRelationalImageDto();
        request.setFuncionality(funcionality);
        request.setUrlLocation(this.idImageLoaded);

        this.presenter.generateRelationalInformation(request, idProviderGenerated);
    }

    @Override
    public void onErrorGetInformationAsProvider(String message) {
        Log.i("RegisterBussines", "onErrorGetInformationAsProvider: " + message);
    }

    @Override
    public void onSuccessRelationalInformationGenerated(String message) {
        hideProgress();
        Toast.makeText(requireContext(), "Servicio de Proveedor registrado correctamente", Toast.LENGTH_SHORT).show();
        House houseFragment = new House();
        navigateTo(houseFragment);
    }

    @Override
    public void onErrorRelationalInformationGenerated(String s) {
        Log.i("RegisterBussines", "onErrorRelationalInformation: " + s);
    }


    /**
     * Method to load a new fragment from the slide menu.
     * @param fragmentDestiny is the fragment to navigate.
     */
    public void navigateTo(Fragment fragmentDestiny) {
        Log.i("RegisterBussines", "Start slide transaction fragment");
        FragmentTransaction transactionTransaction = this.requireActivity().getSupportFragmentManager().beginTransaction();
        transactionTransaction.replace(R.id.frame_container, fragmentDestiny);
        transactionTransaction.addToBackStack(null);
        transactionTransaction.commit();
    }
}