package com.prasadmukne.android.fnotestapp.ui.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.prasadmukne.android.fnotestapp.R;
import com.prasadmukne.android.fnotestapp.utils.FNOViewModel;

/**
 * Created by Prasad Mukne on 23-07-2020.
 */
public class HomeFragment extends Fragment {
    private String fragName;

    public HomeFragment(String name) {
        fragName = name;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final FNOViewModel mFnoViewModel =
                ViewModelProviders.of(getActivity()).get(FNOViewModel.class);

        initialiseUI(root, mFnoViewModel);
        return root;
    }

    private void initialiseUI(View root, final FNOViewModel mFnoViewModel) {
        TextView textView = root.findViewById(R.id.text_home);
        textView.setText("This is " + fragName.toUpperCase() + " fragment");
        AppCompatImageButton deleteButton = root.findViewById(R.id.delete_button);
        if (fragName.equals("default page"))
            deleteButton.setVisibility(View.INVISIBLE);
        else
            deleteButton.setVisibility(View.VISIBLE);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragDeleteDialog(mFnoViewModel);
            }
        });
    }

    private void showFragDeleteDialog(final FNOViewModel mFnoViewModel) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.delete_dialog_confirm))
                .setMessage(getString(R.string.delete_dialog_message))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mFnoViewModel.removeFragment(fragName);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
