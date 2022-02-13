package it.uniba.pioneers.testtool.editor.node.dialogs;

import android.app.AlertDialog;
import android.content.Context;

import it.uniba.pioneers.testtool.editor.node.GraphNode;
import it.uniba.pioneers.testtool.editor.node.GraphNodeVisualizza;

public class NodeDialog extends NodeListDialog{
    public static AlertDialog NodeDialog(Context context, GraphNode nodeObject) {
        AlertDialog tmpDialog = getDialog(context, nodeObject);
        return tmpDialog;
    }

    public static AlertDialog NodeDialog(Context context, GraphNodeVisualizza nodeObject) {
        AlertDialog tmpDialog = getDialog(context, nodeObject);
        return tmpDialog;
    }
}
