package it.uniba.pioneers.testtool.editor.listaNodi;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

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
        buttonAdd.setText("Mostra tutti");
        buttonAdd.setWidth(200);
        buttonAdd.setTextSize(8);
        buttonAdd.setBackgroundColor(Color.DKGRAY);

        hideDuplicate();
        try {
            displayGrafo = getRootView().findViewById(R.id.displayGrafo);

            JSONObject object = new JSONObject();
            hideDuplicate(); //TODO RISOLVERE PROBLEMA

            buttonAdd.setOnClickListener(view1 -> {
                if(buttonAdd.getText().equals("Mostra tutti")){
                    buttonAdd.setText("Nascondi Duplicati");
                    buttonAdd.setBackgroundColor(Color.LTGRAY);
                    buttonAdd.setTextColor(Color.BLACK);

                    for(ListNode listNode : listNodeArrayList){
                        listNode.setCircle(false);
                        listNode.setVisibility(VISIBLE);
                    }
                }else{
                    buttonAdd.setText("Mostra tutti");
                    buttonAdd.setBackgroundColor(Color.DKGRAY);
                    buttonAdd.setTextColor(Color.WHITE);

                    hideDuplicate();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideDuplicate() {
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
    }

    public void addNode(ListNode listNode){
        listNodeArrayList.add(listNode);
        linearLayout.addView(listNode);
        hideDuplicate();
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
