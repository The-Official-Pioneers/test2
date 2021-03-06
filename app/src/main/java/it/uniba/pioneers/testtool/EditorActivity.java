package it.uniba.pioneers.testtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import it.uniba.pioneers.testtool.databinding.ActivityEditor2Binding;
import it.uniba.pioneers.widget.Node;

public class EditorActivity extends AppCompatActivity {
    public ArrayList<Node> opere;
    private ActivityEditor2Binding binding;

    FirstFragment f = new FirstFragment();
    SecondFragment s = new SecondFragment();
    EditorPercorsi e = new EditorPercorsi();

    Integer state = 0;

    FragmentManager supportFragmentManager;
    FragmentContainerView containerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditor2Binding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_editor);

        Button avanti = findViewById(R.id.changeFragment2);
        Button indietro = findViewById(R.id.changeFragment);

        containerView = findViewById(R.id.fragmentContainerView3);
        supportFragmentManager = getSupportFragmentManager();

        supportFragmentManager.beginTransaction()
                .replace(containerView.getId(), f)
                .commit();

        indietro.setOnClickListener(view -> {
            switch (state) {
                case 1:
                    supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.fade_in, R.anim.slade_out, R.anim.fade_in, R.anim.slade_out)
                            .replace(containerView.getId(), f)
                            .commit();
                    state = 0;
                    indietro.setVisibility(View.INVISIBLE);
                    avanti.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.fade_in, R.anim.slade_out, R.anim.fade_in, R.anim.slade_out)
                            .replace(containerView.getId(), s)
                            .commit();
                    state = 1;
                    indietro.setVisibility(View.VISIBLE);
                    avanti.setVisibility(View.VISIBLE);
                    break;
            }


        });

        avanti.setOnClickListener(view2 -> {
            switch (state) {
                case 0:
                    supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.fade_in, R.anim.slade_out, R.anim.fade_in, R.anim.slade_out)
                            .replace(containerView.getId(), s)
                            .commit();
                    state = 1;
                    indietro.setVisibility(View.VISIBLE);
                    avanti.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.fade_in, R.anim.slade_out, R.anim.fade_in, R.anim.slade_out)
                            .replace(containerView.getId(), e)
                            .commit();
                    state = 2;

                    indietro.setVisibility(View.VISIBLE);
                    avanti.setVisibility(View.INVISIBLE);
                    break;

            }


        });
    }
}