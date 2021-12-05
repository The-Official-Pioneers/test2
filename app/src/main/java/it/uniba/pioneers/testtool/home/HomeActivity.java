package it.uniba.pioneers.testtool.home;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import it.uniba.pioneers.testtool.R;
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

    public void onClickDummy(View view) {
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
}