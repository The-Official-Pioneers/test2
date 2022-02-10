package it.uniba.pioneers.testtool;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSingolaOpera#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSingolaOpera extends Fragment {
    public TextView titolo;
    public TextView descrizione;
    public static ImageView img;
    FragmentSingolaOpera fragmentSingolaOpera;

    public static EditText editableTitolo;
    public static EditText editableDescrizione;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentSingolaOpera() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSingolaOpera.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSingolaOpera newInstance(String param1, String param2) {
        FragmentSingolaOpera fragment = new FragmentSingolaOpera();
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

    public void setDataOpera(){
        titolo = (TextView) getActivity().findViewById(R.id.txt_titolo);
        descrizione = (TextView) getActivity().findViewById(R.id.txt_descrizione);
        img = (ImageView) getActivity().findViewById(R.id.img_foto);
        // modifica visibilità della UI in base al tipo di utente e se l'opera esiste o la si sta creando
        if(MainActivity.tipoUtente.equals("curatore") && MainActivity.operaSelezionata!=null && !MainActivity.qr){
        }
        if(!MainActivity.tipoUtente.equals("curatore")){
            FloatingActionButton modificaFoto =(FloatingActionButton)getActivity().findViewById(R.id.btn_modifica_img);
            modificaFoto.setVisibility(View.GONE);
        }

        Intent info = getActivity().getIntent();
        if (info != null) {

            if (MainActivity.tipoUtente.equals("curatore")) {
                editableTitolo = (EditText) getActivity().findViewById(R.id.txt_edit_titolo);
                editableDescrizione = (EditText) getActivity().findViewById(R.id.txt_edit_descrizione);

                if(MainActivity.operaSelezionata!=null) {
                    byte[] bytes = Base64.decode(MainActivity.operaSelezionata.getFoto(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    img.setImageBitmap(decodedByte);
                    editableTitolo.setText(MainActivity.operaSelezionata.getTitolo());
                    editableDescrizione.setText(MainActivity.operaSelezionata.getDescrizione());
                }
                else{
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
    }

    @Override
    public void onResume() {
        super.onResume();
        if(this.fragmentSingolaOpera != null){
            MainActivity.fragmentSingolaOpera = this.fragmentSingolaOpera;
        }
        setDataOpera();
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
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_singola_opera, container, false);
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDataOpera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.operaSelezionata=null;
        MainActivity.fragmentSingolaOpera=null;
    }
}