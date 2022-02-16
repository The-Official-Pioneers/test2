package it.uniba.pioneers.testtool.editor.grafo_visualizza;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import it.uniba.pioneers.data.Visita;
import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.VisualizzaVisite.VisiteCreateUtente;
import it.uniba.pioneers.testtool.editor.listaNodi.ListaNodi;
import it.uniba.pioneers.testtool.editor.options.OptionsEditor;

public class GrafoVisualizzaFragment extends Fragment {
    public static LinearLayout layoutEditorGrafo = null;
    public static LinearLayout grafoManipulateLayout = null;
    public static LinearLayout listaNodiLinearLayout = null;
    public static GrafoVisualizzaFragment grafoVisualizzaFragment = null;


    public DisplayGrafoVisualizza displayGrafoVisualizza = null;
    public ListaNodi listaNodi = null;
    public OptionsEditor optionsEditor = null;
    public GrafoVisualizza graph = null;

    public Visita visita = null;
    public GrafoVisualizzaFragment(Visita visita) {
        // Required empty public constructor
        this.visita = visita;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        grafoVisualizzaFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grafo_visualizza, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutEditorGrafo = getView().findViewById(R.id.layoutEditorGrafo);
        grafoManipulateLayout = getView().findViewById(R.id.grafoManipulateLayout);
        listaNodiLinearLayout = getView().findViewById(R.id.listaNodiLinearLayout);

        displayGrafoVisualizza = getView().findViewById(R.id.displayGrafoModifica);
        listaNodi = getView().findViewById(R.id.listaNodi);
        optionsEditor = getView().findViewById(R.id.optionsGrafo);

        Log.v("grafoManipulateLayout", String.valueOf(grafoManipulateLayout));

        DisplayGrafoVisualizza displayGrafoVisualizza = new DisplayGrafoVisualizza(getContext(), visita);

        displayGrafoVisualizza.setId(R.id.displayGrafoModifica);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        displayGrafoVisualizza.setLayoutParams(param);

        //ListaNodi listaNodi = new ListaNodi(getContext(), NodeType.OPERA);
        //listaNodi.setId(R.id.listaNodi);

        grafoManipulateLayout.addView(displayGrafoVisualizza);

        graph = displayGrafoVisualizza.graph;

        //listaNodi.setLayoutParams(param);
        //grafoManipulateLayout.addView(listaNodi);
    }

}