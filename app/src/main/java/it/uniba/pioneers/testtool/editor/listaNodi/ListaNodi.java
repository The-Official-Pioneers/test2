package it.uniba.pioneers.testtool.editor.listaNodi;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.icu.util.MeasureUnit;
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
import it.uniba.pioneers.testtool.editor.grafo.node.GraphNode;
import it.uniba.pioneers.testtool.editor.grafo.node.ListNode;
import it.uniba.pioneers.testtool.editor.grafo.DisplayGrafo;
import it.uniba.pioneers.testtool.editor.grafo.node.NodeType;

@SuppressWarnings("ALL")
public class ListaNodi extends HorizontalScrollView {
    private final GraphNode parentNode;
    public LinearLayout linearLayout;
    public ArrayList<ListNode> listNodeArrayList = new ArrayList<>();
    public NodeType listType = null;

    HorizontalScrollView scrollView;
    Button buttonAdd;
    DisplayGrafo displayGrafo = null;

    public void init(){
        LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
        layoutInflater.inflate(R.layout.layout_lista_nodi, this);

        scrollView = (HorizontalScrollView) this.getChildAt(0);
        linearLayout = (LinearLayout) this.scrollView.getChildAt(0);
        buttonAdd = (Button) this.linearLayout.getChildAt(0);
        buttonAdd.setText("Nascondi Duplicati");
        buttonAdd.setWidth(200);
        buttonAdd.setTextSize(8);
        buttonAdd.setBackgroundColor(Color.DKGRAY);

        try {
            displayGrafo = getRootView().findViewById(R.id.displayGrafo);

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
                if(buttonAdd.getText().equals("Nascondi Duplicati")){
                    buttonAdd.setText("Mostra Tutti");
                    buttonAdd.setBackgroundColor(Color.LTGRAY);
                    buttonAdd.setTextColor(Color.BLACK);

                    for(GraphNode node : parentNode.graphParent.graph.successors(parentNode)){
                            for(ListNode listNode : listNodeArrayList){
                                try {
                                    if(node.data.getInt("id") == listNode.data.getInt("id")){
                                        listNode.setVisibility(GONE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                    }

                }else{
                    buttonAdd.setText("Nascondi Duplicati");
                    buttonAdd.setBackgroundColor(Color.DKGRAY);
                    buttonAdd.setTextColor(Color.WHITE);

                    for(ListNode listNode : listNodeArrayList){
                        listNode.setCircle(false);
                        listNode.setVisibility(VISIBLE);
                    }
                }
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

    public ListaNodi(@NonNull Context context, NodeType listType, GraphNode parentNode) {
        super(context);
        this.listType = listType;
        this.parentNode = parentNode;
        init();
    }
}
