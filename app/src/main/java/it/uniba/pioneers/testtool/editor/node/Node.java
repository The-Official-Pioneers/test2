package it.uniba.pioneers.testtool.editor.node;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import it.uniba.pioneers.sqlite.DbContract;
import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.editor.node.enums.NodeType;

public class Node extends ConstraintLayout {
    public NodeType type;
    public boolean clicked = false;
    public boolean isDroppable = false;
    public boolean circle = false;
    public TextView label = null;

    public JSONObject data = null;
    public int size = 0;
    public boolean inizializated = false;

    public void setInizializated(boolean flag){
        this.inizializated = flag;
    }

    public Node(@NonNull Context context) {
        super(context);
    }



    public void setColor(int color){
        GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this.getRootView().getContext(),
                R.drawable.shape_circle).mutate();
        drawable.setColor(color);

        this.findViewById(R.id.vistaProva).setBackground(drawable);
    }

    public void setCircle(boolean flag){
        if(flag){
            GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this.getRootView().getContext(),
                    R.drawable.shape_circle).mutate();

            this.findViewById(R.id.vistaProva).setBackground(drawable);
            setColor(Color.YELLOW);
            this.circle = true;
        }else{
            GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this.getRootView().getContext(), R.drawable.shape).mutate();
            this.findViewById(R.id.vistaProva).setBackground(drawable);

            this.circle = false;
        }
    }

}
