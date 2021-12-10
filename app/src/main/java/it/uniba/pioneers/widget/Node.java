package it.uniba.pioneers.widget;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.common.graph.MutableGraph;
import com.google.common.graph.MutableValueGraph;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.uniba.pioneers.testtool.EditorActivity;
import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.widget.grafo.DrawView;
import it.uniba.pioneers.widget.grafo.Line;

public class Node extends ConstraintLayout{

    static public final int VISITA = 0;
    static public final int MUSEO = 1;
    static public final int AREA = 2;
    static public final int OPERA = 3;

    public NodeType tipo;

    Node n;
    boolean circle = false;
    LinearLayout linearLayout;

    JSONObject data;

    ArrayList<Line> successorLine = new ArrayList<>();

    final String ARG_DESCRIZIONE = "descrizione";
    final String ARG_ANNO = "anno";

    MutableGraph<Node> graph = null;

    public boolean clicked = false;
    public boolean inizializated = false;

    public Node clone(Context context) {
        return new Node(context, this.linearLayout, this.data);
    }

    private final class MyTouchListener implements OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

                GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(n.getRootView().getContext(), R.drawable.shape_circle).mutate();
                n.findViewById(R.id.vistaProva).setBackground(drawable);
                n.circle = false;

                view.startDragAndDrop(data, shadowBuilder, view, 0);
                //view.setVisibility(GONE);
                return true;
            } else {

                Toast.makeText(n.getRootView().getContext(), "DINE", Toast.LENGTH_LONG).show();

                return false;
            }
        }
    }

    class MyDragListener implements OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            return true;
        }
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



    public void init(Context context) throws JSONException {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.sample_node, this);
        n = this;

        data = new JSONObject();
        data.put(ARG_DESCRIZIONE, "Ciao mi chiamo antonionoonno");
        data.put(ARG_ANNO, "1500");

        setCircle(this.circle);
        // Defines the one method for the interface, which is called when the View is long-clicked
        //this.setOnTouchListener(new MyTouchListener());

        //this.setOnDragListener(new MyDragListener());

        TextView tmp = new TextView(context.getApplicationContext());

    }

    public void setInizializated(boolean flag){
        this.inizializated = flag;
    }

    public void setClicked(boolean flag){
        this.clicked = flag;
    }

    public Node(Context context, NodeType nodeType) {
        super(context);
        try {
            init(context);
            this.tipo = nodeType;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Node(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        try {
            init(context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Node(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        try {
            init(context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Node(@NonNull Context context) {
        super(context);
        try {
            init(context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addSuccessor(Node nodeEnd){
        this.graph.addNode(nodeEnd);
        this.graph.putEdge(this, nodeEnd);
    }

    public Node(@NonNull Context context, MutableGraph<Node> graph, NodeType tipo) {
        super(context);
        try {
            this.tipo = tipo;
            this.graph = graph;
            init(context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public Node(@NonNull Context context, boolean circle) {
        super(context);
        this.circle = circle;
        try {
            init(context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Node(@NonNull Context context, LinearLayout ln,  JSONObject data) {
        super(context);
        try {
            this.linearLayout = ln;
            this.circle = true;
            init(context);

            this.data.put(ARG_DESCRIZIONE, data.getString("descrizione"));
            this.data.put(ARG_ANNO, data.getString("anno"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Node(@NonNull Context context, LinearLayout ln) {
        super(context);
        try {
            this.linearLayout = ln;
            init(context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
