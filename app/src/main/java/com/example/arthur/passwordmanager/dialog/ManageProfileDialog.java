package com.example.arthur.passwordmanager.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.example.arthur.passwordmanager.R;
import com.example.arthur.passwordmanager.model.Profile;

import java.util.ArrayList;

public class ManageProfileDialog extends DialogFragment {

    public interface ManageProfileDialogListener {
        void onEdit(Profile profile);
        void onDelete(Profile profile);
        void onCopyLogin(Profile profile);
    }

    private ManageProfileDialog.ManageProfileDialogListener listener;

    public static ManageProfileDialog newInstance(Profile profile) {
        ManageProfileDialog f = new ManageProfileDialog();
        Bundle args = new Bundle();
        args.putParcelable("profile", profile);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        CharSequence menuItems[];
        menuItems = new CharSequence[]{getString(R.string.edit), getString(R.string.delete), getString(R.string.copy_login)};
        final Profile profile = getArguments().getParcelable("profile");

        builder.setItems(menuItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                listener.onEdit(profile);
                                break;
                            case 1:
                                listener.onDelete(profile);
                                break;
                            case 2:
                                listener.onCopyLogin(profile);
                                break;
                        }
                        dialog.dismiss();
                    }
                }
        );
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ManageProfileDialog.ManageProfileDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement DialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (listener != null)
            listener = null;
    }
}
