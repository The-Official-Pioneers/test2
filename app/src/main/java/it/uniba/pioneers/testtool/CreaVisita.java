package it.uniba.pioneers.testtool;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

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
import it.uniba.pioneers.data.users.CuratoreMuseale;
import it.uniba.pioneers.testtool.editor.grafo_modifica.GrafoModificaFragment;

public class CreaVisita extends AppCompatActivity {

    private String selectedItem = "";
    public static Visita visita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_visita);
        gestioneToolBar();

        switch(MainActivity.tipoUtente){
            case "visitatore":
                setLuoghiVisite(this);
                break;
            case "curatore":
                findViewById(R.id.linear_crea_visita).setVisibility(View.GONE);
                startEditorCuratore();
        }
    }

    private void gestioneToolBar() {
        Toolbar toolbar = findViewById(R.id.toolBarCreaVisita);
        toolbar.setTitle("E-culture Tool");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        toolbar.setLogo(R.mipmap.ic_launcher_menu);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.shuttle_gray));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    //Avvia editor dopo aver selezionato il luogo dove effettuare la visita
    public void goToEditorAfterLuogo(View view) {
        //Creo una nuova visita
        visita = new Visita();

        //Riempi la visita appena creata con i dati necessari
        Spinner spinner = (Spinner) this.findViewById(R.id.spinner_scegli_luogo);
        selectedItem = spinner.getSelectedItem().toString();

        visita.setTipo_creatore(1);
        visita.setCreatore_visitatore(Math.toIntExact(MainActivity.visitatore.getId()));

        Date tmpDate = Date.from(Instant.now());
        Long tmpLong = tmpDate.getTime();
        visita.setData(tmpLong);
        visita.setLuogo(selectedItem);

        //Una volta inseriti i dati nella visita, la inserisco sul db per poi passare
        //alla sua composizione nell'editor
        visita.createDataDb(this, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Boolean status =  response.getBoolean("status");
                    if(status){
                        visita.setDataFromJSON(response.getJSONObject("data"));

                        GrafoModificaFragment grafoModificaFragment = new GrafoModificaFragment(visita);
                        androidx.fragment.app.FragmentManager supportFragmentManager;

                        findViewById(R.id.linear_crea_visita).setVisibility(View.GONE);

                        supportFragmentManager = getSupportFragmentManager();
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.frameCreaVisita, grafoModificaFragment)
                                .commit();

                    }else{
                        Toast.makeText(getApplicationContext(), R.string.creazione_non_riuscita, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //Se è un curatore a creare la visita allora avvia editor con luogo quello della zona
    //del curatore attuale
    public void startEditorCuratore(){

        visita = new Visita();
        visita.setTipo_creatore(0);
        visita.setCreatore_curatore(Math.toIntExact(MainActivity.curatore.getId()));
        Date tmpDate = Date.from(Instant.now());
        Long tmpLong = tmpDate.getTime();
        visita.setData(tmpLong);

        try{
            CuratoreMuseale.getLuogoCuratore(this, MainActivity.curatore,
                    response -> {
                        try {
                            if(response.getBoolean("status")){
                                JSONObject tmpObj = response.getJSONObject("data");
                                JSONArray arrayData = tmpObj.getJSONArray("luogoCuratore");
                                JSONObject tmpObj2 = arrayData.getJSONObject(0);
                                visita.setLuogo(tmpObj2.getString("luogo"));

                                visita.createDataDb(this, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            Boolean status =  response.getBoolean("status");
                                            if(status){
                                                visita.setDataFromJSON(response.getJSONObject("data"));

                                                GrafoModificaFragment grafoModificaFragment = new GrafoModificaFragment(visita);
                                                androidx.fragment.app.FragmentManager supportFragmentManager;
                                                supportFragmentManager = getSupportFragmentManager();
                                                supportFragmentManager.beginTransaction()
                                                        .replace(R.id.frameCreaVisita, grafoModificaFragment)
                                                        .commit();
                                            }else{
                                                Toast.makeText(getApplicationContext(), R.string.impossibile_ottenere_luogo, Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException | ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }else{

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {

                    });
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}