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
    //@Override
   /* public void onBackPressed() {
        this.finishAffinity();
    }*/


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }


    public void onClickButton(View view) {

        FragmentManager fragmentManager = getFragmentManager();
        /*** INIZIO TRANSAZIONE ***/
        LoginFragment f = new LoginFragment();
        androidx.fragment.app.FragmentManager supportFragmentManager;
        supportFragmentManager = getSupportFragmentManager();
        //setCustomAnimations (int enter, int exit, int popEnter int popExit) => primo parametro è l'animazione del fragment che sto attaccando
        //                                                                    => secondo parametro è l'animazione di quando stacco un fragment dal backstack
        //                                                                    => terzo parametro bho
        //                                                                    => quarto parametro è l'animazione di quando faccio un pop dal backsteck ma senza rimuovere il fragment'
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                .replace(R.id.fragmentContainerView4, f).addToBackStack(null)
                .commit();
        /*** FINE TRANSAZIONE ***/
    }

    public void goMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

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
    public void registerButton(View v){
        RegisterFragment fragment = (RegisterFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView4);
        fragment.controllData(v);
    }

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

    private void launchIntentForVisitatore(View view) {
        Toast.makeText(view.getContext(), R.string.bentornato_login + " Andrea", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.putExtra("typeUser", "visitatore");
        intent.putExtra("idUser", 2);
        view.getContext().startActivity(intent);
    }

    private void launchIntentForCuratore(View view) {
        Toast.makeText(view.getContext(), R.string.bentornato_login + " Luca", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.putExtra("typeUser", "curatore");
        intent.putExtra("idUser", 1);
        view.getContext().startActivity(intent);

    }

    private void launchIntentForGuida(View view) {
        Toast.makeText(view.getContext(), R.string.bentornato_login + " Antonio", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.putExtra("typeUser", "guida");
        intent.putExtra("idUser", 1001);
        view.getContext().startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}