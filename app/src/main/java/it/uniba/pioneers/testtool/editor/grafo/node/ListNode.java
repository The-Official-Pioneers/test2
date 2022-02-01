package it.uniba.pioneers.testtool.editor.grafo.node;

import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import it.uniba.pioneers.testtool.R;

public class ListNode extends Node{
    LinearLayout linearLayout;
    public ListNode self = null;

    final String ARG_DESCRIZIONE = "descrizione";
    final String ARG_ANNO = "anno";

    public ListNode clone(Context context) {
        return new ListNode(context, this.linearLayout, this.data);
    }

    private final class MyTouchListener implements OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

                GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(self.getRootView().getContext(), R.drawable.shape_circle).mutate();
                self.findViewById(R.id.vistaProva).setBackground(drawable);
                self.circle = false;

                view.startDragAndDrop(data, shadowBuilder, view, 0);
                //view.setVisibility(GONE);
                return true;
            } else {
                return false;
            }
        }
    }


    public void init(Context context) throws JSONException {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.sample_node, this);
        self = this;

        setCircle(this.circle);
        // Defines the one method for the interface, which is called when the View is long-clicked
        this.setOnTouchListener(new MyTouchListener());


    }



    public ListNode(@NonNull Context context) {
        super(context);
        try {
            init(context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public ListNode(@NonNull Context context, LinearLayout ln, JSONObject data) {
        super(context);
        try {
            initDroppableNode(context, ln);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initDroppableNode(@NonNull Context context, LinearLayout ln) throws JSONException {
        JSONObject data;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.sample_node, this);
        this.linearLayout = ln;
        this.circle = true;

        self = this;

        data = new JSONObject();

        setCircle(this.circle);
        // Defines the one method for the interface, which is called when the View is long-clicked
        this.setOnTouchListener(new MyTouchListener());

        this.isDroppable = true;
    }
}
