package it.uniba.pioneers.testtool.AreaPersonale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.uniba.pioneers.data.users.Guida;
import it.uniba.pioneers.testtool.MainActivity;
import it.uniba.pioneers.testtool.R;

public class FragmentAreaPersonaleGuida extends Fragment {
    List<String> listaSpecializz = new ArrayList<>();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FragmentAreaPersonaleGuida() {

    }

    public static FragmentAreaPersonaleGuida newInstance(String param1, String param2) {
        FragmentAreaPersonaleGuida fragment = new FragmentAreaPersonaleGuida();
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
        return inflater.inflate(R.layout.fragment_area_personale_guida, container, false);
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        setDataGuida();
    }
    //Metodo necessario per impostare i dati della Guida all'interno dell'Area Personale
    private void setDataGuida(){
        //Imposto testo in italiano/inglese in base alla lingua del sistema
        setTextEditText();
        //Imposto i valori dello spinner
        setSpecializzazioniGuida(getContext());

        ImageView propic = (ImageView) getActivity().findViewById(R.id.img_propic);
        EditText nome = (EditText) getActivity().findViewById(R.id.txt_nome_opera);
        EditText cognome = (EditText) getActivity().findViewById(R.id.txt_cognome);
        EditText datanascita = (EditText) getActivity().findViewById(R.id.txt_datan);
        EditText email = (EditText) getActivity().findViewById(R.id.txt_email);

        if(MainActivity.guida.getPropic() != null){
            byte[] bytes = Base64.decode(MainActivity.guida.getPropic(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            propic.setImageBitmap(decodedByte);
            propic.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        nome.setText(MainActivity.guida.getNome());
        cognome.setText(MainActivity.guida.getCognome());
        email.setText(MainActivity.guida.getEmail());
        datanascita.setText(MainActivity.guida.getShorterDataNascita());
    }

    private void setTextEditText(){
        TextView nome = (TextView) getActivity().findViewById(R.id.nome_areap);
        TextView cognome = (TextView) getActivity().findViewById(R.id.cognome_areap);
        TextView datanascita = (TextView) getActivity().findViewById(R.id.datan_areap);
        TextView email = (TextView) getActivity().findViewById(R.id.email_areap);
        TextView specializzazione = (TextView) getActivity().findViewById(R.id.specializz_areap);
        Button modificaProfilo = (Button) getActivity().findViewById(R.id.btn_edit_profile);
        Button newPass = (Button) getActivity().findViewById(R.id.btn_edit_password);

        nome.setText(R.string.nome_areap);
        cognome.setText(R.string.cognome_areap);
        datanascita.setText(R.string.datan_areap);
        email.setText(R.string.email_areap);
        specializzazione.setText(R.string.specializz_areap);
        modificaProfilo.setText(R.string.modificap_areap);
        newPass.setText(R.string.nuovapass_areap);
    }

    //Gestine Spinner con Specializzazioni presenti nel DB
    public void setSpecializzazioniGuida(Context context){
        Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinner_specializzazione);
        try{
            Guida.getAllSpecializzazioni(getContext(),
                    response -> {
                        try {
                            if(response.getBoolean("status")){

                                JSONObject tmpObj = response.getJSONObject("data");
                                JSONArray arrayData = tmpObj.getJSONArray("arrSpecializzazioni");

                                for(int i = 0; i < arrayData.length(); ++i){
                                    JSONObject specializz = arrayData.getJSONObject(i);
                                    listaSpecializz.add(specializz.getString("unnest"));
                                }

                                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                                        android.R.layout.simple_spinner_item, listaSpecializz);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(spinnerAdapter);

                                //Imposto specializzazzione della guida nello spinner
                                Spinner specializzazione = (Spinner) getActivity().findViewById(R.id.spinner_specializzazione);
                                int selectedItemId = findActualPosition(MainActivity.guida.getSpecializzazione(),
                                        listaSpecializz);
                                specializzazione.setSelection(selectedItemId);

                            }else{
                                System.out.println(R.string.impossibile_procedere);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {

                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int findActualPosition(String actualSpecializ, List<String> listToCheck){
        for(int i = 0; i < listToCheck.size(); i++){
            if(actualSpecializ.equals( listToCheck.get(i) )){
                return i;
            }
        }
        return 1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}