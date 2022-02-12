package it.uniba.pioneers.testtool.editor.grafo_modifica.node.dialogs;

import android.app.AlertDialog;
import android.content.Context;

import it.uniba.pioneers.testtool.editor.grafo_modifica.node.GraphNode;

public class NodeDialog extends NodeListDialog{
    public static AlertDialog NodeDialog(Context context, GraphNode nodeObject) {
        AlertDialog tmpDialog = getDialog(context, nodeObject);
        return tmpDialog;
    }
}
