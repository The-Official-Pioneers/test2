package it.uniba.pioneers.testtool;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListaAree#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListaAree extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static ArrayAdapter<String> lvAdapter;
    public static ArrayList<String> lista;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentListaAree() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListaAree.
     */
    // TODO: Rename and change types and number of parameters
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
        lista = new ArrayList<String>();
        for(int i = 0; i<MainActivity.areeZona.size(); i++){
            lista.add(MainActivity.areeZona.get(i).getNome());
        }

        ListView lv = (ListView) view.findViewById(R.id.listView);
        lvAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                lista
        );
        lv.setAdapter(lvAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(),String.valueOf(MainActivity.areeZona.get(i).getId()), Toast.LENGTH_SHORT).show();
                MainActivity.areaSelezionata = MainActivity.areeZona.get(i);
                MainActivity.currArea=i;
                FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                MainActivity.fragmentSingolaArea = new FragmentSingolaArea();
                fragmentManager.beginTransaction()    // caricamento del fragment che mostrera l'area selezionata
                        .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                        .replace(R.id.fragment_container_list, MainActivity.fragmentSingolaArea)
                        .addToBackStack(null)
                        .commit();
            }
        });


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        MainActivity.toggle.setDrawerIndicatorEnabled(false);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.toggle.setDrawerIndicatorEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Il Tuo Museo");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.areeZona=null;
        MainActivity.areaSelezionata=null;
        MainActivity.opereArea=null;
        MainActivity.operaSelezionata=null;           // modifica della toolbar in base a dove si trova l'utente
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        MainActivity.toggle.setDrawerIndicatorEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("TestTool");
    }
}