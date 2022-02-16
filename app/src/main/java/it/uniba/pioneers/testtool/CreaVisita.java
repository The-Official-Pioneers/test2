package it.uniba.pioneers.testtool;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.uniba.pioneers.data.Visita;
import it.uniba.pioneers.data.Zona;

public class CreaVisita extends AppCompatActivity {

    private String selectedItem = "";
    public static Visita visita;

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

    //Gestione Spinner con Luoghi presenti nel DB
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

        visita = new Visita();

        if(MainActivity.tipoUtente.equals("visitatore")){

            Spinner spinner = (Spinner) this.findViewById(R.id.spinner_scegli_luogo);
            selectedItem = spinner.getSelectedItem().toString();

            visita.setTipo_creatore(1);
            visita.setCreatore_visitatore(Math.toIntExact(MainActivity.visitatore.getId()));
            Date tmpDate = Date.from(Instant.now());
            Long tmpLong = tmpDate.getTime();
            visita.setData(tmpLong);
            visita.setLuogo(selectedItem);
            visita.createDataDb(this, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Boolean status =  response.getBoolean("status");
                        if(status){
                            visita.setDataFromJSON(response.getJSONObject("data"));
                            System.out.println("ID VISITA" + visita.getId());
                        }else{
                            Toast.makeText(getApplicationContext(), R.string.cambio_dati_no_validi, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                }
            });

        } else if(MainActivity.tipoUtente.equals("curatore")){

        }

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