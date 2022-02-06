package it.uniba.pioneers.testtool.editor.listaNodi;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.editor.grafo.node.ListNode;
import it.uniba.pioneers.testtool.editor.grafo.DisplayGrafo;
import it.uniba.pioneers.testtool.editor.grafo.node.NodeType;

@SuppressWarnings("ALL")
public class ListaNodi extends HorizontalScrollView {
    public LinearLayout linearLayout;
    HorizontalScrollView scrollView;
    Button buttonAdd;

    public ArrayList<ListNode> listNodeArrayList = new ArrayList<>();

    DisplayGrafo displayGrafo = null;
    public NodeType listType = null;

    class MyDragListener implements OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();

            ListNode tmp = (ListNode)event.getLocalState();
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

        this.scrollView = (HorizontalScrollView) this.getChildAt(0);
        this.linearLayout = (LinearLayout) this.scrollView.getChildAt(0);
        this.buttonAdd = (Button) this.linearLayout.getChildAt(0);


        try {
            displayGrafo = getRootView().findViewById(R.id.displayGrafo);

            JSONObject object = new JSONObject();

            object.put("visita_id", 3);
            object.put("area", 3);
            object.put("descrizione", "Fake descrizione");
            object.put("titolo", "Fake titolo");
            object.put("altezza", 3000);
            object.put("larghezza", 3000);
            object.put("profondita", 3000);


            for(int i = 0; i < 10; ++i){
                this.addNode(new ListNode(this.linearLayout.getContext(), this, object, NodeType.OPERA));
            }

            this.buttonAdd.setOnClickListener(view1 -> {
                this.linearLayout.addView(new ListNode(this.linearLayout.getContext(), this, object, NodeType.OPERA));

            });
            Toast.makeText(this.getContext(), this.linearLayout.toString(), Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void addNode(ListNode listNode){
        this.linearLayout.addView(listNode);
        listNodeArrayList.add(listNode);
    }

    public void removeNode(int id){
        for(int i = 0; i < this.listNodeArrayList.size(); ++i){
            ListNode tmp = this.listNodeArrayList.get(i);
            if(tmp.getId() == id){
                tmp.setVisibility(GONE);
                this.listNodeArrayList.remove(i);
                break;
            }
        }
    }

    public ListaNodi(@NonNull Context context, NodeType listType) {
        super(context);
        init();
    }


}
