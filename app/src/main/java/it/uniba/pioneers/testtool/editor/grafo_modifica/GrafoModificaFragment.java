package it.uniba.pioneers.testtool.editor.grafo_modifica;

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

public class GrafoModificaFragment extends Fragment {
    public static LinearLayout layoutEditorGrafo = null;
    public static LinearLayout grafoManipulateLayout = null;
    public static LinearLayout listaNodiLinearLayout = null;
    public static GrafoModificaFragment grafoModificaFragment = null;


    public DisplayGrafoModifica displayGrafoModifica = null;
    public ListaNodi listaNodi = null;
    public OptionsEditor optionsEditor = null;
    public GrafoModifica graph = null;

    public GrafoModificaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        grafoModificaFragment = this;
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
        listaNodiLinearLayout = getView().findViewById(R.id.listaNodiLinearLayout);

        this.displayGrafoModifica = getView().findViewById(R.id.displayGrafoModifica);
        listaNodi = getView().findViewById(R.id.listaNodi);
        optionsEditor = getView().findViewById(R.id.optionsGrafo);

        Log.v("grafoManipulateLayout", String.valueOf(grafoManipulateLayout));

        DisplayGrafoModifica displayGrafoModifica = new DisplayGrafoModifica(getContext());

        displayGrafoModifica.setId(R.id.displayGrafoModifica);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        displayGrafoModifica.setLayoutParams(param);


        //ListaNodi listaNodi = new ListaNodi(getContext(), NodeType.OPERA);
        //listaNodi.setId(R.id.listaNodi);

        grafoManipulateLayout.addView(displayGrafoModifica);

        graph = displayGrafoModifica.graph;

        //listaNodi.setLayoutParams(param);
        //grafoManipulateLayout.addView(listaNodi);
    }

}