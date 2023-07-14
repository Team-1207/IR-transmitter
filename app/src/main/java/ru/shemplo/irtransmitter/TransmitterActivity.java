package ru.shemplo.irtransmitter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TransmitterActivity extends AppCompatActivity {

    private static final int PRECISION = 300;

    private ConsumerIrManager irManager;

    //public static int [] POWER = {9000, 4450, 550, 600, 500, 600, 550, 600, 550, 550, 550, 600, 500, 600, 550, 1700, 550, 550, 550, 600, 550, 550, 550, 600, 550, 550, 550, 600, 550, 550, 550, 1700, 550, 600, 550, 1700, 550, 550, 550, 1700, 550, 1700, 550, 550, 550, 600, 550, 1700, 550, 550, 550, 600, 550, 1650, 600, 550, 550, 550, 600, 1650, 550, 1700, 550, 600, 550, 1700, 550};
    public static int [] POWER = {200, 400, 200, 200, 200, 400, 400, 400, 50};

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_transmitter);

        TextView noIRText = findViewById (R.id.no_ir_text);
        LinearLayout IRLayout = findViewById (R.id.ir_layout);

        Button powerButton = findViewById (R.id.power_button);
        powerButton.setOnClickListener (v -> transmit ("Hello, world! This is a small step of big thing".getBytes (StandardCharsets.UTF_8)));
        //powerButton.setOnClickListener (v -> transmit ("aaaaaaba".getBytes (StandardCharsets.UTF_8)));

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