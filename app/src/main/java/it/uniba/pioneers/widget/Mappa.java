package it.uniba.pioneers.widget;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import it.uniba.pioneers.testtool.R;

public class Mappa extends ConstraintLayout {
    MutableValueGraph<Node, Integer> nodeMutableValueGraph = ValueGraphBuilder.directed().build();


    private void init(){
        LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
        layoutInflater.inflate(R.layout.layout_mappa, this);

        this.setOnDragListener((view, event) -> {
            int action = event.getAction();

            Node tmp = (Node)event.getLocalState();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    if(!tmp.circle){

                        GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(tmp.getRootView().getContext(), R.drawable.shape_circle).mutate();
                        tmp.findViewById(R.id.vistaProva).setBackground(drawable);
                        Toast.makeText(tmp.getRootView().getContext(), String.valueOf(event.getX()), Toast.LENGTH_LONG).show();

                        tmp.circle = true;
                    }else{
                        GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(tmp.getRootView().getContext(), R.drawable.shape).mutate();
                        tmp.findViewById(R.id.vistaProva).setBackground(drawable);
                        Toast.makeText(tmp.getRootView().getContext(), "STO DROPPANDO", Toast.LENGTH_LONG).show();

                        tmp.circle = false;
                    }

                    this.nodeMutableValueGraph.addNode(tmp);

                    Node node = new Node(this.getContext(), true);
                    this.addView(node);

                    node.setX(event.getX());
                    node.setY(event.getY());
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            return true;
        });
    }

    public Mappa(@NonNull Context context) {
        super(context);
        init();
    }

    public Mappa(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Mappa(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Mappa(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
}
