package com.lifeistech.android.sharedtournament;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

public class MyProgressFragment extends DialogFragment {
    private ProgressDialog prog;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        prog=new ProgressDialog(getActivity());
        prog.setMessage("読み込み中...");
        prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return prog;
    }
    public int getProgress(){
        return prog.getProgress();
    }
    public void setProgress(int value){
        prog.setProgress(value);
    }

}
