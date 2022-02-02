package it.uniba.pioneers.testtool.editor.grafo.node;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import it.uniba.pioneers.testtool.R;

public class Node extends ConstraintLayout {
    public NodeType type;
    public boolean clicked = false;
    public boolean isDroppable = false;
    public boolean circle = false;

    public JSONObject data;


    public Node(@NonNull Context context) {
        super(context);
    }

    public void setClicked(boolean flag){
        this.clicked = flag;
    }

    public void setCircle(boolean flag){
        if(type == NodeType.OPERA){
            byte[] decodedString = new byte[0];
            try {
                decodedString = Base64.decode(data.getString("foto"), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                BitmapDrawable ob = new BitmapDrawable(getResources(), decodedByte);

                this.findViewById(R.id.vistaProva).setBackground(ob.getCurrent());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            if(flag){
                GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this.getRootView().getContext(), R.drawable.shape_circle).mutate();
                this.findViewById(R.id.vistaProva).setBackground(drawable);
                //QUI ANDRà settata l'immagine
                this.circle = true;
            }else{
                GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this.getRootView().getContext(), R.drawable.shape).mutate();
                this.findViewById(R.id.vistaProva).setBackground(drawable);

                this.circle = false;
            }
        }
    }
}
