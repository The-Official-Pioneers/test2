package it.uniba.pioneers.testtool.gestioneMuseo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    /*
    * Quando viene richiamato, andiamo ad invalidare la toolbar per gestirne la
    * corretta visualizzazione degli action button che in questa particolare schermata
    * devono essere nascosti
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_aree, container, false);
        setHasOptionsMenu(true);   // richiesta di invalidazione della toolbar per forzarne l'aggiornamento
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.toggle.setDrawerIndicatorEnabled(false);
        //Abilitazione della freccia sulla toolbar per la navigazione all'indietro
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.le_tue_aree);

        //Creazione della lista con i nomi di tutte le aree del museo
        lista = new ArrayList<String>();
        for(int i = 0; i<MainActivity.areeZona.size(); i++){
            lista.add(MainActivity.areeZona.get(i).getNome());
        }

        ListView lv = (ListView) view.findViewById(R.id.listView);
        //Creazione di un'array adapter per la listView
        lvAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                lista
        );
        lv.setAdapter(lvAdapter);  // imposto l'adapter per la list view
        //Gestione del click di un singolo un'elemento della lista
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity.areaSelezionata = MainActivity.areeZona.get(i);  // valorizzo l'oggetto contenente le informazioni dell'opera selezionata
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

    /*
    * Quando richiamto sostituisce, nella toolbar, il menu con la freccetta per tornare
    * indietro
     */
    @Override
    public void onResume() {
        super.onResume();
        MainActivity.toggle.setDrawerIndicatorEnabled(false);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Oggetti utili per la gestione delle operazioni disponibili sulla toolbar
        MainActivity.areeZona=null;
        MainActivity.areaSelezionata=null;
        MainActivity.opereArea=null;
        MainActivity.operaSelezionata=null;
        //sostituisce, nella toolbar, freccetta per tornare indietro con il menu
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        MainActivity.toggle.setDrawerIndicatorEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.e_culture_tool));
    }
}