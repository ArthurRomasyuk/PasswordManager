package com.example.arthur.passwordmanager;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.arthur.passwordmanager.adapter.ProfileAdapter;
import com.example.arthur.passwordmanager.dialog.AddProfileDialog;
import com.example.arthur.passwordmanager.dialog.ManageProfileDialog;
import com.example.arthur.passwordmanager.dialog.PasswordDialog;
import com.example.arthur.passwordmanager.model.Profile;
import com.example.arthur.passwordmanager.view_model.ProfileViewModel;

import java.util.List;

public class ProfileListActivity extends AppCompatActivity implements AddProfileDialog.AddProfileDialogListener, ProfileAdapter.OnItemClickListener, ManageProfileDialog.ManageProfileDialogListener {

    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);

        RecyclerView recyclerView = findViewById(R.id.profile_list);
        final ProfileAdapter adapter = new ProfileAdapter(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        profileViewModel.getAllProfiles().observe(this, new Observer<List<Profile>>() {
            @Override
            public void onChanged(@Nullable final List<Profile> profiles) {
                adapter.setProfiles(profiles);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddProfileDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showPasswordDialog();
    }

    public void showPasswordDialog() {
        new PasswordDialog().show(getSupportFragmentManager(), "addProfileDialog");
    }

    public void showAddProfileDialog() {
        new AddProfileDialog().show(getSupportFragmentManager(), "addProfileDialog");
    }

    public void showUpdateProfileDialog(Profile profile) {
        AddProfileDialog.newInstance(profile).show(getSupportFragmentManager(), "addProfileDialogWithProfile");
    }

    public void showManageProfileDialog(Profile profile){
        ManageProfileDialog manageProfileDialog = ManageProfileDialog.newInstance(profile);
        manageProfileDialog.show(getFragmentManager(), "manageProfileDialog");
    }

    @Override
    public void onAddPositiveClick(Profile profile) {
        profileViewModel.save(profile);
    }

    @Override
    public void onCopyPassword(String password) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            clipboard.setPrimaryClip(ClipData.newPlainText("password", password));
            Toast.makeText(this, R.string.password_was_copied, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(Profile profile) {
        showManageProfileDialog(profile);
    }

    @Override
    public void onDeleteItems(List<Profile> profiles) {
        profileViewModel.deleteSelectedProfiles(profiles);
    }

    @Override
    public void onEdit(Profile profile) {
        showUpdateProfileDialog(profile);
    }

    @Override
    public void onDelete(Profile profile) {
        profileViewModel.delete(profile);
    }

    @Override
    public void onCopyLogin(Profile profile) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            clipboard.setPrimaryClip(ClipData.newPlainText("login", profile.getLogin()));
            Toast.makeText(this, R.string.login_was_copied, Toast.LENGTH_SHORT).show();
        }
    }
}
