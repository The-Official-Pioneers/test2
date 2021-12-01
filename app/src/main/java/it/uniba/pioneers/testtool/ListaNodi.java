package it.uniba.pioneers.testtool;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import it.uniba.pioneers.testtool.databinding.FragmentFirstBinding;
import it.uniba.pioneers.widget.Node;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListaNodi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaNodi extends Fragment {

    private FragmentFirstBinding binding;


    public ListaNodi() {
        // Required empty public constructor
    }

    public static ListaNodi newInstance(String param1, String param2) {
        ListaNodi fragment = new ListaNodi();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFirstBinding.inflate(inflater, container, false);



        return inflater.inflate(R.layout.fragment_lista_nodi, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        Button b = getView().findViewById(R.id.button5);

        LinearLayout l = getView().findViewById(R.id.listaPalline);



        b.setOnClickListener(view1 -> {
            Toast.makeText(getContext(), "Ciao mondooo", Toast.LENGTH_LONG).show();
            l.addView(new Node(l.getContext()));

        });
    }


}