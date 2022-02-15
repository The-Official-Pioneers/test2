package it.uniba.pioneers.testtool;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Date;

import it.uniba.pioneers.data.Visita;
import it.uniba.pioneers.data.users.Guida;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSingolaVisita#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSingolaVisita extends Fragment {

    Visita v = new Visita();
    Guida g = new Guida();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentSingolaVisita() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SingolaVisita.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSingolaVisita newInstance(String param1, String param2) {
        FragmentSingolaVisita fragment = new FragmentSingolaVisita();
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
        return inflater.inflate(R.layout.fragment_singola_visita, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDataVisita();
    }

    @Override
    public void onResume() {
        super.onResume();
        setDataVisita();
    }

    private void setDataVisita(){

        EditText num_visita = (EditText) getActivity().findViewById(R.id.txt_numero_visita);
        EditText luogo_visita = (EditText) getActivity().findViewById(R.id.txt_luogo_visita);
        EditText data_visita = (EditText) getActivity().findViewById(R.id.txt_data_visita);
        EditText guida_visita = (EditText) getActivity().findViewById(R.id.txt_guida_visita);
        TextView nome_guida_visita = (TextView) getActivity().findViewById(R.id.txt_nome_guida_visita);

        g.setId(VisiteCreateUtente.visitaSelezionata.getGuida());
        g.readDataDb(getContext());

        //ID Visita
        num_visita.setText( String.valueOf(VisiteCreateUtente.visitaSelezionata.getId()) );

        //Luogo Visita
        luogo_visita.setText(VisiteCreateUtente.visitaSelezionata.getLuogo());

        //Data Visita
        Date visitDate = new Date( (VisiteCreateUtente.visitaSelezionata.getData())*1000 );
        data_visita.setText(FragmentVisiteCreateUtente.outputFormat.format(visitDate));

        //ID Guida Visita
        guida_visita.setText(String.valueOf(VisiteCreateUtente.visitaSelezionata.getGuida()));

        //Nome Guida Visita
        StringBuilder nomeGuida = new StringBuilder();
        nomeGuida.append(g.getNome());
        nomeGuida.append(" ");
        nomeGuida.append(g.getCognome());
        nome_guida_visita.setText(nomeGuida.toString());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VisiteCreateUtente.visitaSelezionata = null;
    }

}
