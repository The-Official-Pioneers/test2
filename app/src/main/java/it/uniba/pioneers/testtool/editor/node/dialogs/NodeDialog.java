package it.uniba.pioneers.testtool.editor.node.dialogs;

import android.app.AlertDialog;
import android.content.Context;

import it.uniba.pioneers.testtool.editor.node.GraphNodeModifica;
import it.uniba.pioneers.testtool.editor.node.GraphNodeVisualizza;

public class NodeDialog extends NodeModificaDialog {
    public static AlertDialog NodeDialog(Context context, GraphNodeModifica nodeObject) {
        AlertDialog tmpDialog = getDialog(context, nodeObject);
        return tmpDialog;
    }

    public static AlertDialog NodeDialog(Context context, GraphNodeVisualizza nodeObject) {
        AlertDialog tmpDialog = NodeVisualizzaDialog.getDialog(context, nodeObject);
        return tmpDialog;
    }
}
