package it.uniba.pioneers.testtool.VisualizzaVisite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Date;

import it.uniba.pioneers.data.Visita;
import it.uniba.pioneers.testtool.MainActivity;
import it.uniba.pioneers.testtool.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSingolaVisita#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSingolaVisita extends Fragment {

    Visita v = new Visita();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FragmentSingolaVisita() {

    }

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
        setRetainInstance(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(Bundle bd) {
        super.onActivityCreated(bd);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_singola_visita, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((VisiteCreateUtente)getActivity()).getSupportActionBar().setTitle(
                getString(R.string.visita) + " " + String.valueOf(VisiteCreateUtente.visitaSelezionata.getId()) +
                        " - " + VisiteCreateUtente.visitaSelezionata.getLuogo());
    }

    @Override
    public void onResume() {
        super.onResume();
        setDataVisita();
    }

    //Metodo necessario per impostare i dati della visita selezionata dalla lista
    //popolata precedentemente
    private void setDataVisita(){
        EditText num_visita = (EditText) getActivity().findViewById(R.id.txt_numero_visita);
        EditText luogo_visita = (EditText) getActivity().findViewById(R.id.txt_luogo_visita);
        EditText data_visita = (EditText) getActivity().findViewById(R.id.txt_data_visita);
        EditText guida_visita = (EditText) getActivity().findViewById(R.id.txt_guida_visita);

        //ID Visita
        num_visita.setText( String.valueOf(VisiteCreateUtente.visitaSelezionata.getId()) );
        //Luogo Visita
        luogo_visita.setText(VisiteCreateUtente.visitaSelezionata.getLuogo());
        //Data Visita
        Date visitDate = new Date( (VisiteCreateUtente.visitaSelezionata.getData())*1000 );
        data_visita.setText(FragmentVisiteCreateUtente.outputFormat.format(visitDate));
        //ID Guida Visita
        guida_visita.setText(String.valueOf(VisiteCreateUtente.visitaSelezionata.getGuida()));

        setVisibEliminaVisita();
        setVisibModificaGrafo();
        setVisSelezGuida();
    }

    //Metodo necessario per impostare la visibilità del bottone Modifica Grafo nel caso in cui
    //un visitatore sceglie Visite Predefinite o cerca tutte le visite in un luogo
    //Il bottone è necessario solo se visitatore/curatore museale visualizza una sua visita
    private void setVisibModificaGrafo(){

        if(MainActivity.tipoUtente.equals("visitatore")){
            if(MainActivity.flagVisite == 1 || checkCuratoreCreatoreVisita()){
                Button btn_mod_grafo = (Button) getActivity().findViewById(R.id.modifica_visita);
                btn_mod_grafo.setVisibility(View.GONE);
            } else {
                return;
            }
        } else if(MainActivity.tipoUtente.equals("guida")){
            Button btn_mod_grafo = (Button) getActivity().findViewById(R.id.modifica_visita);
            btn_mod_grafo.setVisibility(View.GONE);
        }


    }

    //Metodo necessario per impostare la visibilità del bottone Elimina Visita nel caso in cui
    //un visitatore sceglie Visite Predefinite o cerca tutte le visite in un luogo
    //Il bottone è necessario solo se visitatore/curatore museale visualizza una sua visita
    private void setVisibEliminaVisita(){
        if(MainActivity.tipoUtente.equals("visitatore")) {
            if (MainActivity.flagVisite == 1 || checkCuratoreCreatoreVisita()) {
                Button btn_elimina_visita = (Button) getActivity().findViewById(R.id.btn_elimina_visita);
                btn_elimina_visita.setVisibility(View.GONE);
            } else {
                return;
            }
        } else if(MainActivity.tipoUtente.equals("guida")){
            Button btn_elimina_visita = (Button) getActivity().findViewById(R.id.btn_elimina_visita);
            btn_elimina_visita.setVisibility(View.GONE);
        }
    }

    //Metodo necessario per impostare la visibilità del bottone Seleziona Guida nel caso in cui
    //una guida sta vedendo le sue visite
    //Tale bottone deve essere disponibile solo per visitatore/curatore nel momento in cui
    //visualizzano le loro visite
    private void setVisSelezGuida(){
        if(MainActivity.tipoUtente.equals("guida")){
            Button btn_sel_guida = (Button) getActivity().findViewById(R.id.btn_scegli_guida);
            btn_sel_guida.setVisibility(View.GONE);
        }
    }

    //Metodo necessario per controllare se la visita scelta dal visitatore è di un curatore o meno
    private boolean checkCuratoreCreatoreVisita(){
        if(VisiteCreateUtente.visitaSelezionata.getTipo_creatore() == 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VisiteCreateUtente.visitaSelezionata = null;
    }

}
