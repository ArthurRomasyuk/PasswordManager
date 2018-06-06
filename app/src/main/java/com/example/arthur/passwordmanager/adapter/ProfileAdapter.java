package com.example.arthur.passwordmanager.adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arthur.passwordmanager.R;
import com.example.arthur.passwordmanager.model.Profile;

import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private List<Profile> profiles;
    private OnItemClickListener listener;
    private boolean multiSelect = false;
    private List<Profile> selectedItems = new ArrayList<Profile>();

    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            multiSelect = true;
            MenuItem delete = menu.add("Delete");
            delete.setIcon(R.drawable.delete);
            selectedItems.clear();
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            listener.onDeleteItems(selectedItems);
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelect = false;
        }
    };

    public ProfileAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, parent, false);
        return new ProfileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProfileViewHolder holder, int position) {
        final Profile profile = profiles.get(position);
        holder.login.setText(profile.getLogin());
        holder.password.setText(profile.getPassword());
        if (holder.unmask.isChecked()) {
            holder.maskText.setVisibility(View.INVISIBLE);
            holder.password.setVisibility(View.VISIBLE);
        } else {
            holder.maskText.setVisibility(View.VISIBLE);
            holder.password.setVisibility(View.INVISIBLE);
        }
        holder.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCopyPassword(profile.getPassword());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (multiSelect)
                    holder.selectItem(profile);
                else
                    listener.onItemClick(profile);
            }
        });
        holder.unmask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.maskText.setVisibility(View.INVISIBLE);
                    holder.password.setVisibility(View.VISIBLE);
                } else {
                    holder.maskText.setVisibility(View.VISIBLE);
                    holder.password.setVisibility(View.INVISIBLE);
                }
            }
        });
        holder.update(profile);
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                ((AppCompatActivity) v.getContext()).startSupportActionMode(actionModeCallbacks);
//                selectItem(profile, holder);
//                return true;
//            }
//        });

    }

    @Override
    public int getItemCount() {
        if (profiles != null)
            return profiles.size();
        else return 0;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
        notifyDataSetChanged();
    }

//    void selectItem(Profile profile, ProfileViewHolder holder) {
//        if (multiSelect) {
//            if (selectedItems.contains(profile)) {
//                selectedItems.remove(profile);
//                holder.itemView.setBackgroundColor(Color.WHITE);
//            } else {
//                selectedItems.add(profile);
//                holder.itemView.setBackgroundColor(Color.LTGRAY);
//            }
//        }
//    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder {

        private TextView login;
        private TextView password;
        private ImageView copy;
        private TextView maskText;
        private CheckBox unmask;

        ProfileViewHolder(View itemView) {
            super(itemView);
            login = itemView.findViewById(R.id.login);
            password = itemView.findViewById(R.id.password);
            copy = itemView.findViewById(R.id.copy);
            maskText = itemView.findViewById(R.id.mask_text);
            unmask = itemView.findViewById(R.id.unmask);
        }

        void selectItem(Profile item) {
            if (multiSelect) {
                if (selectedItems.contains(item)) {
                    selectedItems.remove(item);
                    itemView.setBackgroundColor(Color.WHITE);
                } else {
                    selectedItems.add(item);
                    itemView.setBackgroundColor(Color.LTGRAY);
                }
            }
        }

        void update(final Profile value) {
            if (selectedItems.contains(value)) {
                itemView.setBackgroundColor(Color.LTGRAY);
            } else {
                itemView.setBackgroundColor(Color.WHITE);
            }
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ((AppCompatActivity)view.getContext()).startSupportActionMode(actionModeCallbacks);
                    selectItem(value);
                    return true;
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onCopyPassword(String password);

        void onItemClick(Profile profile);

        void onDeleteItems(List<Profile> profiles);
    }
}
