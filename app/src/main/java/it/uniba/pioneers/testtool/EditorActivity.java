package it.uniba.pioneers.testtool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

import it.uniba.pioneers.testtool.databinding.ActivityEditor2Binding;
import it.uniba.pioneers.widget.Node;

public class EditorActivity extends AppCompatActivity {
    public ArrayList<Node> opere;
    private ActivityEditor2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditor2Binding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_editor);

    }
}