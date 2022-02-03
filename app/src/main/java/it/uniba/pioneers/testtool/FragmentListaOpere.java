package it.uniba.pioneers.testtool;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_opere, container, false);

        lista = new ArrayList<String>();
        for(int i = 0; i<MainActivity.opereArea.size(); i++){
            lista.add(MainActivity.opereArea.get(i).getTitolo());
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
                Toast.makeText(getActivity(),String.valueOf(MainActivity.opereArea.get(i).getId()), Toast.LENGTH_SHORT).show();
                MainActivity.operaSelezionata = MainActivity.opereArea.get(i);
                FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                MainActivity.fragmentSingolaOpera = new FragmentSingolaOpera();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_list, MainActivity.fragmentSingolaOpera)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(MainActivity.opereArea.size()!=0){
            TextView txtNoOpere = (TextView) getActivity().findViewById(R.id.txt_no_opere);
            txtNoOpere.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.opereArea=null;
        MainActivity.operaSelezionata=null;
    }
}