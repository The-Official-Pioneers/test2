package it.uniba.pioneers.testtool.gestioneMuseo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import it.uniba.pioneers.testtool.MainActivity;
import it.uniba.pioneers.testtool.R;

public class FragmentSingolaArea extends Fragment {
    FragmentSingolaArea fragmentSingolaArea;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static EditText editableNome;

    private String mParam1;
    private String mParam2;

    public FragmentSingolaArea() {

    }

    public static FragmentSingolaArea newInstance(String param1, String param2) {
        FragmentSingolaArea fragment = new FragmentSingolaArea();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if(this.fragmentSingolaArea != null){
            MainActivity.fragmentSingolaArea = this.fragmentSingolaArea;
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_singola_area, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editableNome = (EditText) getActivity().findViewById(R.id.txt_edit_nome);
        editableNome.setText(MainActivity.areaSelezionata.getNome());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Oggetti utili per la gestione dei dati e delle operazioni disponibili all'utente
        MainActivity.areaSelezionata=null;
        MainActivity.opereArea=null;
        MainActivity.operaSelezionata=null;
    }

    @Override
    public void onPause() {
        super.onPause();
        this.fragmentSingolaArea = MainActivity.fragmentSingolaArea;
        MainActivity.fragmentSingolaArea = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(this.fragmentSingolaArea != null){
            MainActivity.fragmentSingolaArea = this.fragmentSingolaArea;
        }
    }
}