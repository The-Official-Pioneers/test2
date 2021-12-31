package it.uniba.pioneers.testtool;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InfoOpera extends AppCompatActivity {

    private TextView titolo;
    private TextView descrizione;
    private TextView altezza;
    private ImageView img;

    private EditText editableTitolo;
    private EditText editableDescrizione;
    private EditText editableAltezza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_opera);



        titolo = (TextView) findViewById(R.id.txt_titolo);
        descrizione = (TextView) findViewById(R.id.txt_descrizione);
        altezza = (TextView) findViewById(R.id.txt_altezza);
        img = (ImageView) findViewById(R.id.img_foto);

        byte[] bytes = Base64.decode(MainActivity.opera.getFoto(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        img.setImageBitmap(decodedByte);


        Intent info = getIntent();
        if(info != null) {
            int tipoUtente = info.getIntExtra("tipoUtente", 0);
            if (tipoUtente == 1) {
                editableTitolo = (EditText) findViewById(R.id.txt_edit_titolo);
                editableDescrizione = (EditText) findViewById(R.id.txt_edit_descrizione);
                editableAltezza = (EditText) findViewById(R.id.txt_edit_altezza);
                editableTitolo.setVisibility(View.VISIBLE);
                editableDescrizione.setVisibility(View.VISIBLE);
                editableAltezza.setVisibility(View.VISIBLE);
                editableTitolo.setText(MainActivity.opera.getTitolo());
                editableDescrizione.setText(MainActivity.opera.getDescrizione());
                editableAltezza.setText(String.valueOf(MainActivity.opera.getAltezza()));

                Button modifica = (Button) findViewById(R.id.btn_modifica);
                Button annulla = (Button) findViewById(R.id.btn_annulla);
                modifica.setVisibility(View.VISIBLE);
                annulla.setVisibility(View.VISIBLE);
            }
            else{
                titolo.append(MainActivity.opera.getTitolo());
                descrizione.append(MainActivity.opera.getDescrizione());
                altezza.append(String.valueOf(MainActivity.opera.getAltezza())+" metri");
            }
        }
    }

    public void salvaModifiche(View view) {
        String titolo = (String) editableTitolo.getText().toString();
        String descrizione = (String) editableDescrizione.getText().toString();
        int altezza = Integer.parseInt((String) editableAltezza.getText().toString());

        if(titolo.equals(MainActivity.opera.getTitolo()) && descrizione.equals(MainActivity.opera.getDescrizione()) && altezza==MainActivity.opera.getAltezza()) {
            Toast.makeText(this, "Modifica almeno un campo prima di salvare" ,Toast.LENGTH_LONG).show();
        }else{
            MainActivity.opera.setTitolo(titolo);
            MainActivity.opera.setDescrizione(descrizione);
            MainActivity.opera.setAltezza(altezza);
            MainActivity.opera.updateDataDb(this);
            Toast.makeText(this, "Modifica salvata" ,Toast.LENGTH_LONG).show();
        }
    }

    public void annullaModifiche(View view) {
        String titolo = (String) editableTitolo.getText().toString();
        String descrizione = (String) editableDescrizione.getText().toString();
        int altezza = Integer.parseInt((String) editableAltezza.getText().toString());

        finish();
    }
}