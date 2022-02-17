package it.uniba.pioneers.testtool.editor.listaNodi;

import android.content.Context;
import android.graphics.Color;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.editor.node.GraphNodeModifica;
import it.uniba.pioneers.testtool.editor.node.ListNodeModifica;
import it.uniba.pioneers.testtool.editor.grafo_modifica.DisplayGrafoModifica;
import it.uniba.pioneers.testtool.editor.node.enums.NodeType;

@SuppressWarnings("ALL")
public class ListaNodi extends HorizontalScrollView {
    private final GraphNodeModifica parentNode;
    public LinearLayout linearLayout;
    public ArrayList<ListNodeModifica> listNodeArrayListModifica = new ArrayList<>();
    public NodeType listType = null;

    HorizontalScrollView scrollView;
    Button buttonAdd;
    DisplayGrafoModifica displayGrafoModifica = null;

    public void init(){
        LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
        layoutInflater.inflate(R.layout.layout_lista_nodi, this);

        scrollView = (HorizontalScrollView) this.getChildAt(0);
        linearLayout = (LinearLayout) this.scrollView.getChildAt(0);
        buttonAdd = (Button) this.linearLayout.getChildAt(0);
        buttonAdd.setText(R.string.mostra_tutti_editor);
        buttonAdd.setWidth(200);
        buttonAdd.setTextSize(8);
        buttonAdd.setBackgroundColor(Color.DKGRAY);

        setOnDragListener((view, dragEvent) -> {
            switch (dragEvent.getAction()){
                case DragEvent.ACTION_DROP:
                    view.setVisibility(VISIBLE);
            }
            return true;
        });

        hideDuplicate();
        try {
            displayGrafoModifica = getRootView().findViewById(R.id.displayGrafoModifica);

            JSONObject object = new JSONObject();
            hideDuplicate(); //TODO RISOLVERE PROBLEMA

            buttonAdd.setOnClickListener(view1 -> {
                if(buttonAdd.getText().equals(R.string.mostra_tutti_editor)){
                    buttonAdd.setText(R.string.nascondi_duplicati_editor);
                    buttonAdd.setBackgroundColor(Color.LTGRAY);
                    buttonAdd.setTextColor(Color.BLACK);

                    for(ListNodeModifica listNodeModifica : listNodeArrayListModifica){
                        listNodeModifica.setCircle(false);
                        listNodeModifica.setVisibility(VISIBLE);
                    }
                }else{
                    buttonAdd.setText(R.string.mostra_tutti_editor);
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
        for(GraphNodeModifica node : parentNode.graphParent.graph.successors(parentNode)){
            for(ListNodeModifica listNodeModifica : listNodeArrayListModifica){
                try {
                    if(node.data.getInt("id") == listNodeModifica.data.getInt("id")){
                        listNodeModifica.setVisibility(GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addNode(ListNodeModifica listNodeModifica){
        listNodeArrayListModifica.add(listNodeModifica);
        linearLayout.addView(listNodeModifica);
        hideDuplicate();
    }

    public void removeNode(int id){
        for(int i = 0; i < this.listNodeArrayListModifica.size(); ++i){
            ListNodeModifica tmp = this.listNodeArrayListModifica.get(i);
            if(tmp.getId() == id){
                tmp.setVisibility(GONE);
                this.listNodeArrayListModifica.remove(i);
                break;
            }
        }
    }

    public ListaNodi(@NonNull Context context, NodeType listType, GraphNodeModifica parentNode) {
        super(context);
        this.listType = listType;
        this.parentNode = parentNode;
        init();
    }
}
