package it.uniba.pioneers.testtool.editor.options;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.editor.grafo.DisplayGrafo;

public class OptionsEditor extends LinearLayout {

    Button listaOpere = null;
    Button salvaPercorso = null;
    Button condividiPercorso = null;
    Button eliminaPercorso = null;

    public void initButtonsListener(){

    }

    public void init(Context context){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.widget_options_grafo, this);


        initButtonsListener();

    }

    public OptionsEditor(Context context) {
        super(context);
        init(context);
    }

    public OptionsEditor(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OptionsEditor(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
}
