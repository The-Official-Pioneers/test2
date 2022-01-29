package it.uniba.pioneers.testtool.home;

import static android.view.View.GONE;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import org.checkerframework.common.returnsreceiver.qual.This;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;

import it.uniba.pioneers.testtool.MainActivity;
import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.home.ui.RegisterFragment;
import it.uniba.pioneers.testtool.home.ui.login.LoginFragment;

public class HomeActivity extends AppCompatActivity {

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

    public void onClickButton(View view) {
        Toast.makeText(this, "Ha funzionato", Toast.LENGTH_SHORT).show();

        FragmentManager fragmentManager = getFragmentManager();
        /*** INIZIO TRANSAZIONE ***/
        LoginFragment f = new LoginFragment();
        androidx.fragment.app.FragmentManager supportFragmentManager;
        supportFragmentManager = getSupportFragmentManager();
        //setCustomAnimations (int enter, int exit, int popEnter int popExit) => primo parametro è l'animazione del fragment che sto attaccando
        //                                                                    => secondo parametro è l'animazione di quando stacco un fragment dal backstack
        //                                                                    => terzo parametro bho
        //                                                                    => quarto parametro è l'animazione di quando faccio un pop dal backsteck ma senza rimuovere il fragment'
        supportFragmentManager.beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, 0, 0,
                android.R.anim.slide_out_right)
                .replace(R.id.fragmentContainerView4, f).addToBackStack(null)
                .commit();
        /*** FINE TRANSAZIONE ***/
    }

    public void goMainActivity(View view) {
        Toast.makeText(this, "aweo", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void AttachRegisterFragment(View view) {
        Toast.makeText(this, "aweo", Toast.LENGTH_SHORT).show();

        /*** INIZIO TRANSAZIONE ***/
        RegisterFragment f = new RegisterFragment();
        androidx.fragment.app.FragmentManager supportFragmentManager;
        supportFragmentManager = getSupportFragmentManager();
        //setCustomAnimations (int enter, int exit, int popEnter int popExit) => primo parametro è l'animazione del fragment che sto attaccando
        //                                                                    => secondo parametro è l'animazione di quando stacco un fragment dal backstack
        //                                                                    => terzo parametro bho
        //                                                                    => quarto parametro è l'animazione di quando faccio un pop dal backsteck ma senza rimuovere il fragment'
        supportFragmentManager.beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, 0, 0,
                android.R.anim.slide_out_right)
                .replace(R.id.fragmentContainerView4, f, "tag")
                .addToBackStack(null)
                .commit();
        /*** FINE TRANSAZIONE ***/

    }
    public void clickedButton(View view){
        RegisterFragment fragment = (RegisterFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView4);
        switch (view.getId()){
            case R.id.curatore:
                fragment.callBackHandler(view, "curatore");
            break;
            case R.id.visitatore:
                fragment.callBackHandler(view, "visitatore");
            break;
            case R.id.guida:
                fragment.callBackHandler(view, "guida");
            break;
        }
    }

    public void addImage(View v){
        RegisterFragment fragment = (RegisterFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView4);
        fragment.addImageToForm(v);
    }

    public InputStream getContentResolverAct(Uri imageUri) throws Exception{
        return getContentResolver().openInputStream(imageUri);
    }
    public void registerCuratore(View v){
        RegisterFragment fragment = (RegisterFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView4);
        fragment.registerCuratoreFragment(v);
    }

}