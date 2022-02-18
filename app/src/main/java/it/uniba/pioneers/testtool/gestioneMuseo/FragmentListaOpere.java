package it.uniba.pioneers.testtool.gestioneMuseo;

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

import it.uniba.pioneers.testtool.MainActivity;
import it.uniba.pioneers.testtool.R;

public class FragmentListaOpere extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static ArrayAdapter<String> lvAdapter;
    public static ArrayList<String> lista;

    private String mParam1;
    private String mParam2;

    public FragmentListaOpere() {

    }

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
        MainActivity.toggle.setDrawerIndicatorEnabled(false);    // abilitazione della navigazione all'indietro dalla toolbar
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
    }

    /*
     * Quando viene richiamato, andiamo ad invalidare la toolbar per gestirne la
     * corretta visualizzazione degli action button che in questa particolare schermata
     * devono essere nascosti
     *
     * inoltre per non disorientare l'utente, viene impostato il nome dell'area a cui
     * appartengono le opere
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_lista_opere, container, false);
        MainActivity.toggle.setDrawerIndicatorEnabled(false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(MainActivity.areaSelezionata.getNome());   // sostituzione del titolo della toolbar con il nome dell'area selezionata
        return view;
    }

    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Se non ci sono opere nell'area selezionata, viene mostrata una textView
        if(MainActivity.opereArea.size()!=0){
            TextView txtNoOpere = (TextView) getActivity().findViewById(R.id.txt_no_opere);
            txtNoOpere.setVisibility(View.GONE);
        }
        //Abilitazione della freccia sulla toolbar per la navigazione all'indietro
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Creazione della lista di nomi di tutte le opere di una determinata area
        lista = new ArrayList<String>();
        for(int i = 0; i<MainActivity.opereArea.size(); i++){
            lista.add(MainActivity.opereArea.get(i).getTitolo());
        }

        ListView lv = (ListView) view.findViewById(R.id.listView);
        //Creazione dell'adapter per la listView
        lvAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                lista
        );
        lv.setAdapter(lvAdapter); // imposta l'adapter alla list view
        //Gestione del click su un'elemento della lista
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity.operaSelezionata = MainActivity.opereArea.get(i);
                MainActivity.currOpera=i;
                //Caricamento del fragment che mostrerà i dettagli dell'opera selezionata
                FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                MainActivity.fragmentSingolaOpera = new FragmentSingolaOpera();
                //Caricamento del fragment che mostrera l'opera selezionata
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                        .replace(R.id.fragment_container_list, MainActivity.fragmentSingolaOpera)
                        .addToBackStack(null)
                        .commit();
            }

        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Oggetti utili per la gestione delle operazioni disponibili
        MainActivity.opereArea=null;
        MainActivity.operaSelezionata=null;
        MainActivity.fotoModificata=false;
        //Modifica della toolbar in base a dove si trova l'utente
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.le_tue_aree);
    }
}