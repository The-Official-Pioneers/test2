package it.uniba.pioneers.testtool;

import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

import it.uniba.pioneers.testtool.databinding.ActivityEditor2Binding;
import it.uniba.pioneers.widget.Node;

public class EditorActivity2 extends AppCompatActivity {

    private ActivityEditor2Binding binding;
    private ArrayList<Node> opere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     binding = ActivityEditor2Binding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());



     ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle("My new title"); // set the top title

        String title = actionBar.getTitle().toString(); // get the title

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                new AlertDialog.Builder(this)
                        .setTitle("OPERE")
                        .setMessage("Seleziona le opere che vuoi visualizzare")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation

                                /*FragmentContainerView f1 = binding.getRoot().findViewById(R.id.fragment1);

                                LinearLayout l = f1.findViewById(R.id.layoutEditor);
                                Node tmp = new Node(l.getContext());

                                l.addView(tmp);
*/
                            }
                        })
                        .setView(R.layout.dialog_layout)

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                break;
            case R.id.action_settings2:
                Toast.makeText(this, "Clicked Menu 2", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}