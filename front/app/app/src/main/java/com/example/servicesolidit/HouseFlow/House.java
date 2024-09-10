package com.example.servicesolidit.HouseFlow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.servicesolidit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link House#newInstance} factory method to
 * create an instance of this fragment.
 */
public class House extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public House() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment House.
     */
    // TODO: Rename and change types and number of parameters
    public static House newInstance(String param1, String param2) {
        House fragment = new House();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private RecyclerView recyclerView;
    private CardAdapter adapter;
    private List<CardModel> cardList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_house, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        cardList = new ArrayList<>();
        cardList.add(new CardModel("https://img.freepik.com/fotos-premium/pico-montana-nevada-galaxia-estrellada-majestad-ia-generativa_1038396-50.jpg","Universo","nuves y luna llena","tierra houhohhohohih, oihoihohohio,ohiohoihoih"));
        cardList.add(new CardModel("https://www.xtrafondos.com/thumbs/1_3490.jpg","Inspiracion", "Hombre araña birncando", "universo houhohhohohih, oihoihohohio,ohiohoihoih"));
        cardList.add(new CardModel("https://i.pinimg.com/564x/84/5f/fe/845ffebc3c5c0da953034f4c62577f66.jpg","Espacio","astronauta alienigena","ifninito houhohhohohih, oihoihohohio,ohiohoihoih"));
        cardList.add(new CardModel("https://i.pinimg.com/originals/e1/bf/12/e1bf12d2dcb97315ac0aefe56eb55255.jpg","Cubo rubick", "cubo rubick con escenarios","espacio houhohhohohih, oihoihohohio,ohiohoihoih"));
        cardList.add(new CardModel("https://i.pinimg.com/736x/84/22/bb/8422bb607da39d3b4a52385e73e41b86.jpg","Panda", "panda escuchando musica","Noche houhohhohohih, oihoihohohio,ohiohoihoih"));
        cardList.add(new CardModel("https://fondospantallamovil.com/wp-content/uploads/2024/01/08cba71f004b4b3fa04dbd030c3c65fatplv-photomode-image.jpeg","Control","control exbox para juegos","Mesa houhohhohohih, oihoihohohio,ohiohoihoih"));
        cardList.add(new CardModel("https://imagenesconfrasesbonitas.com/wp-content/uploads/fondos-de-pantalla-astronautas-para-celular-hd-4k-luna-muertos-galaxia-11-624x1109.jpg","Astronauta","astronauta perdido en el espacio","espacio houhohhohohih, oihoihohohio,ohiohoihoih"));
        cardList.add(new CardModel("https://p4.wallpaperbetter.com/wallpaper/505/695/701/skull-download-backgrounds-for-pc-wallpaper-preview.jpg","Calabera", "calabera verde tipo abstract","mural houhohhohohih, oihoihohohio,ohiohoihoih"));
        cardList.add(new CardModel("https://www.tenveo-video-conference-es.com/uploads/202312429/4k-video-conference-camera-for-business0029ee19-8462-4ebd-baea-e6fb890d7ab7.png","Camara","camara ultra HD 4k","Internet houhohhohohih, oihoihohohio,ohiohoihoih"));
        adapter = new CardAdapter(cardList, this.getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }
}