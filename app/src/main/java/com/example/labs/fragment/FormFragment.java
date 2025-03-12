package com.example.labs.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.labs.R;

public class FormFragment extends Fragment {
    private EditText entryField;
    private Button submitBtn;
    private RadioGroup textFonts;

    public static final String SELECTED_FONT_ARG = "selected_font";
    public static final String ENTRY_TEXT_ARG = "entry_text";

    public static final String RESULT_FRAGMENT_TAG = "result_tag";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form, container, false);

        entryField = view.findViewById(R.id.entryField);
        submitBtn = view.findViewById(R.id.submitBtn);
        textFonts = view.findViewById(R.id.textFonts);

        submitBtn.setOnClickListener(v -> showResult());

        return view;
    }

    private void showResult() {
        String enteredValue = entryField.getText().toString();
        int checkedValue = textFonts.getCheckedRadioButtonId();

        if (checkedValue == -1 || enteredValue.trim().isEmpty()) {
            Toast.makeText(getContext(), R.string.warning_message, Toast.LENGTH_LONG).show();
            return;
        }

        Bundle args = new Bundle();
        args.putString(ENTRY_TEXT_ARG, enteredValue);
        args.putInt(SELECTED_FONT_ARG, checkedValue);

        ResultFragment resultFragment = new ResultFragment();
        resultFragment.setArguments(args);
        FragmentManager fragmentManager = getParentFragmentManager();
        resultFragment.show(fragmentManager, RESULT_FRAGMENT_TAG);
    }

    public void clearInputs() {
        entryField.setText("");
        textFonts.clearCheck();
    }

    public interface OnClearInputListener {
        void onClearInput();
    }
}
