package it.uniba.pioneers.testtool.editor.grafo.node;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import org.json.JSONObject;

import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.widget.NodeType;

public class Node extends ConstraintLayout {
    public NodeType type;
    public boolean clicked = false;
    public boolean isDroppable = false;
    public boolean circle = false;

    JSONObject data;


    public Node(@NonNull Context context) {
        super(context);
    }

    public void setClicked(boolean flag){
        this.clicked = flag;
    }

    public void setCircle(boolean flag){
        if(flag){
            GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this.getRootView().getContext(), R.drawable.shape_circle).mutate();
            this.findViewById(R.id.vistaProva).setBackground(drawable);
            Toast.makeText(this.getRootView().getContext(), "ciaoooo", Toast.LENGTH_LONG).show();

            this.circle = true;
        }else{
            GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this.getRootView().getContext(), R.drawable.shape).mutate();
            this.findViewById(R.id.vistaProva).setBackground(drawable);
            Toast.makeText(this.getRootView().getContext(), "ciaoooo", Toast.LENGTH_LONG).show();

            this.circle = false;
        }
    }




}
