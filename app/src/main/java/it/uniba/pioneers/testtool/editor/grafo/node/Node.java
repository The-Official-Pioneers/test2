package it.uniba.pioneers.testtool.editor.grafo.node;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.editor.grafo.node.dialogs.NodeDialog;

public class Node extends ConstraintLayout {
    public NodeType type;
    public boolean clicked = false;
    public boolean isDroppable = false;
    public boolean circle = false;

    public JSONObject data;


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
