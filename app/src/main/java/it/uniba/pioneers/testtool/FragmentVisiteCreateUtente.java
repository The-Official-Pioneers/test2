package it.uniba.pioneers.testtool;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.uniba.pioneers.data.Visita;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentVisiteCreateUtente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentVisiteCreateUtente extends Fragment {

    public static SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static ArrayAdapter<String> arrayAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentVisiteCreateUtente() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentVisiteCreateUtente.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentVisiteCreateUtente newInstance(String param1, String param2) {
        FragmentVisiteCreateUtente fragment = new FragmentVisiteCreateUtente();
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
        return inflater.inflate(R.layout.fragment_visite_create_utente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        setLista();
    }

    private void setLista(){

        //ListView presente nel file activity_visite_create_utente.xml
        ListView lista_visite = (ListView) getActivity().findViewById(R.id.lista_visite_utente);
        //Adapter ListView
        setAdapterForList(VisiteCreateUtente.listaVisite, lista_visite);

        lista_visite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Position: "  + position);
                VisiteCreateUtente.visitaSelezionata = VisiteCreateUtente.listaVisite.get(position);
                startFragSingolaVisita();
            }
        });


    }

    private void setAdapterForList(List<Visita> lista, ListView listView){
        List<String> stringVisite = new ArrayList<>();
        Visita visitaToAdd;

        for(int x = 0; x < lista.size(); x++){
            visitaToAdd = VisiteCreateUtente.listaVisite.get(x);

            StringBuilder s = new StringBuilder();
            s.append("Visita: ");
            s.append(visitaToAdd.getId());
            s.append(" - ");
            s.append(visitaToAdd.getLuogo());
            s.append(" - ");
            Date d = new Date( (visitaToAdd.getData())*1000 );
            s.append(outputFormat.format(d));
            System.out.println("STRINGA: " + s);

            stringVisite.add(s.toString());
        }

        arrayAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_list_item_1,
                stringVisite);
        listView.setAdapter(arrayAdapter);
    }

    private void startFragSingolaVisita(){
        // carico il fragment per mostrare la lista delle visite
        FragmentSingolaVisita fsv = new FragmentSingolaVisita();
        androidx.fragment.app.FragmentManager supportFragmentManager;
        supportFragmentManager = getActivity().getSupportFragmentManager();
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                .replace(R.id.frame_visite_create, fsv)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VisiteCreateUtente.visitaSelezionata = null;
        VisiteCreateUtente.listaVisite = null;
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        MainActivity.toggle.setDrawerIndicatorEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.testtool);
    }

}