package it.uniba.pioneers.testtool.editor.node;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.editor.listaNodi.ListaNodi;
import it.uniba.pioneers.testtool.editor.node.enums.NodeType;

public class ListNodeModifica extends Node{
    LinearLayout linearLayout;
    public ListNodeModifica self = null;

    ListaNodi listaNodi = null;

    private final class MyTouchListener implements OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Toast.makeText(view.getContext(), String.valueOf(motionEvent.getAction()), Toast.LENGTH_LONG).show();

            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_UP:
                    view.setVisibility(VISIBLE);
                    break;
                case MotionEvent.ACTION_DOWN:
                    ClipData data = ClipData.newPlainText("", "");
                    DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

                    GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(self.getRootView().getContext(), R.drawable.shape_circle).mutate();
                    self.findViewById(R.id.vistaProva).setBackground(drawable);
                    drawable.setColor(Color.YELLOW);

                    view.startDragAndDrop(data, shadowBuilder, view, 0);

                    view.setVisibility(INVISIBLE);
                    break;

            }
            return true;
        }
    }

    public ListNodeModifica(@NonNull Context context, ListaNodi listaNodi, JSONObject data, NodeType type) {
        super(context);
        try {
            System.out.println(data.toString());

            this.data = data;
            this.listaNodi = listaNodi;
            this.type = type;
            initDroppableNode(context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        clicked = false;
        isDroppable = false;
        setOnTouchListener(null);
        setCircle(false);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initDroppableNode(@NonNull Context context) throws JSONException {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.sample_node, this);
        linearLayout = listaNodi.linearLayout;
        self = this;
        clicked = false;
        setOnClickListener(view -> {
            for(ListNodeModifica node : listaNodi.listNodeArrayListModifica){
                if(!view.equals(node)){
                    node.reset();
                }else{
                    self.clicked = true;
                    self.isDroppable = true;
                    self.setOnTouchListener(new MyTouchListener());
                    self.setCircle(true);
                }
            }
        });
    }


}
