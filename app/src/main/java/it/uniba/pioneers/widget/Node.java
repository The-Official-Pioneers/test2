package it.uniba.pioneers.widget;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import it.uniba.pioneers.testtool.R;

public class Node extends ConstraintLayout {

    boolean circle = false;
    Node n;

    private final class MyTouchListener implements OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

                GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(n.getRootView().getContext(), R.drawable.shape_circle).mutate();
                n.findViewById(R.id.vistaProva).setBackground(drawable);
                n.circle = false;

                view.startDragAndDrop(data, shadowBuilder, view, 0);
                view.setVisibility(GONE);
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


    public void init(Context context){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.sample_node, this);
        n = this;




        if(this.circle){

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
        // Defines the one method for the interface, which is called when the View is long-clicked
        this.setOnTouchListener(new MyTouchListener());

        this.setOnDragListener(new MyDragListener());


        TextView tmp = new TextView(context.getApplicationContext());

    }

    public Node(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Node(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public Node(@NonNull Context context) {
        super(context);
        init(context);
    }

    public Node(@NonNull Context context, boolean circle) {
        super(context);
        this.circle = circle;
        init(context);
    }
}
