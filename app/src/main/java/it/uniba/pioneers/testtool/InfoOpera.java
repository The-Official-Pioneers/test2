package it.uniba.pioneers.testtool;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class InfoOpera extends AppCompatActivity {

    private TextView titolo;
    private TextView descrizione;
    private ImageView img;

    private EditText editableTitolo;
    private EditText editableDescrizione;

    int tipoUtente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_opera);

        titolo = (TextView) findViewById(R.id.txt_titolo);
        descrizione = (TextView) findViewById(R.id.txt_descrizione);
        img = (ImageView) findViewById(R.id.img_foto);
        Button modifica = (Button) findViewById(R.id.btn_modifica);

        byte[] bytes = Base64.decode(MainActivity.opera.getFoto(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        img.setImageBitmap(decodedByte);


        Intent info = getIntent();
        if(info != null) {
            tipoUtente = info.getIntExtra("tipoUtente", 0);
            if (tipoUtente == 1) {
                editableTitolo = (EditText) findViewById(R.id.txt_edit_titolo);
                editableDescrizione = (EditText) findViewById(R.id.txt_edit_descrizione);

                if(savedInstanceState!=null){
                    editableTitolo.setText(savedInstanceState.getString("titolo"));
                    editableDescrizione.setText(savedInstanceState.getString("descrizione"));
                }
                editableTitolo.setText(MainActivity.opera.getTitolo());
                editableDescrizione.setText(MainActivity.opera.getDescrizione());
            }
            else{   // se utente non è curatore, non può modificare nulla
                editableTitolo = (EditText) findViewById(R.id.txt_edit_titolo);
                editableDescrizione = (EditText) findViewById(R.id.txt_edit_descrizione);
                editableTitolo.setVisibility(View.GONE);
                editableDescrizione.setVisibility(View.GONE);
                titolo.append('\n'+MainActivity.opera.getTitolo());
                descrizione.append('\n'+MainActivity.opera.getDescrizione());
                modifica.setVisibility(View.GONE);
            }
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void salvaModifiche(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Confermi")
                .setMessage("Confermare la modifica dell'opera?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {    // se utente conferma modifiche
                        String titolo = (String) editableTitolo.getText().toString();
                        String descrizione = (String) editableDescrizione.getText().toString();

                        if(titolo.equals(MainActivity.opera.getTitolo()) && descrizione.equals(MainActivity.opera.getDescrizione()) ) {
                            new AlertDialog.Builder(InfoOpera.this)
                                    .setMessage("Modifica almeno un campo per salvare")
                                    .setPositiveButton(android.R.string.yes,null)
                                    .show();

                        }else{
                            MainActivity.opera.setTitolo(titolo);
                            MainActivity.opera.setDescrizione(descrizione);
                            MainActivity.opera.updateDataDb(InfoOpera.this);
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(editableTitolo.getText()!=null && editableDescrizione.getText()!=null){
            outState.putString("titolo", String.valueOf(editableTitolo.getText()));
            outState.putString("descrizione", String.valueOf(editableDescrizione.getText()));
        }
    }
    @Override
    public void onBackPressed(){     // controllo uscita senza salvare da tasto indietro
        if(tipoUtente==1) {
            boolean c = String.valueOf(editableTitolo.getText()).equals(MainActivity.opera.getTitolo());
            boolean c2 = String.valueOf(editableDescrizione.getText()).equals(MainActivity.opera.getDescrizione());

            if(!c || !c2) {
                new AlertDialog.Builder(this)
                        .setTitle("Uscire?")
                        .setMessage("Uscire senza salvare le modifiche?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                InfoOpera.super.onBackPressed();
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
        if(tipoUtente==1) {
            boolean c = String.valueOf(editableTitolo.getText()).equals(MainActivity.opera.getTitolo());
            boolean c2 = String.valueOf(editableDescrizione.getText()).equals(MainActivity.opera.getDescrizione());

            if(!c || !c2) {
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