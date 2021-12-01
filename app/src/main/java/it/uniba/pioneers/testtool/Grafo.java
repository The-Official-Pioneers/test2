package it.uniba.pioneers.testtool;

import android.content.Context;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import it.uniba.pioneers.widget.Node;

public class Grafo implements Serializable {
    MutableValueGraph<Node, Integer> grafo;

    public Grafo(){
        this.grafo = ValueGraphBuilder.undirected().build();
    }



}


