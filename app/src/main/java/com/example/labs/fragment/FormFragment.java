package com.example.labs.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.labs.R;
import com.example.labs.RecordsActivity;
import com.example.labs.db.TextStyleRepository;
import com.example.labs.db.dao.TextStyleEntity;
import com.example.labs.db.impl.TextStyleRepositoryImpl;

public class FormFragment extends Fragment {
    private EditText entryField;

    private RadioGroup textFonts;

    public static final String SELECTED_FONT_ARG = "selected_font";
    public static final String ENTRY_TEXT_ARG = "entry_text";

    public static final String RESULT_FRAGMENT_TAG = "result_tag";

    private String enteredValue;
    private int checkedValue;

    TextStyleRepository textStyleRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form, container, false);
        textStyleRepository = new TextStyleRepositoryImpl(getContext());

        entryField = view.findViewById(R.id.entryField);
        Button saveBtn = view.findViewById(R.id.saveBtn);
        textFonts = view.findViewById(R.id.textFonts);
        Button showRecordsBtn = view.findViewById(R.id.showRecordsBtn);
        Button showResultBtn = view.findViewById(R.id.showResultBtn);
        Button clearBtn = view.findViewById(R.id.clearBtn);

        saveBtn.setOnClickListener(v -> saveResult());
        showResultBtn.setOnClickListener(v -> showResult());
        showRecordsBtn.setOnClickListener(v -> showAllDbRecords());
        clearBtn.setOnClickListener(v -> clearInputs());

        return view;
    }

    private void saveResult() {
        boolean isCorrect = validateInputValues();

        if (!isCorrect) return;

        boolean result = textStyleRepository.createTextStyleEntry(new TextStyleEntity(enteredValue, checkedValue));

        if (!result) {
            Toast.makeText(getContext(), R.string.record_saving_failed_message, Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(getContext(), R.string.record_added_successful_message, Toast.LENGTH_LONG).show();
    }

    private void showResult() {
        boolean isCorrect = validateInputValues();

        if (!isCorrect) return;

        Bundle args = new Bundle();
        args.putString(ENTRY_TEXT_ARG, enteredValue);
        args.putInt(SELECTED_FONT_ARG, checkedValue);

        ResultFragment resultFragment = new ResultFragment();
        resultFragment.setArguments(args);
        FragmentManager fragmentManager = getParentFragmentManager();
        resultFragment.show(fragmentManager, RESULT_FRAGMENT_TAG);
    }

    private boolean validateInputValues() {
        enteredValue = entryField.getText().toString();
        checkedValue = textFonts.getCheckedRadioButtonId();

        if (checkedValue == -1 || enteredValue.trim().isEmpty()) {
            Toast.makeText(getContext(), R.string.warning_message, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void showAllDbRecords() {
        startActivity(new Intent(getActivity(), RecordsActivity.class));
    }

    public void clearInputs() {
        entryField.setText("");
        textFonts.clearCheck();
    }

}
