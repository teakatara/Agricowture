package com.example.syouk.Agricowture;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class DroneDialog extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        int Cownum = getArguments().getInt("CowNum");
        dialogBuilder.setTitle("ここにドローンを向かわせますか？");
        dialogBuilder.setMessage(Cownum+"にドローンを向かわせますか？");

        // OKボタン
        dialogBuilder.setPositiveButton("はい", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //デバッグ用
                Toast toast = Toast.makeText(getActivity(), "はいを押した", Toast.LENGTH_SHORT);
                toast.show();
                Constant.droneOK = true;
            }
        });

        // NGボタン
        dialogBuilder.setNegativeButton("いいえ", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Constant.droneWhileEscape = true;
            }
        });

        return dialogBuilder.create();
    }
}
