package it.uniba.pioneers.testtool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;

import it.uniba.pioneers.data.users.Visitatore;

public class AreaPersonaleVisitatore extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_area_personale_visitatore);

        ImageView propic = (ImageView) findViewById(R.id.img_propic);

        EditText nome = (EditText) findViewById(R.id.txt_nome);
        EditText cognome = (EditText) findViewById(R.id.txt_cognome);
        EditText datanascita = (EditText) findViewById(R.id.txt_datan);
        EditText email = (EditText) findViewById(R.id.txt_email);

        byte[] bytes = Base64.decode(MainActivity.visitatore.getPropic(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        propic.setImageBitmap(decodedByte);

        nome.setText(MainActivity.visitatore.getNome());
        cognome.setText(MainActivity.visitatore.getCognome());
        email.setText(MainActivity.visitatore.getEmail());
        datanascita.setText(MainActivity.visitatore.getShorterDataNascita());

    }

    public void editProfile(View view){
        System.out.println("A BOMBAZZA RAGAZZI OGGI ANDIAMO IN QUEL DI BARI A TROVARE BRAVO PETROSINO");

        EditText nome = (EditText) findViewById(R.id.txt_nome);
        EditText cognome = (EditText) findViewById(R.id.txt_cognome);
        EditText datanascita = (EditText) findViewById(R.id.txt_datan);
        EditText email = (EditText) findViewById(R.id.txt_email);

        if(checkForChanges(nome,cognome,datanascita,email)){
            System.out.println("SIUM CAMBIANO LE COSE SIUM");
            String newNome = nome.getText().toString();
            String newCognome = cognome.getText().toString();
            String newDatan = datanascita.getText().toString();
            String newEmail = email.getText().toString();

            try{

                MainActivity.visitatore.setNome(newNome);
                MainActivity.visitatore.setCognome(newCognome);
                MainActivity.visitatore.setEmail(newEmail);

                if(validateDate(newDatan)){
                    System.out.println("NEW VALID DATE");
                    MainActivity.visitatore.setDataNascita( Visitatore.output.parse(newDatan) );
                }

                System.out.println("NUOVA DATA DI NASCITA: " + MainActivity.visitatore.getDataNascita());
                System.out.println("NUOVA 1: " + MainActivity.visitatore.getNome());
                System.out.println("NUOVA 2: " + MainActivity.visitatore.getCognome());
                System.out.println("NUOVA 3: " + MainActivity.visitatore.getEmail());

                MainActivity.visitatore.updateDataDb(view.getContext());

                System.out.println("SHOULD BE UPDATED");

            } catch(ParseException e){
                Snackbar.make(getWindow().getDecorView().getRootView(), "Impossibile procedere",
                        Snackbar.LENGTH_LONG).show();
            }


        }

    }

    private boolean checkForChanges(EditText nomeToCheck, EditText cognomeToCheck, EditText datanToCheck, EditText emailToCheck){

        if( !(nomeToCheck.getText().toString().equals(MainActivity.visitatore.getNome())) ||
                !(cognomeToCheck.getText().toString().equals(MainActivity.visitatore.getCognome())) ||
                !(datanToCheck.getText().toString().equals(MainActivity.visitatore.getShorterDataNascita())) ||
                !(emailToCheck.getText().toString().equals(MainActivity.visitatore.getEmail()))){
            return true;
        }

        Snackbar.make(getWindow().getDecorView().getRootView(), "Nessuna modifica effettuata", Snackbar.LENGTH_LONG).show();
        return false;
    }

    private boolean validateDate(String dateToValid){

        //output is SimpleDateFormat of this type ("dd/MM/yyyy")
        try {
            if(countSlashes(dateToValid)){
                Visitatore.output.parse(dateToValid);
            } else {
                return false;
            }
        } catch (ParseException e){
            System.out.println("DATE NOT VALID");
            return false;
        }
        return true;
    }

    private boolean countSlashes(String s){
        int slashes = 0;
        for(int i = 0; i < s.length()-1; i++) {
            if (s.charAt(i) == '/') {
                slashes++;
            }
        }
        return (slashes == 2);
    }

}
