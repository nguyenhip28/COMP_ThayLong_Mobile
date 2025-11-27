package com.example.computer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;
    private String operand1 = "";
    private String operand2 = "";
    private String operator = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.resultTextView);

        View.OnClickListener numberClickListener = v -> {
            Button button = (Button) v;
            resultTextView.append(button.getText().toString());
        };

        findViewById(R.id.button0).setOnClickListener(numberClickListener);
        findViewById(R.id.button1).setOnClickListener(numberClickListener);
        findViewById(R.id.button2).setOnClickListener(numberClickListener);
        findViewById(R.id.button3).setOnClickListener(numberClickListener);
        findViewById(R.id.button4).setOnClickListener(numberClickListener);
        findViewById(R.id.button5).setOnClickListener(numberClickListener);
        findViewById(R.id.button6).setOnClickListener(numberClickListener);
        findViewById(R.id.button7).setOnClickListener(numberClickListener);
        findViewById(R.id.button8).setOnClickListener(numberClickListener);
        findViewById(R.id.button9).setOnClickListener(numberClickListener);

        View.OnClickListener operatorClickListener = v -> {
            Button button = (Button) v;
            if (!resultTextView.getText().toString().isEmpty()) {
                operand1 = resultTextView.getText().toString();
                operator = button.getText().toString();
                resultTextView.setText("");
            }
        };

        findViewById(R.id.buttonAdd).setOnClickListener(operatorClickListener);
        findViewById(R.id.buttonSubtract).setOnClickListener(operatorClickListener);
        findViewById(R.id.buttonMultiply).setOnClickListener(operatorClickListener);
        findViewById(R.id.buttonDivide).setOnClickListener(operatorClickListener);

        findViewById(R.id.buttonEquals).setOnClickListener(v -> {
            if (!operand1.isEmpty() && !operator.isEmpty() && !resultTextView.getText().toString().isEmpty()) {
                operand2 = resultTextView.getText().toString();
                double result = 0;
                try {
                    double num1 = Double.parseDouble(operand1);
                    double num2 = Double.parseDouble(operand2);

                    switch (operator) {
                        case "+":
                            result = num1 + num2;
                            break;
                        case "-":
                            result = num1 - num2;
                            break;
                        case "*":
                            result = num1 * num2;
                            break;
                        case "/":
                            if (num2 != 0) {
                                result = num1 / num2;
                            } else {
                                resultTextView.setText("Error");
                                return;
                            }
                            break;
                    }
                    resultTextView.setText(String.valueOf(result));
                } catch (NumberFormatException e) {
                    resultTextView.setText("Error");
                }
                operand1 = "";
                operand2 = "";
                operator = "";
            }
        });

        findViewById(R.id.buttonClear).setOnClickListener(v -> {
            resultTextView.setText("");
            operand1 = "";
            operand2 = "";
            operator = "";
        });
    }
}
