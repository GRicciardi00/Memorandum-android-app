package com.example.memoapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class InfoFragment extends DialogFragment {
    public InfoFragment() {

    }
    private TextView descriptionView;

    public static InfoFragment newInstance(String title) {
        InfoFragment frag = new InfoFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.info_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        descriptionView = (TextView) view.findViewById(R.id.descriptionTextView);
        //Set how to use description
        descriptionView.setText(
                "● Use this app to save your memos.\n\r" +
                "● Add a memo with '+ button' on the right.\n\r" +
                "● For view your memo tap on it, delete your memos with a long tap.\n\r"+
                "● You can set your memos completed or active after clicking on it.\n\r" +
                "● The 'little man button' change the view from active memos to completed memos\n\r"+
                        "● The 'Show expired memo button' allow you to change the view to the expired memos\n\r"+
                "● The 'map button' on the left shows all the active memos on the map.");

    }
}
