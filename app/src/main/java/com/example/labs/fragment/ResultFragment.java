package com.example.labs.fragment;

import static com.example.labs.fragment.FormFragment.ENTRY_TEXT_ARG;
import static com.example.labs.fragment.FormFragment.SELECTED_FONT_ARG;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.example.labs.R;

public class ResultFragment extends DialogFragment {

    private TextView result;
    private Button clearButton;
    private FormFragment.OnClearInputListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        result = view.findViewById(R.id.result);
        clearButton = view.findViewById(R.id.closeBtn);

        Bundle args = getArguments();
        String entryText = args.getString(ENTRY_TEXT_ARG);
        int selectedFont = args.getInt(SELECTED_FONT_ARG);

        Typeface typeface = Typeface.DEFAULT;

        if(selectedFont == R.id.monomakh) {
            typeface = ResourcesCompat.getFont(requireContext(), R.font.monomakh);
        } else if (selectedFont == R.id.roboto) {
            typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto);
        } else if (selectedFont == R.id.oswald) {
            typeface = ResourcesCompat.getFont(requireContext(), R.font.oswald);
        }
        result.setTypeface(typeface);
        result.setText(entryText);

        clearButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClearInput();
            }
            dismiss();
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (FormFragment.OnClearInputListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + "must implement OnClearInputListener");
        }
    }
}