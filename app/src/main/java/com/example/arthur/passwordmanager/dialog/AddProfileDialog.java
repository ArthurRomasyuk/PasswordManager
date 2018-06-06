package com.example.arthur.passwordmanager.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.arthur.passwordmanager.R;
import com.example.arthur.passwordmanager.model.Profile;

public class AddProfileDialog extends DialogFragment {

    private AddProfileDialogListener listener;
    private Profile profile;

    public interface AddProfileDialogListener {
        void onAddPositiveClick(Profile profile);
    }

    public static AddProfileDialog newInstance(Profile profile) {
        AddProfileDialog f = new AddProfileDialog();
        Bundle args = new Bundle();
        args.putParcelable("profile", profile);
        f.setArguments(args);
        return f;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_profile, null);
        final EditText login = dialogView.findViewById(R.id.login);
        final EditText password = dialogView.findViewById(R.id.password);
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    confirmProfile(login, password);
                }
                return false;
            }
        });
        if (getArguments() != null) {
            profile = getArguments().getParcelable("profile");
            if (profile != null) {
                login.setText(profile.getLogin());
                password.setText(profile.getPassword());
                login.setSelection(login.getText().length());
            }
        }

        builder.setView(dialogView)
                .setPositiveButton(R.string.add, null)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddProfileDialog.this.dismiss();
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmProfile(login, password);
                    }
                });
            }
        });

        return dialog;
    }

    private void confirmProfile(EditText login, EditText password) {
        String loginText = login.getText().toString();
        String passwordText = password.getText().toString();
        if (loginText.length() > 0 && passwordText.length() > 0) {
            if (profile == null)
                profile = new Profile(loginText, passwordText);
            else {
                profile.setLogin(loginText);
                profile.setPassword(passwordText);
            }
            listener.onAddPositiveClick(profile);
            AddProfileDialog.this.dismiss();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AddProfileDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (listener != null)
            listener = null;
    }
}
