package com.example.roy.cryptofolio;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class CurrencyDialog extends AppCompatDialogFragment {
    private EditText editTextAmount;
    private CurrencyDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("Amount")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Nothing happens here
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String amount = editTextAmount.getText().toString();

                        listener.addToPortfolio(amount);
                    }
                });

        editTextAmount = view.findViewById(R.id.edit_amount);

        return builder.create();
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try {
            listener = (CurrencyDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement CurrencyDialogListener");
        }
    }

    public interface CurrencyDialogListener {
        void addToPortfolio(String amount);
    }
}
