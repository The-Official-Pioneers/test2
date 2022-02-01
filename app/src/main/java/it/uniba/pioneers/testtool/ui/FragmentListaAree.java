package it.uniba.pioneers.testtool.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import it.uniba.pioneers.testtool.MainActivity;
import it.uniba.pioneers.testtool.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListaAree#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListaAree extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

        String[] lista = {      // da generalizzare con for
                MainActivity.areeZona.get(0).getNome(),
                MainActivity.areeZona.get(1).getNome(),
                MainActivity.areeZona.get(2).getNome(),
        };


        ListView lv = (ListView) view.findViewById(R.id.listView);
        ArrayAdapter<String> lvAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                lista
        );
        lv.setAdapter(lvAdapter);

        // Inflate the layout for this fragment
        return view;
    }



}