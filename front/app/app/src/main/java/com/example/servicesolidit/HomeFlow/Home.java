package com.example.servicesolidit.HomeFlow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.servicesolidit.ApointmentFlow.ViewAgreements.ObtainAgreements;
import com.example.servicesolidit.ApointmentFlow.ViewAppointments.ObtainAppointments;
import com.example.servicesolidit.ConversationFlow.Conversation;
import com.example.servicesolidit.HeadDrawn;
import com.example.servicesolidit.FeedFlow.House;
import com.example.servicesolidit.MainActivity;
import com.example.servicesolidit.ProfileFlow.Profile;
import com.example.servicesolidit.R;
import com.example.servicesolidit.Search.Search;
import com.example.servicesolidit.Utils.Constants;
import com.example.servicesolidit.Utils.Dtos.Requests.UploadRelationalImageDto;
import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoProfileDto;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Home extends AppCompatActivity implements ActivityView{

    private final HeadDrawn headDrawn = new HeadDrawn();

    private final House firstFragment = new House();
    private final Search secondFragment = new Search();
    private final Profile thirdFragment = new Profile();
    private final Map<Integer, Fragment> fragmentMap = new HashMap<>();

    private final Map<Integer, Runnable> navigationActions = new HashMap<>();


    private DrawerLayout drawerLayout;
    private ImageView imagen;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ProgressBar idProgressBarHome;

    private ActivityResultLauncher<String[]> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private Uri selectedImageUri;
    private ImageView profileImage;
    private static final String[] PERMISSIONS_BEFORE_API33 = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final String[] PERMISSIONS_API33_AND_ABOVE = {
            android.Manifest.permission.READ_MEDIA_IMAGES
    };
    private boolean alreadyLoaded = false;

    private ActivityPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        this.presenter = new ActivityPresenter(this);

        fragmentMap.put(R.id.firstFragment, firstFragment);
        fragmentMap.put(R.id.secondFragment, secondFragment);
        fragmentMap.put(R.id.thirdFragment, thirdFragment);
        this.idProgressBarHome = findViewById(R.id.idProgressBarHome);

        drawerLayout = findViewById(R.id.drawer_home);
        imagen = findViewById(R.id.icon_menu);
        navigationView = findViewById(R.id.slide_drawn);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigateTo(firstFragment);

        toolbar = findViewById(R.id.menu_toolbar);
        setSupportActionBar(toolbar);


        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        View header = navigationView.getHeaderView(0);
        TextView userNameOnHeader = header.findViewById(R.id.txt_name_user_logged_on_header);
        TextView emailOnHeader = header.findViewById(R.id.txt_mail_user_logged_on_header);

        // Set up permission request launcher
        setupPermissionLauncher();

        // Set up image picker launcher
        setupImagePickerLauncher();
        profileImage = header.findViewById(R.id.imgProfileHeaderNav);

        profileImage.setOnClickListener(v->{
            if(!alreadyLoaded) {
                handleImageSelection();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);
        String userNameFromShared = sharedPreferences.getString(Constants.GET_NAME_USER, "");
        String emailFromShared = sharedPreferences.getString(Constants.GET_EMAIL_USER, "");

        userNameOnHeader.setText(userNameFromShared);
        emailOnHeader.setText(emailFromShared);

        //************************  metodos para el mapeo del slide de izquierda - derecha  ***************************************/
        navigationActions.put(R.id.item_message, () -> {
            Conversation conversationFragment = new Conversation();
            this.navigateTo(conversationFragment);
        });

        navigationActions.put(R.id.item_appointment, () -> {
            ObtainAppointments fragment = new ObtainAppointments();
            this.navigateTo(fragment);
        });

        navigationActions.put(R.id.item_agreements, () -> {
            ObtainAgreements fragment = new ObtainAgreements();
            this.navigateTo(fragment);
        });
/*
        navigationActions.put(R.id.item_record, () -> {
            Toast.makeText(this, "Hola desde historial", Toast.LENGTH_SHORT).show();
        });

        navigationActions.put(R.id.item_view_edit, () -> {
            Toast.makeText(this, "Hola desde ver y editar servicios", Toast.LENGTH_SHORT).show();
        });

        navigationActions.put(R.id.item_publish_service, () -> {
            Toast.makeText(this, "Hola desde publicar servicio", Toast.LENGTH_SHORT).show();
        });

 */

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Runnable action = navigationActions.get(item.getItemId());
                if(action !=null){
                    action.run();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
                return false;
            }
        });
        this.presenter.getImageInformation(getLoggedId());
    }

    //Metodos para la funcionalidad de las transiciones de la barra inferior
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = fragmentMap.get(item.getItemId());
            if (fragment != null) {
                navigateTo(fragment);
                return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_head, menu);
        // Cambiar el color de texto de los ítems
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString s = new SpannableString(item.getTitle());
            s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
            item.setTitle(s);
        }
        return true;
    }

    /**
     * Method to configure options icon.
     * @param item selected.
     * @return true or false.
     */
    @SuppressLint("ApplySharedPref")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_log_out) {
            // Limpiar SharedPreferences
            try {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit(); // Usar apply() en lugar de commit()

                // Crear Intent para ir al Login y limpiar la pila de actividades
                Intent loginActivity = new Intent(this, MainActivity.class);
                loginActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginActivity);

                // Finalizar la actividad actual
                finish();
            }catch (Exception e){
                Log.i("HomeClass", "Ocurrió un error: " + e.getMessage());
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                        Toast.makeText(getApplicationContext(), "Permisos denegados. No se puede acceder a la galería.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupImagePickerLauncher() {
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    getApplicationContext();
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            profileImage.setImageURI(selectedImageUri);
                            uploadImage(selectedImageUri);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "No se seleccionó ninguna imagen.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleImageSelection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                requestPermissionLauncher.launch(PERMISSIONS_API33_AND_ABOVE);
            }
        } else {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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


    /**
     * Send image to the presenter to make it persistent.
     * @param imageUri = image
     */
    private void uploadImage(Uri imageUri) {
        File file = new File(getPathFromUri(getApplicationContext(), imageUri));
        if (file == null) {
            Toast.makeText(getApplicationContext(), "Error al procesar la imagen", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear MultipartBody para el archivo
        RequestBody filePart = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), filePart);

        this.presenter.loadImageToProvider(imagePart);
    }


    /**
     * Method to load a new fragment from the slide menu.
     * @param fragmentDestiny is the fragment to navigate.
     */
    public void navigateTo(Fragment fragmentDestiny) {
        Log.i("HomeClass", "Start slide transaction fragment");
        FragmentTransaction transactionTransaction = this.getSupportFragmentManager().beginTransaction();
        transactionTransaction.replace(R.id.frame_container, fragmentDestiny);
        transactionTransaction.addToBackStack(null);
        transactionTransaction.commit();
    }

    @Override
    public void onShowProgress() {
        this.idProgressBarHome.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideProgress() {
        this.idProgressBarHome.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessSaveImage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        onHideProgress();
        this.alreadyLoaded = true;
    }

    @Override
    public void onErrorSaveImage(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessUploadImage(String response) {
        if(!response.isEmpty()){
            UploadRelationalImageDto request = new UploadRelationalImageDto();
            request.setUrlLocation(response);
            request.setFuncionality("profile");
            this.presenter.saveImageInformation(getLoggedId(), request);
        }
    }

    @Override
    public void onErrorUploadImage(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        onHideProgress();
    }

    @Override
    public void onLoadProfileSuccess(UserInfoProfileDto result) {
        String url =  !result.getImageName().isEmpty() ? result.getImageName().get(0).getUrlLocation() : "not-found-image";
        Picasso.get()
                .load(Constants.BASE_URL + "images/print/"+url)
                .placeholder(R.drawable.load)
                .error(R.drawable.logoimagen)
                .into(this.profileImage);
        this.alreadyLoaded = url != "not-found-image";
        onHideProgress();
    }

    @Override
    public void onLoadProfileError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public int getLoggedId(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);
        int userIdLogged = sharedPreferences.getInt(Constants.GET_LOGGED_USER_ID, 0);
        Log.i("ConversationClass", "IdLogged: " + userIdLogged);
        return  userIdLogged;
    }

}
