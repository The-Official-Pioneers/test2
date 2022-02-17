package it.uniba.pioneers.testtool;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;

import com.android.volley.Response;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;

import it.uniba.pioneers.data.Area;
import it.uniba.pioneers.data.Opera;
import it.uniba.pioneers.data.Zona;
import it.uniba.pioneers.data.users.CuratoreMuseale;
import it.uniba.pioneers.data.users.Guida;
import it.uniba.pioneers.data.users.Visitatore;
import it.uniba.pioneers.testtool.AreaPersonale.AreaPersonale;
import it.uniba.pioneers.testtool.VisualizzaVisite.VisiteCreateUtente;
import it.uniba.pioneers.testtool.databinding.ActivityMainBinding;
import it.uniba.pioneers.testtool.editor.EditorActivity;
import it.uniba.pioneers.testtool.gestioneMuseo.FragmentListaAree;
import it.uniba.pioneers.testtool.gestioneMuseo.FragmentListaOpere;
import it.uniba.pioneers.testtool.gestioneMuseo.FragmentSingolaArea;
import it.uniba.pioneers.testtool.gestioneMuseo.FragmentSingolaOpera;
import it.uniba.pioneers.testtool.home.FragmentHomeCuratore;
import it.uniba.pioneers.testtool.home.FragmentHomeGuida;
import it.uniba.pioneers.testtool.home.FragmentHomeVisitatore;
import it.uniba.pioneers.testtool.home.HomeActivity;
import it.uniba.pioneers.testtool.network.NetworkChangeListener;


public class MainActivity extends AppCompatActivity {
    public static final int PICK_FROM_GALLERY = 1;
    public static final int SCAN_QR = 2;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    public static DrawerLayout drawer;
    public FragmentManager supportFragmentManager;
    public static ActionBarDrawerToggle toggle;
    public static Opera opera;
    public static Zona zona;
    public static boolean qr;
    public static ArrayList<Area> areeZona;
    public static Area areaSelezionata;
    public static FragmentListaAree fragmentListaAree;
    public static FragmentSingolaArea fragmentSingolaArea;
    public static int currArea=-1;
    public static ArrayList<Opera> opereArea;
    public static Opera operaSelezionata;
    public static String oldFoto;
    public static FragmentListaOpere fragmentListaOpere;
    public static FragmentSingolaOpera fragmentSingolaOpera;
    public static int currOpera=-1;
    public static String tipoUtente;
    public static boolean fotoModificata;
    public static boolean nuovaOpera;
    public static int idUtente;
    public static Bundle budleFragOpera;

    public static Visitatore visitatore = new Visitatore();
    public static CuratoreMuseale curatore = new CuratoreMuseale();
    public static Guida guida = new Guida();

    //Flag usato per capire se il visitatore vuole vedere le sue visite o quelle predefinite
    //2 = visite in base al luogo, 1 = visite predef, 0 = sue visite
    public static int flagVisite;

    public static String luogoToSearch;

    //Flag usato per capire se la guida vuole vedere le visite da fare o quelle già fatte
    //1 = visite da fare, 0 = già fatte
    public static int flagVisiteGuida;

    NetworkChangeListener networkChangeListener= new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // inizializzazione degli oggetti di supporto per gestionde dei dati e delle operazioni disponibili all'utente
        qr=false;
        areeZona=null;
        areaSelezionata=null;
        fragmentListaAree=null;
        fragmentSingolaArea=null;
        opereArea=null;
        operaSelezionata=null;
        fragmentListaOpere=null;
        fragmentSingolaOpera=null;
        fotoModificata=false;
        nuovaOpera=false;
        budleFragOpera=null;
                                        // in base al tipo di utente cheha eseguito il login carico un fragmentHome differente
        Intent intent = getIntent();
        if(intent.getStringExtra("typeUser")!=null) {
            tipoUtente = intent.getStringExtra("typeUser");
            idUtente = intent.getIntExtra("idUser", 1);
        }

       //tipoUtente = "curatore";
       //idUtente = 1; //curatore

        //tipoUtente = "visitatore";
        //IdUtente = 2; //visitatore

        //tipoUtente = "guida";
        //idUtente = 1004; //guida

        gestioneToolBar();

        switch(tipoUtente){    // caricamento della home corretta
            case "curatore":
                FragmentHomeCuratore fragC = new FragmentHomeCuratore();
                supportFragmentManager = getSupportFragmentManager();
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_list, fragC)
                        .commit();
                break;
            case "visitatore":
                FragmentHomeVisitatore fragV= new FragmentHomeVisitatore();
                supportFragmentManager = getSupportFragmentManager();
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_list, fragV)
                        .commit();

                break;
            case "guida":
                FragmentHomeGuida fragG = new FragmentHomeGuida();
                supportFragmentManager = getSupportFragmentManager();
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_list, fragG)
                        .commit();
                break;
            default:
                FragmentHomeVisitatore frag= new FragmentHomeVisitatore();
                androidx.fragment.app.FragmentManager supportFragmentManager;
                supportFragmentManager = getSupportFragmentManager();
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_list, frag)
                        .commit();

                break;
        }

    }

    private void gestioneToolBar() {
        Toolbar toolbar = findViewById(R.id.toolBarHome);
        toolbar.setTitle("E-culture Tool");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        toolbar.setLogo(R.mipmap.ic_launcher_menu);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.shuttle_gray));
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();

        // creazione della toolbar
        creaToolbar();

        switch(tipoUtente){ // lettura dati utente da db per popolare l'area personales
            case "visitatore":
                visitatore.setId(idUtente);
                visitatore.readDataDb(this, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean status =  response.getBoolean("status");
                            if(status){
                                MainActivity.visitatore.setDataFromJSON(response.getJSONObject("data"));
                            }else{
                                Toast.makeText(MainActivity.this, R.string.impossibile_leggere_dati, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case "guida":
                guida.setId(idUtente);
                guida.readDataDb(this);
                break;
            case "curatore":
                curatore.setId(idUtente);
                curatore.readDataDb(this);
                break;
        }

        if(tipoUtente.equals("visitatore")){
            EditText searchBar = (EditText) findViewById(R.id.text_cerca);
            searchBar.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    luogoToSearch = searchBar.getText().toString();
                    goToVisiteByLuogo(getCurrentFocus());
                    return true;
                }
                return false;
            });
        }

    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    public void creaToolbar() {
        Toolbar toolbar = findViewById(R.id.toolBarHome);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle); //aggiungo un listner al toggle
        toggle.syncState(); //Ruota il toggle quando viene cliccato
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areeZona != null || qr) {
                    onBackPressed();
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        creaToolbar();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);   // gestione del cambio di configurazione cosi da non riavviare l'activity
    }

    public void logOutMethod(MenuItem item){
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Vuoi effetturare il logout?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){    //se clicco il bottone back e sta aperto il drawer
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(tipoUtente.equals("curatore")) {
            if(operaSelezionata!=null){     // click del tasto back se ci sono delle modifiche non salvate di un'opera
                boolean c = String.valueOf(FragmentSingolaOpera.editableTitolo.getText()).equals(MainActivity.operaSelezionata.getTitolo());
                boolean c2 = String.valueOf(FragmentSingolaOpera.editableDescrizione.getText()).equals(MainActivity.operaSelezionata.getDescrizione());
                boolean c3 = fotoModificata;

                if(!c || !c2 || c3) {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.uscire)
                            .setMessage(R.string.uscire_no_salvare)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.super.onBackPressed();
                                    fotoModificata=false;
                                    if(oldFoto!=null) {
                                        operaSelezionata.setFoto(oldFoto);
                                    }
                                    oldFoto=null;
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
            else if(areaSelezionata!=null){   // click del tasto back se ci sono delle modifiche non salvate di un'area
                boolean c = String.valueOf(FragmentSingolaArea.editableNome.getText()).equals(MainActivity.areaSelezionata.getNome());
                if(!c) {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.uscire)
                            .setMessage(R.string.uscire_no_salvare)
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
            }else if(areeZona==null && !qr){   // uscita dall'app, viene premuto il tasto back quando ci si trova nella home
                new AlertDialog.Builder(this)
                        .setTitle(R.string.uscire)
                        .setMessage(R.string.uscire_sicuro_app)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                idUtente = 0;
                                tipoUtente = "";
                                finish();
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
        }else if(!qr){  // uscita dall'app, viene premuto il tasto back quando ci si trova nella home
            new AlertDialog.Builder(this)
                    .setTitle(R.string.uscire)
                    .setMessage(R.string.uscire_sicuro_app)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            idUtente = 0;
                            tipoUtente = "";
                            finish();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {           // listener degli action button
            case R.id.action_delete_opera:
                eliminaOpera(null);
            break;
            case R.id.action_save_opera:
                 modificaAggiungiOpera(null);
            break;
            case R.id.action_delete_area:
                eliminaArea(null);
            break;
            case R.id.action_save_area:
                modificaNomeArea(null);
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);  // modifica della visibilità degli action button in base alla posizion dell'utente(curatore)
        menu.findItem(R.id.action_delete_opera).setVisible(false);
        menu.findItem(R.id.action_save_opera).setVisible(false);
        menu.findItem(R.id.action_delete_area).setVisible(false);
        menu.findItem(R.id.action_save_area).setVisible(false);

        if(fragmentSingolaOpera != null && tipoUtente.equals("curatore")){
            if(!qr){
                menu.findItem(R.id.action_delete_opera).setVisible(true);
            }
            menu.findItem(R.id.action_save_opera).setVisible(true);
        }
        if(fragmentSingolaArea != null  && tipoUtente.equals("curatore")){
            menu.findItem(R.id.action_delete_area).setVisible(true);
            menu.findItem(R.id.action_save_area).setVisible(true);
        }
        if(operaSelezionata!=null || qr){
            toggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        return true;
    }

    public void goEditorActivity(View view){
        Intent intent = new Intent(this, EditorActivity.class);
        startActivity(intent);
    }

    public void scannerQr(View view) {  // listener per la funzionalità "interagisci con l'opera"
       scanCode();
    }
    private void scanCode(){   // controllo permessi camera
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.permessi_camera)
                        .setMessage(R.string.permessi_camera_consenti)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, SCAN_QR);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, SCAN_QR);
            }
        }else{   // se ci sono i permessi allora si prosegue allo scan del qr-code
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setCaptureActivity(CaptureAct.class);
            integrator.setOrientationLocked(false);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt(getResources().getString(R.string.scan_qr_code));
            integrator.initiateScan();
        }
    }
    public void modificaFoto(View view) {  // controllo permessi accesso alla galleria
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.permessi_galleria)
                        .setMessage(R.string.consentire_permessi_galleria)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
            }
        }else{
            Intent intent = new Intent(Intent.ACTION_PICK);   // selezione foto da galleria
            intent.setType("image/*");
            startActivityForResult(intent, PICK_FROM_GALLERY);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SCAN_QR:
                // Se la richiesta è stata cancellata, l'array result è vuoto
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    IntentIntegrator integrator = new IntentIntegrator(this);
                    integrator.setCaptureActivity(CaptureAct.class);
                    integrator.setOrientationLocked(false);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    integrator.setPrompt(getResources().getString(R.string.scan_qr_code));
                    integrator.initiateScan();
                }else{
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.permesso_negato)
                            .setMessage(R.string.permesso_negato_camera)
                            .setPositiveButton(android.R.string.ok, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                break;
            case PICK_FROM_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 1);
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.permesso_negato)
                            .setMessage(R.string.permesso_negato_galleria)
                            .setPositiveButton(android.R.string.ok, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                break;
        }
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != PICK_FROM_GALLERY && resultCode == RESULT_OK) {   // scansione del qr
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() != null) {
                    operaSelezionata = new Opera();
                    operaSelezionata.setId(Integer.parseInt(result.getContents()));
                    operaSelezionata.readDataDb(MainActivity.this, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Boolean status =  response.getBoolean("status");
                                if(status){
                                    operaSelezionata.setDataFromJSON(response.getJSONObject("data"));
                                    qr=true;
                                    fragmentSingolaOpera = new FragmentSingolaOpera();   // caricamento del fragment per mostrare i dettagli dell'opera scansionata
                                    androidx.fragment.app.FragmentManager supportFragmentManager;
                                    supportFragmentManager = getSupportFragmentManager();
                                    supportFragmentManager.beginTransaction()
                                            .setCustomAnimations(R.anim.fade_in, R.anim.slade_out, R.anim.fade_in, R.anim.slade_out)
                                            .replace(R.id.fragment_container_list, fragmentSingolaOpera)
                                            .addToBackStack(null)
                                            .commit();

                                }else{  // nessun risultato ottenuto dalla scansione
                                    operaSelezionata=null;
                                    qr=false;
                                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                    MainActivity.toggle.setDrawerIndicatorEnabled(true);

                                    new AlertDialog.Builder(MainActivity.this)
                                            .setMessage(R.string.nessun_risultato)
                                            .setPositiveButton(android.R.string.yes, null)
                                            .show();
                                    return;
                                }
                            } catch (JSONException | ParseException e) {
                                operaSelezionata=null;
                                qr=false;
                                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                MainActivity.toggle.setDrawerIndicatorEnabled(true);
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    operaSelezionata=null;
                    qr=false;
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    MainActivity.toggle.setDrawerIndicatorEnabled(true);
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage(R.string.nessun_risultato)
                            .setPositiveButton(android.R.string.yes, null)
                            .show();
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        } else if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {  // selezione di una foto dalla galleria
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Uri targetUri = data.getData();
            Bitmap bitmap;
            ImageView oldPropic = (ImageView) fragmentSingolaOpera.img;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                oldPropic.setImageBitmap(bitmap);
                if(operaSelezionata!=null){
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] b = baos.toByteArray();
                    String encImage = Base64.encodeToString(b, Base64.DEFAULT);
                    oldFoto=operaSelezionata.getFoto();  // salvo la vecchia foto per annullamento delle modifiche
                    operaSelezionata.setFoto(encImage);
                }

                Snackbar.make(getWindow().getDecorView().getRootView(), R.string.foto_impostata_successo,
                        Snackbar.LENGTH_LONG).show();
                fotoModificata = true;

            } catch (FileNotFoundException e) {
                Snackbar.make(getWindow().getDecorView().getRootView(), R.string.impossibile_procedere,
                        Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void goToPersonalArea(MenuItem item) throws InterruptedException { // controllo tipo utente
        Intent intent = new Intent(this, AreaPersonale.class);
        startActivity(intent);
    }

    public void goToPersonalAreaFromDashboard(View view)throws InterruptedException{ // controllo tipo utente
        Intent intent = new Intent(this, AreaPersonale.class);
        startActivity(intent);
    }

    public void gestisciMuseo(View view) {
        if (tipoUtente.equals("ospite")) {   // controllo sul tipo di utente
            new AlertDialog.Builder(this)
                    .setTitle(R.string.accedi)
                    .setMessage(R.string.registrati_accedi_museo)
                    .setPositiveButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {    // ottengo tutte le aree del museo del curatore corrente
            Area.areeZona(this, (int) curatore.getZona(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Boolean status = response.getBoolean("status");
                        areeZona = new ArrayList<Area>();
                        if (status) {
                            JSONArray resultAree = response.getJSONArray("data");
                            for (int i = 0; i < resultAree.length(); i++) {
                                Area tmp = new Area();
                                tmp.setDataFromJSON(resultAree.getJSONObject(i));
                                areeZona.add(tmp);
                            }
                        }
                        FragmentListaAree fls = new FragmentListaAree();   // carico il fragment per mostrare la lista delle aree
                        androidx.fragment.app.FragmentManager supportFragmentManager;
                        supportFragmentManager = getSupportFragmentManager();
                        supportFragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                                .replace(R.id.fragment_container_list, fls)
                                .addToBackStack(null)
                                .commit();
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void aggiungiArea(View view) {
        AlertDialog.Builder dialogInserimento = new AlertDialog.Builder(this);
        dialogInserimento.setTitle(R.string.aggiungi_nuova_arae);
        dialogInserimento.setMessage(R.string.inserisci_nome_area);
        final EditText nomeArea = new EditText(this);
        dialogInserimento.setView(nomeArea);
        dialogInserimento.setNegativeButton(android.R.string.cancel, null);
        dialogInserimento.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!nomeArea.getText().toString().equals("")) {
                    Area nuovaArea = new Area();    // inserimento nuva area nel db
                    nuovaArea.setNome(nomeArea.getText().toString());
                    nuovaArea.setZona((int) curatore.getZona());
                    nuovaArea.createDataDb(getApplicationContext(), response -> {

                    });

                    FragmentListaAree.lista.add(nuovaArea.getNome());
                    areeZona.add(nuovaArea);
                    FragmentListaAree.lvAdapter.notifyDataSetChanged();   // aggiorno la listView
                }
            }
        });
        dialogInserimento.show();
    }

    public void eliminaArea(View view) {     // eliminazione area con richiesta di conferma
        new AlertDialog.Builder(this)
                .setTitle(R.string.eliminazione)
                .setMessage(getString(R.string.conferma_eliminazione) + "\n" + getString(R.string.conferma_eliminazione_finale))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        areeZona.remove(currArea);
                        FragmentListaAree.lista.remove(areaSelezionata.getNome());
                        areeZona.remove(areaSelezionata);
                        FragmentListaAree.lvAdapter.notifyDataSetChanged();  // aggiorno la listView

                        areaSelezionata.deleteDataDb(getApplicationContext());  // aggiorno il db
                        areaSelezionata=null;
                        getSupportFragmentManager().popBackStack();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void modificaNomeArea(View view){   // modifica del nome dell'area con richiesta di conferma
          new AlertDialog.Builder(this)
                    .setTitle(R.string.confermi)
                    .setMessage(R.string.confermi_modifica)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {    // se utente conferma modifiche
                            String nome = fragmentSingolaArea.editableNome.getText().toString();
                            if(nome == "" || nome.equals(areaSelezionata.getNome())){
                                new AlertDialog.Builder(MainActivity.this)
                                        .setMessage(R.string.modifica_campo)
                                        .setPositiveButton(android.R.string.yes,null)
                                        .show();
                            }else{
                                areaSelezionata.setNome(nome);
                                areaSelezionata.updateDataDb(MainActivity.this);
                                new AlertDialog.Builder(MainActivity.this)
                                        .setMessage(R.string.modifica_effettuata)
                                        .setPositiveButton(android.R.string.yes,null)
                                        .show();
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
    }

    public void mostraOpereArea(View view) {    // ottengo tutte le opere di una area
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
                        fragmentListaOpere = new FragmentListaOpere();     // caricamento del fragment con la lista di tutte le opere di un'area scelta
                        androidx.fragment.app.FragmentManager supportFragmentManager;
                        supportFragmentManager = getSupportFragmentManager();
                        supportFragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                                .replace(R.id.fragment_container_list, fragmentListaOpere)
                                .addToBackStack(null)
                                .commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void aggiungiOpera(View view) {    // aggiunta di una nuova opera
        fragmentSingolaOpera = new FragmentSingolaOpera();
        androidx.fragment.app.FragmentManager supportFragmentManager;  // carico fragment per l'aggiunta/modifica di una opera
        supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.slade_out, R.anim.fade_in, R.anim.slade_out)
                .replace(R.id.fragment_container_list, fragmentSingolaOpera)
                .addToBackStack(null)
                .commit();
    }
    public void eliminaOpera(View view) {   // eliminazione di un'opera con richiesta di conferma
        if(operaSelezionata!=null) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.eliminazione)
                    .setMessage(R.string.conferma_eliminazione)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {    // se utente conferma modifiche
                            opereArea.remove(currOpera);

                            FragmentListaOpere.lista.remove(operaSelezionata.getTitolo());
                           // opereArea.remove(operaSelezionata);
                            FragmentListaOpere.lvAdapter.notifyDataSetChanged();  // aggiorno la listView

                            operaSelezionata.deleteDataDb(getApplicationContext());  // aggiorno il db
                            operaSelezionata = null;
                            getSupportFragmentManager().popBackStack();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    public void modificaAggiungiOpera(View view) {   // aggiunta e modifica di un'opera sfruttano lo stesso actionButton(nel caso della modifica l'oggetto operaSelezionata non è nullo)
        if(operaSelezionata!=null) {   // modifica opera gia esistente
            new AlertDialog.Builder(this)
                    .setTitle(R.string.confermi)
                    .setMessage(R.string.conferma_modifica_opera)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {    // se utente conferma modifiche
                            String titolo = (String) fragmentSingolaOpera.editableTitolo.getText().toString();
                            String descrizione = (String) fragmentSingolaOpera.editableDescrizione.getText().toString();
                            String encImage="";
                            if(!operaSelezionata.getFoto().equals("")) {
                                Bitmap image = ((BitmapDrawable) fragmentSingolaOpera.img.getDrawable()).getBitmap();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                                byte[] b = baos.toByteArray();
                                encImage = Base64.encodeToString(b, Base64.DEFAULT);
                            }
                            if (titolo.equals(operaSelezionata.getTitolo()) && descrizione.equals(MainActivity.operaSelezionata.getDescrizione()) && !fotoModificata) {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setMessage(R.string.modifica_campo)
                                        .setPositiveButton(android.R.string.yes, null)
                                        .show();

                            } else {
                                operaSelezionata.setTitolo(titolo);
                                operaSelezionata.setDescrizione(descrizione);
                                operaSelezionata.setFoto(encImage);
                                operaSelezionata.updateDataDb(MainActivity.this);  // salvataggio delle modifiche nel db
                                new AlertDialog.Builder(MainActivity.this)
                                        .setMessage(R.string.modifica_effettuata)
                                        .setPositiveButton(android.R.string.yes, null)
                                        .show();
                                fotoModificata=false;
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else{ // aggingi opera
            nuovaOpera=true;
            invalidateOptionsMenu();
            String titolo = (String) fragmentSingolaOpera.editableTitolo.getText().toString();
            String descrizione = (String) fragmentSingolaOpera.editableDescrizione.getText().toString();
            String encImage="";
            if(fotoModificata){
                Bitmap image = ((BitmapDrawable)fragmentSingolaOpera.img.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG,50,baos);
                byte[] b = baos.toByteArray();
                encImage = Base64.encodeToString(b, Base64.DEFAULT);

            }
            if (titolo.equals("") || descrizione.equals("")) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage(R.string.aggiungi_campi_validi)
                        .setPositiveButton(android.R.string.yes, null)
                        .show();

            } else {
                Opera nuova = new Opera();
                nuova.setTitolo(titolo);
                nuova.setDescrizione(descrizione);
                nuova.setFoto(encImage);

                nuova.setArea(areaSelezionata.getId());
                nuova.createDataDb(MainActivity.this);   // aggiunta dell'opera nel db

                FragmentListaOpere.lista.add(nuova.getTitolo());
                opereArea.add(nuova);
                FragmentListaAree.lvAdapter.notifyDataSetChanged();  // aggiorno la listView
                nuovaOpera=false;
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    public void creaVisita(View view) {
        Intent intent = new Intent(this, CreaVisita.class);
        startActivity(intent);
    }

    public void goToYourVisite(View view) {
        flagVisite = 0;
        flagVisiteGuida = 1;
        Intent intent = new Intent(this, VisiteCreateUtente.class);
        startActivity(intent);
    }

    public void goToVisitePredefinite(View view) {
        flagVisite = 1;
        Intent intent = new Intent(this, VisiteCreateUtente.class);
        startActivity(intent);
    }

    public void goToVisiteByLuogo(View view) {
        flagVisite = 2;
        Intent intent = new Intent(this, VisiteCreateUtente.class);
        startActivity(intent);
    }

    public void goToPastVisit(View view) {
        flagVisiteGuida = 0;
        Intent intent = new Intent(this, VisiteCreateUtente.class);
        startActivity(intent);
    }

}