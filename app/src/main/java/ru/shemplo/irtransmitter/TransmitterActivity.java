package ru.shemplo.irtransmitter;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TransmitterActivity extends AppCompatActivity {
    private static final int PRECISION = 300;
    private ConsumerIrManager irManager;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_transmitter);

        TextView noIRText = findViewById (R.id.no_ir_text);
        LinearLayout IRLayout = findViewById (R.id.ir_layout);

        Button helloButton = findViewById (R.id.hello_button);
        helloButton.setOnClickListener (v -> transmit ("Hello, world! This is a small step of big thing".getBytes (StandardCharsets.UTF_8)));
        //powerButton.setOnClickListener (v -> transmit ("aaaaaaba".getBytes (StandardCharsets.UTF_8)));

        Button randomButton = findViewById (R.id.random_button);
        randomButton.setOnClickListener (v -> transmit ("....".getBytes (StandardCharsets.UTF_8)));

        EditText inputTextField = findViewById (R.id.text_input_field);
        inputTextField.addTextChangedListener (new TextWatcher() {

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                transmit (s.toString ().getBytes (StandardCharsets.UTF_8));
            }

            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged (Editable s) {}

        });

        irManager = getSystemService (ConsumerIrManager.class);
        noIRText.setVisibility (irManager.hasIrEmitter () ? View.GONE : View.VISIBLE);
        IRLayout.setVisibility (irManager.hasIrEmitter () ? View.VISIBLE : View.GONE);
    }

    public void transmit (byte [] data) {
        irManager.transmit (33000, encode (data));
    }

    private int [] encode (byte [] data) {
        Log.i ("A", Arrays.toString (data));
        int [] result = new int [(data.length << 3) + 1];
        result [result.length - 1] = PRECISION / 2;

        for (int b = 0; b < data.length; b++) {
            for (int i = 0; i < 8; i++) {
                result [(b << 3) + i] = PRECISION * (((data [b] >>> (7 - i)) & 1) + 1);
            }
        }

        Log.i ("A", Arrays.toString (result));
        return result;
    }

}