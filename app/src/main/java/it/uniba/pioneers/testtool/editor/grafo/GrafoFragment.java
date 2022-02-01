package it.uniba.pioneers.testtool.editor.grafo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.editor.listaNodi.ListaNodi;
import it.uniba.pioneers.testtool.editor.options.OptionsEditor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GrafoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GrafoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public LinearLayout layoutEditorGrafo = null;
    public LinearLayout grafoManipulateLayout = null;

    public DisplayGrafo displayGrafo = null;
    public ListaNodi listaNodi = null;
    public OptionsEditor optionsEditor = null;

    public GrafoFragment() {
        // Required empty public constructor
    }


    public static GrafoFragment newInstance(String param1, String param2) {
        GrafoFragment fragment = new GrafoFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grafo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutEditorGrafo = getView().findViewById(R.id.layoutEditorGrafo);
        grafoManipulateLayout = getView().findViewById(R.id.grafoManipulateLayout);

        displayGrafo = getView().findViewById(R.id.displayGrafo);
        listaNodi = getView().findViewById(R.id.listaNodi);
        optionsEditor = getView().findViewById(R.id.optionsGrafo);


        Log.v("grafoManipulateLayout", String.valueOf(grafoManipulateLayout));

        DisplayGrafo displayGrafo = new DisplayGrafo(getContext());

        displayGrafo.setId(R.id.displayGrafo);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        displayGrafo.setLayoutParams(param);


        ListaNodi listaNodi = new ListaNodi(getContext());
        listaNodi.setId(R.id.listaNodi);
        param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                3.0f
        );

        listaNodi.setLayoutParams(param);
        listaNodi.setVisibility(View.GONE);
        grafoManipulateLayout.addView(displayGrafo);
        grafoManipulateLayout.addView(listaNodi);
    }

}