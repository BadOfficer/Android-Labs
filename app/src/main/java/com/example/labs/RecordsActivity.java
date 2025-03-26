package com.example.labs;

import static android.view.Gravity.CENTER_VERTICAL;
import static android.view.View.TEXT_ALIGNMENT_CENTER;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.HORIZONTAL;
import static com.example.labs.fragment.FormFragment.ENTRY_TEXT_ARG;
import static com.example.labs.fragment.FormFragment.RESULT_FRAGMENT_TAG;
import static com.example.labs.fragment.FormFragment.SELECTED_FONT_ARG;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.labs.db.TextStyleRepository;
import com.example.labs.db.dao.TextStyleEntity;
import com.example.labs.db.impl.TextStyleRepositoryImpl;
import com.example.labs.fragment.ResultFragment;

import java.util.List;

public class RecordsActivity extends AppCompatActivity {
    private TableLayout table;
    private Button deleteBtn;
    private Button backBtn;
    private TextStyleRepository textStyleRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        deleteBtn = findViewById(R.id.deleteAllBtn);
        table = findViewById(R.id.recordsTable);
        backBtn = findViewById(R.id.goBackBtn);

        backBtn.setOnClickListener(v -> finish());

        textStyleRepository = new TextStyleRepositoryImpl(this);

        renderTable();

        deleteBtn.setOnClickListener(v -> deleteAllRecords());
    }

    private void showEmptyMessage() {
        TextView message = new TextView(this);
        message.setText(R.string.table_empty_message);
        message.setWidth(MATCH_PARENT);
        message.setPadding(0, 20, 0, 20);
        message.setTextSize(24);
        message.setTextColor(this.getResources().getColor(R.color.red));
        message.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        deleteBtn.setEnabled(false);
        table.addView(message);
    }

    private void createTableHeader() {
        TableRow headerRow = new TableRow(this);

        TextView textColumn = new TextView(getApplication());
        textColumn.setText(R.string.table_text_column_header);
        textColumn.setTextAlignment(TEXT_ALIGNMENT_CENTER);

        TextView fontColumn = new TextView(getApplication());
        fontColumn.setText(R.string.table_font_column_header);
        fontColumn.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        fontColumn.setLayoutParams(new TableRow.LayoutParams(0, WRAP_CONTENT, 1));

        TextView actionColumn = new TextView(getApplication());
        actionColumn.setText(R.string.table_actions_column_header);
        actionColumn.setTextAlignment(TEXT_ALIGNMENT_CENTER);

        headerRow.addView(textColumn);
        headerRow.addView(fontColumn);
        headerRow.addView(actionColumn);

        table.addView(headerRow);
    }

    private void createTableRow(TextStyleEntity textStyleRow) {
        String font = "";

        if (textStyleRow.getFontId() == R.id.monomakh) {
            font = this.getString(R.string.third_type);
        } else if (textStyleRow.getFontId() == R.id.oswald) {
            font = this.getString(R.string.first_type);
        } else if (textStyleRow.getFontId() == R.id.roboto) {
            font = this.getString(R.string.second_type);
        }

        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableRow.LayoutParams(
                MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

        tableRow.setGravity(CENTER_VERTICAL);

        TextView textValue = new TextView(this);
        textValue.setText(textStyleRow.getText());
        textValue.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        textValue.setMaxWidth(200);

        TextView fontValue = new TextView(this);
        fontValue.setText(font);
        fontValue.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        fontValue.setLayoutParams(new TableRow.LayoutParams(0, WRAP_CONTENT, 1));

        LinearLayout actionsColumn = new LinearLayout(this);
        actionsColumn.setOrientation(HORIZONTAL);

        Button showResultBtn = new Button(this);
        showResultBtn.setText(R.string.try_result_btn);
        showResultBtn.setOnClickListener(v -> tryResult(textStyleRow.getText(), textStyleRow.getFontId()));

        ImageButton removeRecordBtn = new ImageButton(this);
        removeRecordBtn.setImageResource(R.drawable.ic_delete);
        removeRecordBtn.setLayoutParams(new TableRow.LayoutParams(
                WRAP_CONTENT,
                MATCH_PARENT
        ));
        removeRecordBtn.setOnClickListener(v -> deleteRecordById(textStyleRow.getId()));

        actionsColumn.addView(showResultBtn);
        actionsColumn.addView(removeRecordBtn);

        tableRow.addView(textValue);
        tableRow.addView(fontValue);
        tableRow.addView(actionsColumn);

        table.addView(tableRow);
    }

    private void renderTable() {
        table.removeAllViews();

        List<TextStyleEntity> styles = textStyleRepository.getAllTextStyles();

        if (styles.isEmpty()) {
            showEmptyMessage();
        } else {
            createTableHeader();
            styles.forEach(this::createTableRow);
        }
    }

    private void tryResult(String text, int fontId) {
        Bundle args = new Bundle();
        args.putString(ENTRY_TEXT_ARG, text);
        args.putInt(SELECTED_FONT_ARG, fontId);

        ResultFragment resultFragment = new ResultFragment();
        resultFragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        resultFragment.show(fragmentManager, RESULT_FRAGMENT_TAG);
    }

    private void deleteAllRecords() {
        textStyleRepository.deleteAllTextStyles();
        Toast.makeText(this, R.string.records_deleted_successful_message, Toast.LENGTH_LONG).show();
        renderTable();
    }

    private void deleteRecordById(long id) {
        textStyleRepository.deleteTextStyleById(id);
        Toast.makeText(this, R.string.record_deleted_successful_message, Toast.LENGTH_LONG).show();
        renderTable();
    }
}