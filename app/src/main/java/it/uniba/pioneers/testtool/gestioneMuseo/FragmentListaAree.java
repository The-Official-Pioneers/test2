package it.uniba.pioneers.testtool.gestioneMuseo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

import it.uniba.pioneers.testtool.MainActivity;
import it.uniba.pioneers.testtool.R;

public class FragmentListaAree extends Fragment{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static ArrayAdapter<String> lvAdapter;
    public static ArrayList<String> lista;

    private String mParam1;
    private String mParam2;

    public FragmentListaAree() {

    }

    public static FragmentListaAree newInstance(String param1, String param2) {
        FragmentListaAree fragment = new FragmentListaAree();
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
        View view = inflater.inflate(R.layout.fragment_lista_aree, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.toggle.setDrawerIndicatorEnabled(false);
        //Abilitazione della navigazione all'indietro
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.il_tuo_museo);

        //Creazione della lista con i nomi di tutte le aree del museo
        lista = new ArrayList<String>();
        for(int i = 0; i<MainActivity.areeZona.size(); i++){
            lista.add(MainActivity.areeZona.get(i).getNome());
        }

        ListView lv = (ListView) view.findViewById(R.id.listView);
        //Creazione dell'adapter per la listView
        lvAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                lista
        );
        lv.setAdapter(lvAdapter);
        //Gestione del click su un'elemento della lista
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(),String.valueOf(MainActivity.areeZona.get(i).getId()), Toast.LENGTH_SHORT).show();
                MainActivity.areaSelezionata = MainActivity.areeZona.get(i);
                MainActivity.currArea=i;
                //Caricamento del fragment che mostrerà i dettagli dell'area selezionata
                FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                MainActivity.fragmentSingolaArea = new FragmentSingolaArea();
                //Caricamento del fragment che mostrera l'area selezionata
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                        .replace(R.id.fragment_container_list, MainActivity.fragmentSingolaArea)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.toggle.setDrawerIndicatorEnabled(false);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Oggetti utili per la gestione delle operazioni disponibili
        MainActivity.areeZona=null;
        MainActivity.areaSelezionata=null;
        MainActivity.opereArea=null;
        MainActivity.operaSelezionata=null;
        //Modifica della toolbar in base a dove si trova l'utente
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        MainActivity.toggle.setDrawerIndicatorEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.testtool);
    }
}