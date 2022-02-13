package it.uniba.pioneers.testtool;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListaOpere#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListaOpere extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static ArrayAdapter<String> lvAdapter;
    public static ArrayList<String> lista;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentListaOpere() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListaOpere.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListaOpere newInstance(String param1, String param2) {
        FragmentListaOpere fragment = new FragmentListaOpere();
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
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_lista_opere, container, false);

        lista = new ArrayList<String>();         // creazione della lista di nomi di tutte le opere di una determinata area
        for(int i = 0; i<MainActivity.opereArea.size(); i++){
            lista.add(MainActivity.opereArea.get(i).getTitolo());
        }

        ListView lv = (ListView) view.findViewById(R.id.listView);
        lvAdapter = new ArrayAdapter<String>(         // creazione dell'adapter per la listView
                getActivity(),
                android.R.layout.simple_list_item_1,
                lista
        );
        lv.setAdapter(lvAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){   // gestione del click su un'elemento della lista
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity.operaSelezionata = MainActivity.opereArea.get(i);
                MainActivity.currOpera=i;
                FragmentManager fragmentManager= getActivity().getSupportFragmentManager();     // caricamento del fragment che mostrerà i dettagli dell'opera selezionata
                MainActivity.fragmentSingolaOpera = new FragmentSingolaOpera();
                fragmentManager.beginTransaction()          // caricamento del fragment che mostrera l'opera selezionata
                        .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                        .replace(R.id.fragment_container_list, MainActivity.fragmentSingolaOpera)
                        .addToBackStack(null)
                        .commit();
            }

        });
        MainActivity.toggle.setDrawerIndicatorEnabled(false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(MainActivity.areaSelezionata.getNome());   // sostituzione del titolo della toolbar con il nome dell'area selezionata
        return view;
    }
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(MainActivity.opereArea.size()!=0){    // se non ci sono opere nell'area selezionata, viene mostrata una textView
            TextView txtNoOpere = (TextView) getActivity().findViewById(R.id.txt_no_opere);
            txtNoOpere.setVisibility(View.GONE);
        }
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // abilitazione della navigazione all'indietro
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.opereArea=null;       // oggetti utili per la gestione delle operazioni disponibili
        MainActivity.operaSelezionata=null;
        MainActivity.fotoModificata=false;               // modifica della toolbar in base a dove si trova l'utente
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.il_tuo_museo);
    }
}