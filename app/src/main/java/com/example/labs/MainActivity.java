package com.example.labs;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button submitBtn;
    private Button cancelBtn;
    private TextView resultText;
    private EditText enterField;
    private RadioGroup textTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        submitBtn = findViewById(R.id.submitBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        textTypes = findViewById(R.id.textTypes);
        resultText = findViewById(R.id.result);
        enterField = findViewById(R.id.entryInfo);

        submitBtn.setOnClickListener(v -> {
            String enteredValue = enterField.getText().toString();
            int checkedValue = textTypes.getCheckedRadioButtonId();

            if (checkedValue == -1 || enteredValue.trim().isEmpty()) {
                Toast.makeText(MainActivity.this, R.string.warning_message, Toast.LENGTH_LONG).show();
                return;
            }

            RadioButton checkedButton = findViewById(checkedValue);
            int checkedId = checkedButton.getId();

            Typeface typeface = Typeface.DEFAULT;

            if(checkedId == R.id.monomakh) {
                typeface = ResourcesCompat.getFont(MainActivity.this, R.font.monomakh);
            } else if (checkedId == R.id.roboto) {
                typeface = ResourcesCompat.getFont(MainActivity.this, R.font.roboto);
            } else if (checkedId == R.id.oswald) {
                typeface = ResourcesCompat.getFont(MainActivity.this, R.font.oswald);
            }
            resultText.setTypeface(typeface);
            resultText.setText(enterField.getText().toString());
        });

        cancelBtn.setOnClickListener(v -> {
            textTypes.clearCheck();
            enterField.setText("");
            resultText.setText("");
        });
    }
}