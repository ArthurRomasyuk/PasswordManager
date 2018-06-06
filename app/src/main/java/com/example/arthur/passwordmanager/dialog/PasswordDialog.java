package com.example.arthur.passwordmanager.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.arthur.passwordmanager.R;
import com.marcoscg.fingerauth.FingerAuth;
import com.marcoscg.fingerauth.FingerAuthDialog;

public class PasswordDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        setCancelable(false);
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_password, null);
        final TextView text = dialogView.findViewById(R.id.text);
        final EditText password = dialogView.findViewById(R.id.password);
        final String existingPassword = sharedPref.getString("password", "");
        builder.setView(dialogView);
        if (TextUtils.isEmpty(existingPassword)) {
            text.setText(R.string.password_dialog_create_text);
            if (FingerAuth.hasFingerprintSupport(getActivity()))
                builder.setPositiveButton(R.string.create, null).setNegativeButton(R.string.use_fingerprint, null);
            else
                builder.setPositiveButton(R.string.create, null);
        } else {
            text.setText(R.string.password_dialog_enter_password);
            if (FingerAuth.hasFingerprintSupport(getActivity()))
                builder.setPositiveButton(R.string.ok, null).setNegativeButton(R.string.use_fingerprint, null);
            else
                builder.setPositiveButton(R.string.ok, null);
        }

        final AlertDialog passwordDialog = builder.create();
        passwordDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button positiveButton = passwordDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negativeButton = passwordDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                positiveButton.setTextColor(getActivity().getResources().getColor(com.marcoscg.fingerauth.R.color.fingerauth_dialog_color_accent));
                negativeButton.setTextColor(getActivity().getResources().getColor(com.marcoscg.fingerauth.R.color.fingerauth_dialog_color_accent));
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(existingPassword)) {
                            String pass = password.getText().toString();
                            if (pass.length() > 0) {
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("password", pass);
                                editor.apply();
                                passwordDialog.dismiss();
                            } else {
                                text.setText(R.string.password_dialog_create_text_empty_case);
                            }
                        } else {
                            String pass = password.getText().toString();
                            if (pass.length() > 0) {
                                if (existingPassword.equals(pass))
                                    passwordDialog.dismiss();
                                else
                                    text.setText(R.string.password_dialog_wrong_password_text);
                            } else {
                                text.setText(R.string.password_dialog_create_text_empty_pass_try);
                            }
                        }
                    }
                });
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showFingerAuthDialog(passwordDialog);
                    }
                });

            }
        });
        return passwordDialog;
    }

    private void showFingerAuthDialog(final AlertDialog passwordDialog) {
        new FingerAuthDialog(getActivity())
                .setTitle(getString(R.string.fingerprint_authentication))
                .setCancelable(false)
                .setMaxFailedCount(3)
                .setPositiveButton(R.string.use_password, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setOnFingerAuthListener(new FingerAuth.OnFingerAuthListener() {
                    @Override
                    public void onSuccess() {
                        passwordDialog.dismiss();
                    }

                    @Override
                    public void onFailure() {
                    }

                    @Override
                    public void onError() {
                    }
                }).show();
    }

}
