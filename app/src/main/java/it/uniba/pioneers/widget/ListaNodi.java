package it.uniba.pioneers.widget;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import java.util.ArrayList;

import it.uniba.pioneers.testtool.R;

@SuppressWarnings("ALL")
public class ListaNodi extends ScrollView {
    LinearLayout linearLayout;
    ScrollView scrollView;
    //MutableValueGraph<Node, Integer> nodeMutableValueGraph = ValueGraphBuilder.directed().build();


    ArrayList<Node> nodeArrayList = new ArrayList<>();
    class MyDragListener implements OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
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
                        Toast.makeText(tmp.getRootView().getContext(), "ciaoooo", Toast.LENGTH_LONG).show();

                        tmp.circle = true;
                    }else{
                        GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(tmp.getRootView().getContext(), R.drawable.shape).mutate();
                        tmp.findViewById(R.id.vistaProva).setBackground(drawable);
                        Toast.makeText(tmp.getRootView().getContext(), "ciaoooo", Toast.LENGTH_LONG).show();

                        tmp.circle = false;
                    }

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            return true;
        }
    }
    public void init(){
        LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
        layoutInflater.inflate(R.layout.layout_lista_nodi, this);
        this.setOnDragListener(new MyDragListener());

        this.scrollView = (ScrollView) this.getChildAt(0);
        this.linearLayout = (LinearLayout) this.scrollView.getChildAt(0);
        for(int i = 0; i < 10; ++i){
            Node tmp = new Node(this.getContext());
            this.addNode(tmp);
        }


        Toast.makeText(this.getContext(), this.linearLayout.toString(), Toast.LENGTH_LONG).show();
    }

    public void addNode(Node node){
        this.linearLayout.addView(node);
        nodeArrayList.add(node);
    }

    public void removeNode(int id){
        for(int i = 0; i < this.nodeArrayList.size(); ++i){
            Node tmp = this.nodeArrayList.get(i);
            if(tmp.getId() == id){
                tmp.setVisibility(GONE);
                this.nodeArrayList.remove(i);
                break;
            }
        }
    }

    public ListaNodi(@NonNull Context context) {
        super(context);
        init();
    }

    public ListaNodi(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListaNodi(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ListaNodi(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
}
