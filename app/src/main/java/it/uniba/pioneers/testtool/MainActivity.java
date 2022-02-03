package it.uniba.pioneers.testtool;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Response;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import it.uniba.pioneers.data.Area;
import it.uniba.pioneers.data.Opera;
import it.uniba.pioneers.data.Zona;
import it.uniba.pioneers.data.users.Visitatore;
import it.uniba.pioneers.testtool.databinding.ActivityMainBinding;
import it.uniba.pioneers.testtool.home.CaptureAct;
import it.uniba.pioneers.testtool.home.FragmentHomeCuratore;


public class MainActivity extends AppCompatActivity {
    public static int MY_PERMISSIONS_REQUEST_CAMERA=100;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private DrawerLayout drawer;
    public DialogNodeInfo dialogOperaInfo = new DialogNodeInfo();
    public FragmentHomeCuratore frag;
    public static Opera opera;
    public static Zona zona;
    public static ArrayList<Area> areeZona;
    public static Area areaSelezionata;
    public static FragmentListaAree fragmentListaAree;
    public static FragmentSingolaArea fragmentSingolaArea;
    public static ArrayList<Opera> opereArea;
    public static Opera operaSelezionata;
    public static FragmentListaOpere fragmentListaOpere;
    public static FragmentSingolaOpera fragmentSingolaOpera;
    public static int tipoUtente=1;
    public int idUtente;

    //AGGIUNTO DA IVAN
    public static Visitatore visitatore = new Visitatore();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        areeZona=new ArrayList<Area>();
        areaSelezionata=null;
        fragmentListaAree=null;
        fragmentSingolaArea=null;
        opereArea=new ArrayList<Opera>();
        operaSelezionata=null;
        fragmentListaOpere=null;
        fragmentSingolaOpera=null;


        Toolbar toolbar = findViewById(R.id.toolBarHome);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle); //aggiungo un listner al toggle
        toggle.syncState(); //Ruota il toggle quando viene cliccato


        visitatore.setId(2);
        visitatore.readDataDb(MainActivity.this, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Boolean status =  response.getBoolean("status");
                    if(status){
                        visitatore.setDataFromJSON(response.getJSONObject("data"));
                        Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "Non è stato possibile leggere i dati dal db", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        });


        /*** INIZIO TRANSAZIONE ***/
                                        //// if per tipo di utente e fragment da committare
        frag = new FragmentHomeCuratore();
        androidx.fragment.app.FragmentManager supportFragmentManager;
        supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_list, frag)
                .commit();

        /*** FINE TRANSAZIONE ***/
    }

    //se clicco il bottone back e sta aperto il drawer
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(tipoUtente==1) {
            if(operaSelezionata!=null){
                boolean c = String.valueOf(FragmentSingolaOpera.editableTitolo.getText()).equals(MainActivity.operaSelezionata.getTitolo());
                boolean c2 = String.valueOf(FragmentSingolaOpera.editableDescrizione.getText()).equals(MainActivity.operaSelezionata.getDescrizione());

                if(!c || !c2) {
                    new AlertDialog.Builder(this)
                            .setTitle("Uscire?")
                            .setMessage("Uscire senza salvare le modifiche?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.super.onBackPressed();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }else{
                    super.onBackPressed();
                }
            }
            /*else{
                super.onBackPressed();
            }*/
            else if(opereArea!=null){
                super.onBackPressed();
            }
            else if(areaSelezionata!=null){
                boolean c = String.valueOf(FragmentSingolaArea.editableNome.getText()).equals(MainActivity.areaSelezionata.getNome());
                if(!c) {
                    new AlertDialog.Builder(this)
                            .setTitle("Uscire?")
                            .setMessage("Uscire senza salvare le modifiche?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.super.onBackPressed();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }else{
                    super.onBackPressed();
                }
            }else if(areeZona!=null){
                super.onBackPressed();
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void goEditorActivity(View view){
        Intent intent = new Intent(this, EditorActivity.class);
        startActivity(intent);
    }

    public void scannerQr(View view) {
       scanCode();
    }
    private void scanCode(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permessi Camera")
                        .setMessage("Consentire all'app l'accesso alla camera per scansionare i QR delle opere, negando l'accesso non ci si potrà interagire")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
            }
        }else{
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setCaptureActivity(CaptureAct.class);
            integrator.setOrientationLocked(false);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Scanning code...");
            integrator.initiateScan();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100: {
                // Se la richiesta è stata cancellata, l'array result è vuoto
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    IntentIntegrator integrator = new IntentIntegrator(this);
                    integrator.setCaptureActivity(CaptureAct.class);
                    integrator.setOrientationLocked(false);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    integrator.setPrompt("Scanning code...");
                    integrator.initiateScan();
                }else{
                    new AlertDialog.Builder(this)
                            .setTitle("Permesso negato")
                            .setMessage("Permesso di accesso alla camera non concesso")
                            .setPositiveButton(android.R.string.ok, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
            return;
        }
    }
    private class Inserisci extends Thread{
        private final int id;
        public Inserisci(int id){
            this.id = id;
        }
        public void run(){
            operaSelezionata = new Opera();
            operaSelezionata.setId(this.id);
            operaSelezionata.readDataDb(MainActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null  ){
                if(result.getContents() != null) {
                    Inserisci i = new Inserisci(Integer.parseInt(result.getContents()));
                    i.setPriority(4);
                    i.start();
                    SystemClock.sleep(1000);


                    Intent informazioniOpera = new Intent(MainActivity.this, InfoOpera.class);
                    informazioniOpera.putExtra("tipoUtente", tipoUtente);  // curatore = 1, visitatore = 2, guida = 3
                    startActivity(informazioniOpera);
                }
                else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Nessun risultato")
                            .setPositiveButton(android.R.string.yes,null)
                            .show();
                }
            }
            else{
                super.onActivityResult(requestCode, resultCode, data);
            }
        }catch(Exception e){

        }
    }

    //AGGIUNTO DA IVAN
    public void goToPersonalArea(MenuItem item) throws InterruptedException {
        Intent intent = new Intent(this, AreaPersonaleVisitatore.class);
        startActivity(intent);
    }

    public void gestisciMuseo(View view) {
        Area.areeZona(this, 10 ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Boolean status = response.getBoolean("status");
                    areeZona = new ArrayList<Area>();

                    if (status) {
                        //JSONObject resultData = response.getJSONObject("data");
                        JSONArray resultAree = response.getJSONArray("data");

                        for(int i =0; i< resultAree.length(); i++) {
                            Area tmp = new Area();
                            tmp.setDataFromJSON(resultAree.getJSONObject(i));
                            areeZona.add(tmp);
                        }
                        FragmentListaAree fls = new FragmentListaAree();
                        androidx.fragment.app.FragmentManager supportFragmentManager;
                        supportFragmentManager = getSupportFragmentManager();
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container_list, fls)
                                .addToBackStack(null)
                                .commit();
                    } else {
                        Toast.makeText(getApplicationContext(), "Non è avenuto nessun cambio dati, verifica che i valori siano validi", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void aggiungiArea(View view) {
        AlertDialog.Builder dialogInserimento = new AlertDialog.Builder(this);
        dialogInserimento.setTitle("Aggiungi una nuova area al tuo museo");
        dialogInserimento.setMessage("Inserisci il nome della nuova area");
        final EditText nomeArea = new EditText(this);
        dialogInserimento.setView(nomeArea);
        dialogInserimento.setNegativeButton(android.R.string.cancel, null);
        dialogInserimento.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!nomeArea.getText().toString().equals("")) {
                    Area nuovaArea = new Area();
                    nuovaArea.setNome(nomeArea.getText().toString());
                    nuovaArea.setZona(10);
                    nuovaArea.createDataDb(getApplicationContext());

                    FragmentListaAree.lista.add(nuovaArea.getNome());
                    areeZona.add(nuovaArea);
                    FragmentListaAree.lvAdapter.notifyDataSetChanged();
                }
            }
        });
        dialogInserimento.show();
    }

    public void eliminaArea(View view) {


    }

    public void modificaNomeArea(View view){
          new AlertDialog.Builder(this)
                    .setTitle("Confermi")
                    .setMessage("Confermi la modifica?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {    // se utente conferma modifiche
                            String nome = fragmentSingolaArea.editableNome.getText().toString();
                            if(nome == "" || nome.equals(areaSelezionata.getNome())){
                                new AlertDialog.Builder(MainActivity.this)
                                        .setMessage("Modifica almeno un campo per salvare")
                                        .setPositiveButton(android.R.string.yes,null)
                                        .show();
                            }else{
                                areaSelezionata.setNome(nome);
                                areaSelezionata.updateDataDb(MainActivity.this);
                                new AlertDialog.Builder(MainActivity.this)
                                        .setMessage("Modifica effettuata")
                                        .setPositiveButton(android.R.string.yes,null)
                                        .show();
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
    }

    public void mostraOpereArea(View view) {
        Opera.opereArea(this, MainActivity.areaSelezionata.getId() ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Boolean status = response.getBoolean("status");
                    opereArea = new ArrayList<Opera>();
                    if(status) {
                        JSONArray resultOpere = response.getJSONArray("data");
                        for (int i = 0; i < resultOpere.length(); i++) {
                            Opera tmp = new Opera();
                            tmp.setId(resultOpere.getJSONObject(i).getInt("id"));
                            tmp.setTitolo(resultOpere.getJSONObject(i).getString("titolo"));
                            tmp.setDescrizione(resultOpere.getJSONObject(i).getString("descrizione"));
                            tmp.setFoto(resultOpere.getJSONObject(i).getString("foto"));
                            tmp.setArea(resultOpere.getJSONObject(i).getInt("area"));
                            opereArea.add(tmp);
                        }
                    }
                        fragmentListaOpere = new FragmentListaOpere();
                        androidx.fragment.app.FragmentManager supportFragmentManager;
                        supportFragmentManager = getSupportFragmentManager();
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container_list, fragmentListaOpere)
                                .addToBackStack(null)
                                .commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void aggiungiOpera(View view) {
        fragmentSingolaOpera = new FragmentSingolaOpera();
        androidx.fragment.app.FragmentManager supportFragmentManager;
        supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_list, fragmentSingolaOpera)
                .addToBackStack(null)
                .commit();
    }
    public void eliminaOpera(View view) {


    }
    public void modificaOpera(View view) {
        if(operaSelezionata!=null) {
            new AlertDialog.Builder(this)
                    .setTitle("Confermi")
                    .setMessage("Confermare la modifica dell'opera?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {    // se utente conferma modifiche
                            String titolo = (String) fragmentSingolaOpera.editableTitolo.getText().toString();
                            String descrizione = (String) fragmentSingolaOpera.editableDescrizione.getText().toString();

                            if (titolo.equals(operaSelezionata.getTitolo()) && descrizione.equals(MainActivity.operaSelezionata.getDescrizione())) {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setMessage("Modifica almeno un campo per salvare")
                                        .setPositiveButton(android.R.string.yes, null)
                                        .show();

                            } else {
                                operaSelezionata.setTitolo(titolo);
                                operaSelezionata.setDescrizione(descrizione);
                                operaSelezionata.updateDataDb(MainActivity.this);
                                new AlertDialog.Builder(MainActivity.this)
                                        .setMessage("Modifica effettuata")
                                        .setPositiveButton(android.R.string.yes, null)
                                        .show();
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else{
            String titolo = (String) fragmentSingolaOpera.editableTitolo.getText().toString();
            String descrizione = (String) fragmentSingolaOpera.editableDescrizione.getText().toString();

            if (titolo.equals("") || descrizione.equals("")) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Aggiungi titolo e descrizione validi")
                        .setPositiveButton(android.R.string.yes, null)
                        .show();

            } else {
                Opera nuovaOpera = new Opera();

                nuovaOpera.setTitolo(titolo);
                nuovaOpera.setDescrizione(descrizione);
                nuovaOpera.setFoto("");
                nuovaOpera.setArea(areaSelezionata.getId());
                nuovaOpera.createDataDb(MainActivity.this);

                FragmentListaOpere.lista.add(nuovaOpera.getTitolo());
                opereArea.add(nuovaOpera);
                FragmentListaAree.lvAdapter.notifyDataSetChanged();

                operaSelezionata = nuovaOpera;
            }
        }
    }

}