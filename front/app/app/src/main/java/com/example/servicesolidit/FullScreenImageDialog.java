package com.example.servicesolidit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class FullScreenImageDialog extends DialogFragment {

    private static final String ARG_IMAGE_URL = "image_url";

    // Método estático para crear una nueva instancia del diálogo con la URL de la imagen
    public static FullScreenImageDialog newInstance(String imageUrl) {
        FullScreenImageDialog dialog = new FullScreenImageDialog();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_URL, imageUrl);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla el diseño del diálogo
        View view = inflater.inflate(R.layout.fragment_full_screen_image_dialog, container, false);

        // Obtiene la URL de la imagen desde los argumentos
        String imageUrl = getArguments() != null ? getArguments().getString(ARG_IMAGE_URL) : null;

        // Referencia al ImageView
        ImageView imageView = view.findViewById(R.id.fullscreen_image);

        // Carga la imagen con Picasso
        if (imageUrl != null) {
            Picasso.get()
                    .load(imageUrl)
                    .fit()
                    .centerInside()
                    .error(R.drawable.lost)
                    .into(imageView);
        }

        // Cierra el diálogo al hacer clic en la imagen
        imageView.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Configura el tamaño del diálogo para que ocupe toda la pantalla
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.black);
        }
    }
}