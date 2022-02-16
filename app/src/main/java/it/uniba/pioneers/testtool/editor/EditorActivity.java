package it.uniba.pioneers.testtool.editor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import it.uniba.pioneers.data.users.Guida;
import it.uniba.pioneers.sqlite.DbContract;
import it.uniba.pioneers.sqlite.DbHelper;
import it.uniba.pioneers.testtool.DialogNodeInfo;
import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.databinding.ActivityEditor2Binding;
import it.uniba.pioneers.testtool.editor.grafo_visualizza.GrafoVisualizzaFragment;
import it.uniba.pioneers.testtool.editor.node.ListNodeModifica;
import it.uniba.pioneers.testtool.editor.grafo_modifica.GrafoModificaFragment;

public class EditorActivity extends AppCompatActivity {
    public ArrayList<ListNodeModifica> opere;
    private ActivityEditor2Binding binding;

    public DialogNodeInfo d = new DialogNodeInfo();

    Integer state = 0;

    FragmentManager supportFragmentManager;
    FragmentContainerView containerView;


    private void init(){
        Button avanti = findViewById(R.id.changeFragment2);
        Button indietro = findViewById(R.id.changeFragment);

        Button test = findViewById(R.id.tst_info);

        indietro.setOnClickListener(view -> {
        });

        avanti.setOnClickListener(view2 -> {
            switch (state) {
                case 0:

                case 1:

            }
        });

        test.setOnClickListener(view3 ->{
            Guida g = new Guida();
            g.setId(1);
            g.setNome("Rino");
            g.setCognome("Pino");
            g.setDataNascita(1639937888767L);
            g.setEmail("pino@rino.cock");
            g.setPassword("dadasdsadasdsasadsad");
            g.setPassword("museo");
            g.setOnline(false);

            g.readDataDb(view3.getRootView().getContext());
            Toast.makeText(this, g.getEmail(), Toast.LENGTH_LONG).show();
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditor2Binding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_editor);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();

        DbHelper dbHelper = new DbHelper(this);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.AreaEntry.COLUMN_NOME, "GINO");
        values.put(DbContract.AreaEntry.COLUMN_ZONA, 1);

        long newRowId = db.insert(DbContract.AreaEntry.TABLE_NAME, null, values);


    }
}