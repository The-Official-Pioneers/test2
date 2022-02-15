package it.uniba.pioneers.testtool.home;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

import it.uniba.pioneers.testtool.MainActivity;
import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.home.ui.RegisterFragment;
import it.uniba.pioneers.testtool.home.ui.login.LoginFragment;

public class HomeActivity extends AppCompatActivity {

    /*****************************************************************************
     * In onCrate, viene creata l'activity; in tale metodo viene aggiunto in modo
     * programmatico il fragment relativo al welcome con una transazione
     * @param savedInstanceState oggetto per gestire il ripristino dello stato
     ****************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FragmentManager fragmentManager = getFragmentManager();
        /*** INIZIO TRANSAZIONE ***/
        WelcomeFragment f = new WelcomeFragment();
        androidx.fragment.app.FragmentManager supportFragmentManager;
        supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView4, f).addToBackStack(null)
                .commit();
        /*** FINE TRANSAZIONE ***/
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /*********************************************************************************
     * Metodo associato al button di inizio; con tale metodo viene aggiunto in modo
     * programmatico il fragment relativo al login con una transazione
     * @param view view associata
     *********************************************************************************/
    public void onClickButton(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        /*** INIZIO TRANSAZIONE ***/
        LoginFragment f = new LoginFragment();
        androidx.fragment.app.FragmentManager supportFragmentManager;
        supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                .replace(R.id.fragmentContainerView4, f).addToBackStack(null)
                .commit();
        /*** FINE TRANSAZIONE ***/
    }

    /*********************************************************************************
     * Metodo associato al button di registrazione; con tale metodo viene aggiunto in
     * modo programmatico il fragment relativo alla registrazione con una transazione
     * @param view view associata
     *********************************************************************************/
    public void AttachRegisterFragment(View view) {
        /*** INIZIO TRANSAZIONE ***/
        RegisterFragment f = new RegisterFragment();
        androidx.fragment.app.FragmentManager supportFragmentManager;
        supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                .replace(R.id.fragmentContainerView4, f, "tag")
                .addToBackStack(null)
                .commit();
        /*** FINE TRANSAZIONE ***/

    }

    /*********************************************************************************
     * Metodo che ci consente di impostare l'attore che è stato scelto dall'utente
     * durante la fase di registrazione. In questo modo riusciamo a caricare il form
     * di registrazione corretto in base all'attore
     * @param view view associata
     *********************************************************************************/
    public void clickedButton(View view){
        RegisterFragment fragment = (RegisterFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView4);
        switch (view.getId()){
            case R.id.curatore:
                /******************************************************
                 * E' stato selezionato il curatore
                 ******************************************************/
                fragment.callBackHandler(view, "curatore");
            break;
            case R.id.visitatore:
                /******************************************************
                 * E' stato selezionato il visitatore
                 ******************************************************/
                fragment.callBackHandler(view, "visitatore");
            break;
            case R.id.guida:
                /******************************************************
                 * E' stata selezionata la guida
                 ******************************************************/
                fragment.callBackHandler(view, "guida");
            break;
        }
    }

    /*********************************************************************************
     * Metodo che ci consente di impostare l'immagine di profilo, invocando un metodo
     * definito nel fragment di registrazione
     * @param view view associata
     *********************************************************************************/
    public void addImage(View view){
        RegisterFragment fragment = (RegisterFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView4);
        fragment.addImageToForm(view);
    }

    /*********************************************************************************
     * Metodo che ci restituisce un inputStream al chiamante in base all'uri passato
     * come parametro
     * @param imageUri imageUri associato
     *********************************************************************************/
    public InputStream getContentResolverAct(Uri imageUri) throws Exception{
        return getContentResolver().openInputStream(imageUri);
    }

    /*********************************************************************************
     * Metodo che ci consente di impostare registrare l'attore nel db, invocando un
     * metodo definito nel fragment di registrazione
     * @param v view associata
     *********************************************************************************/
    public void registerButton(View v){
        RegisterFragment fragment = (RegisterFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView4);
        fragment.controllData(v);
    }

    /*********************************************************************************
     * Metodo che ci consente di accedere con un account di default senza dover
     * effettuare una registrazione o passare dal login
     * @param view view associata
     *********************************************************************************/
    public void alphaVersionTest(View view){
        switch(view.getId()){
            case R.id.curatoreDefault:
                launchIntentForCuratore(view);
            break;
            case R.id.visitatoreDefault:
                launchIntentForVisitatore(view);
            break;
            case R.id.guidaDefault:
                launchIntentForGuida(view);
            break;
        }
    }

    /*********************************************************************************
     * Metodo che ci consente di accedere con un account di default di un visitatore
     * @param view view associata
     *********************************************************************************/
    private void launchIntentForVisitatore(View view) {
        Toast.makeText(view.getContext(), R.string.bentornato_login + " Andrea", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.putExtra("typeUser", "visitatore");
        intent.putExtra("idUser", 2);
        view.getContext().startActivity(intent);
    }

    /*********************************************************************************
     * Metodo che ci consente di accedere con un account di default di un curatore
     * @param view view associata
     *********************************************************************************/
    private void launchIntentForCuratore(View view) {
        Toast.makeText(view.getContext(), R.string.bentornato_login + " Luca", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.putExtra("typeUser", "curatore");
        intent.putExtra("idUser", 1);
        view.getContext().startActivity(intent);

    }

    /*********************************************************************************
     * Metodo che ci consente di accedere con un account di default di un guida
     * @param view view associata
     *********************************************************************************/
    private void launchIntentForGuida(View view) {
        Toast.makeText(view.getContext(), R.string.bentornato_login + " Antonio", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.putExtra("typeUser", "guida");
        intent.putExtra("idUser", 1004);
        view.getContext().startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}