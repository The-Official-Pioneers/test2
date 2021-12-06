package it.uniba.pioneers.widget.grafo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import it.uniba.pioneers.testtool.R;

public class OptionsGrafo extends LinearLayout {

    Button listaOpere = null;
    Button salvaPercorso = null;
    Button condividiPercorso = null;
    Button eliminaPercorso = null;

    public void initButtonsListener(){
        listaOpere.setOnClickListener(view -> {

        });

        salvaPercorso.setOnClickListener(view -> {

        });

        condividiPercorso.setOnClickListener(view -> {

        });

        eliminaPercorso.setOnClickListener(view -> {

        });
    }

    public void init(Context context){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.widget_options_grafo, this);

        listaOpere = this.findViewById(R.id.listaOpere);
        salvaPercorso = this.findViewById(R.id.salvaPercorso);
        condividiPercorso = this.findViewById(R.id.condividiPercorso);
        eliminaPercorso = this.findViewById(R.id.eliminaPercorso);

    }

    public OptionsGrafo(Context context) {
        super(context);
        init(context);
    }

    public OptionsGrafo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OptionsGrafo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public OptionsGrafo(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
}
