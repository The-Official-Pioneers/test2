package it.uniba.pioneers.testtool;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class InfoOpera extends AppCompatActivity {

    private TextView titolo;
    private TextView descrizione;
    private ImageView img;

    private EditText editableTitolo;
    private EditText editableDescrizione;

    private static FragmentSingolaOpera fso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_opera);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
     protected void onStart() {
         super.onStart();
         fso = new FragmentSingolaOpera();    // carica il fragment FragmentSingolaOpera(usato anche dalla MainActivity)
        androidx.fragment.app.FragmentManager supportFragmentManager;
        supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_list, fso)
                .commit();
     }

   public void modificaOpera(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Confermi")
                .setMessage("Confermare la modifica dell'opera?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {    // se utente conferma modifiche
                        String titolo = (String) fso.editableTitolo.getText().toString();
                        String descrizione = (String) fso.editableDescrizione.getText().toString();

                        if(titolo.equals(MainActivity.operaSelezionata.getTitolo()) && descrizione.equals(MainActivity.operaSelezionata.getDescrizione()) ) {
                            new AlertDialog.Builder(InfoOpera.this)
                                    .setMessage("Modifica almeno un campo per salvare")
                                    .setPositiveButton(android.R.string.yes,null)
                                    .show();

                        }else{
                            MainActivity.operaSelezionata.setTitolo(titolo);
                            MainActivity.operaSelezionata.setDescrizione(descrizione);
                            MainActivity.operaSelezionata.updateDataDb(InfoOpera.this);
                            new AlertDialog.Builder(InfoOpera.this)
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

    private boolean checkExit() {
        boolean c1 = String.valueOf(fso.editableTitolo.getText()).equals(MainActivity.operaSelezionata.getTitolo());
        boolean c2 = String.valueOf(fso.editableDescrizione.getText()).equals(MainActivity.operaSelezionata.getDescrizione());
        boolean c3 = MainActivity.fotoModificata;
        return (!c1 || !c2 || c3);
    }

    @Override
    public void onBackPressed(){     // controllo uscita senza salvare da tasto indietro
        if(MainActivity.tipoUtente.equals("curatore")) {
            if(checkExit()) {
                new AlertDialog.Builder(this)
                        .setTitle("Uscire?")
                        .setMessage("Uscire senza salvare le modifiche?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                InfoOpera.super.onBackPressed();
                                MainActivity.fotoModificata=false;
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
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {         // controllo uscita senza salvare da barra
        if(MainActivity.tipoUtente.equals("curatore")) {
            if(checkExit()) {
                new AlertDialog.Builder(this)
                        .setTitle("Uscire?")
                        .setMessage("Uscire senza salvare le modifiche?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
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
                return false;
            }
        }
        else {
            return false;
        }
        return true;
    }
}