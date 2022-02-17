package it.uniba.pioneers.testtool.gestioneMuseo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import it.uniba.pioneers.testtool.MainActivity;
import it.uniba.pioneers.testtool.R;

public class FragmentSingolaOpera extends Fragment {
    public TextView titolo;
    public TextView descrizione;
    public static ImageView img;
    FragmentSingolaOpera fragmentSingolaOpera;

    public static EditText editableTitolo;
    public static EditText editableDescrizione;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    public FragmentSingolaOpera() {

    }

    public static FragmentSingolaOpera newInstance(String param1, String param2) {
        FragmentSingolaOpera fragment = new FragmentSingolaOpera();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle outState) {
        setRetainInstance(true);
        super.onCreate(outState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void setDataOpera(){   // metodo per mostrare le informazioni dell'opera in modalità diverse in base al tipo di utente(modalità: solo lettura oppure anche modifica)
            titolo = (TextView) getActivity().findViewById(R.id.txt_titolo);
            descrizione = (TextView) getActivity().findViewById(R.id.txt_descrizione);
            img = (ImageView) getActivity().findViewById(R.id.img_foto);
            // modifica visibilità degli elementi della UI in base al tipo di utente e se l'opera esiste o la si sta creando
            if (!MainActivity.tipoUtente.equals("curatore")) {
                FloatingActionButton modificaFoto = (FloatingActionButton) getActivity().findViewById(R.id.btn_modifica_img);
                modificaFoto.setVisibility(View.GONE);
            }
            if (MainActivity.tipoUtente.equals("curatore")) {
                editableTitolo = (EditText) getActivity().findViewById(R.id.txt_edit_titolo);
                editableDescrizione = (EditText) getActivity().findViewById(R.id.txt_edit_descrizione);

                if (MainActivity.operaSelezionata != null) {     // campi popolati in base all'opera selezionata
                    byte[] bytes = Base64.decode(MainActivity.operaSelezionata.getFoto(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    img.setImageBitmap(decodedByte);
                    editableTitolo.setText(MainActivity.operaSelezionata.getTitolo());
                    editableDescrizione.setText(MainActivity.operaSelezionata.getDescrizione());
                } else {
                    editableTitolo.setText("");
                    editableDescrizione.setText("");
                }

            } else {   // se utente non è curatore, non può modificare nulla

                byte[] bytes = Base64.decode(MainActivity.operaSelezionata.getFoto(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                img.setImageBitmap(decodedByte);

                editableTitolo = (EditText) getActivity().findViewById(R.id.txt_edit_titolo);
                editableDescrizione = (EditText) getActivity().findViewById(R.id.txt_edit_descrizione);
                editableTitolo.setVisibility(View.GONE);
                editableDescrizione.setVisibility(View.GONE);
                titolo.append('\n' + MainActivity.operaSelezionata.getTitolo());
                descrizione.append('\n' + MainActivity.operaSelezionata.getDescrizione());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.toggle.setDrawerIndicatorEnabled(false);    // abilitazione della navigazione all'indietro dalla toolbar
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(this.fragmentSingolaOpera != null){
            MainActivity.fragmentSingolaOpera = this.fragmentSingolaOpera;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        this.fragmentSingolaOpera = MainActivity.fragmentSingolaOpera;
        MainActivity.fragmentSingolaOpera=null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true); // invalidazione della toolbar
        return inflater.inflate(R.layout.fragment_singola_opera, container, false);
    }

    @Override
    public void onViewCreated( View view, Bundle outState) {
        super.onViewCreated(view, outState);
        MainActivity.toggle.setDrawerIndicatorEnabled(false);   // abilitazione della navigazione all'indietro
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setDataOpera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.operaSelezionata=null;
        MainActivity.fragmentSingolaOpera=null;
        if(MainActivity.qr){  // modifica della UI se questo fragment viene utilizzato per interagire con un opera attraverso il suo QR-code
            ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            MainActivity.toggle.setDrawerIndicatorEnabled(true);
            ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.il_tuo_museo);
            MainActivity.operaSelezionata=null;
            MainActivity.qr=false;
        }
    }
}