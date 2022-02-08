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
import it.uniba.pioneers.testtool.editor.grafo.node.NodeType;
import it.uniba.pioneers.testtool.editor.listaNodi.ListaNodi;
import it.uniba.pioneers.testtool.editor.options.OptionsEditor;

public class GrafoFragment extends Fragment {
    public static LinearLayout layoutEditorGrafo = null;
    public static LinearLayout grafoManipulateLayout = null;
    public static LinearLayout listaNodiLinearLayout = null;

    public DisplayGrafo displayGrafo = null;
    public ListaNodi listaNodi = null;
    public OptionsEditor optionsEditor = null;

    public GrafoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            //TODO EVENTUALE RESTORE
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
        listaNodiLinearLayout = getView().findViewById(R.id.listaNodiLinearLayout);

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


        //ListaNodi listaNodi = new ListaNodi(getContext(), NodeType.OPERA);
        //listaNodi.setId(R.id.listaNodi);


        grafoManipulateLayout.addView(displayGrafo);
        //listaNodi.setLayoutParams(param);
        //grafoManipulateLayout.addView(listaNodi);
    }

}