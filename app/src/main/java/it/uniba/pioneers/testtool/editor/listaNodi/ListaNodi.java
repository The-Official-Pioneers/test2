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
    public ArrayList<ListNode> listNodeArrayList = new ArrayList<>();
    public NodeType listType = null;

    HorizontalScrollView scrollView;
    Button buttonAdd;
    DisplayGrafo displayGrafo = null;

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
        setOnDragListener(new MyDragListener());

        scrollView = (HorizontalScrollView) this.getChildAt(0);
        linearLayout = (LinearLayout) this.scrollView.getChildAt(0);
        buttonAdd = (Button) this.linearLayout.getChildAt(0);

        try {
            displayGrafo = getRootView().findViewById(R.id.displayGrafo);

            /*



            for(int i = 0; i < 10; ++i){
                this.addNode(new ListNode(this.linearLayout.getContext(),
                        this, object, NodeType.OPERA));
            }
*/

            JSONObject object = new JSONObject();

            object.put("visita_id", 3);
            object.put("id", 66);
            object.put("area", 3);
            object.put("descrizione", "Fake descrizione");
            object.put("titolo", "Fake titolo");
            object.put("altezza", 3000);
            object.put("larghezza", 3000);
            object.put("profondita", 3000);

            buttonAdd.setOnClickListener(view1 -> {
                this.addNode(new ListNode(this.linearLayout.getContext(),
                        this, object, NodeType.OPERA));
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNode(ListNode listNode){
        listNodeArrayList.add(listNode);
        linearLayout.addView(listNode);
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
