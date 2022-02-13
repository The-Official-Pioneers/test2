package it.uniba.pioneers.testtool;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import it.uniba.pioneers.data.Visita;
import it.uniba.pioneers.data.users.CuratoreMuseale;

public class VisiteCreateUtente extends AppCompatActivity {

    public static List<Visita> listaVisite;

    private void addItemToLista(Visita vis){
        listaVisite.add(vis);
    }
    private Visita getItemFromLista(int i){
        return listaVisite.get(i);
    }

    public static Visita visitaSelezionata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        visitaSelezionata = null;
        listaVisite = null;

        setContentView(R.layout.activity_visite_create_utente);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startFrag();

    }

    private void startFrag(){

        try{
            CuratoreMuseale.getAllVisiteSingolo(this, MainActivity.curatore,
                    response -> {
                        try {
                            System.out.println(response);
                            if(response.getBoolean("status")){

                                //Visita e ListaVisite necessarie per popolare la ListView
                                Visita v;
                                listaVisite = new ArrayList<>();

                                JSONObject tmpObj = response.getJSONObject("data");
                                JSONArray arrayData = tmpObj.getJSONArray("arrVisite");

                                for(int i = 0; i < arrayData.length(); ++i){
                                    JSONObject visita = arrayData.getJSONObject(i);
                                    v = new Visita();
                                    try {
                                        v.setDataFromJSON(visita);
                                        addItemToLista(v);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }

                                startFragListaVisite();

                            }else{
                                System.out.println("Sium sium");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {

                    });
        } catch(Exception e){

        }

    }

    private void startFragListaVisite(){
        // carico il fragment per mostrare la lista delle visite
        FragmentVisiteCreateUtente flv = new FragmentVisiteCreateUtente();
        androidx.fragment.app.FragmentManager supportFragmentManager;
        supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                .replace(R.id.frame_visite_create, flv)
                .addToBackStack(null)
                .commit();
    }

    public void eliminaVisitaSingola(View view) {
        if(visitaSelezionata.getId() != 0){
            listaVisite.remove(visitaSelezionata);
            FragmentVisiteCreateUtente.arrayAdapter.notifyDataSetChanged();
            visitaSelezionata.deleteDataDb(this);
            visitaSelezionata = null;
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(visitaSelezionata == null){
            finish();
        }
    }
}