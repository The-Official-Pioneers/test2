package it.uniba.pioneers.testtool;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.uniba.pioneers.data.Zona;

public class CreaVisita extends AppCompatActivity {

    private String selectedItem = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_visita);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setLuoghiVisite(this);
    }

    //Gestine Spinner con Luoghi presenti nel DB
    public void setLuoghiVisite(Context context){
        Spinner spinner = (Spinner) this.findViewById(R.id.spinner_scegli_luogo);

        try{
            Zona.getAllLuoghi(CreaVisita.this,
                    response -> {
                        try {
                            if(response.getBoolean("status")){

                                List<String> listaLuoghi = new ArrayList<>();

                                JSONObject tmpObj = response.getJSONObject("data");
                                JSONArray arrayData = tmpObj.getJSONArray("arrLuoghi");

                                for(int i = 0; i < arrayData.length(); ++i){
                                    JSONObject luogo = arrayData.getJSONObject(i);
                                    listaLuoghi.add(luogo.getString("nome"));
                                }

                                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                                        android.R.layout.simple_spinner_item, listaLuoghi);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(spinnerAdapter);

                            }else{
                                System.out.println("Sium sium");
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


    public void goToEditorAfterLuogo(View view) {
        Spinner spinner = (Spinner) this.findViewById(R.id.spinner_scegli_luogo);
        selectedItem = spinner.getSelectedItem().toString();

        /* PER ANTONIO
        * da dichiarare come variabile nella classe
        * private static FragmentEditor frag;
        *
        * CODICE NECESSARIO NELL'ACTIVITY PER SOSTITUIRE FRAME NEL LAYOUT DELL'ACTIVITY
        * CON QUELLO DEL FRAGMENT
        *
        frag = new FragmentEditor();
        androidx.fragment.app.FragmentManager supportFragmentManager;
        supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_activity, frag)
                .commit();
        *
        *
        * ===============================================================
        * DA AGGIUNGERE NEL FRAGMENT PER POTER CHIAMARE getSelectedItem()
        * CreaVisita act = (CreaVisita) getActivity();
        * String selectedItemFromAct = act.getSelectedItem();
        * ===============================================================
        */
    }

    public String getSelectedItem() {
        return selectedItem;
    }
}