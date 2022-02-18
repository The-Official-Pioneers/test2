package it.uniba.pioneers.testtool.editor.grafo_modifica;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import it.uniba.pioneers.data.Visita;
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
    public Visita visita = null;

    public GrafoModificaFragment(Visita visita) {
        this.visita = visita;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        grafoModificaFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        DisplayGrafoModifica displayGrafoModifica = new DisplayGrafoModifica(getContext(), visita);

        displayGrafoModifica.setId(R.id.displayGrafoModifica);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        displayGrafoModifica.setLayoutParams(param);
        grafoManipulateLayout.addView(displayGrafoModifica);

        graph = displayGrafoModifica.graph;
    }

}