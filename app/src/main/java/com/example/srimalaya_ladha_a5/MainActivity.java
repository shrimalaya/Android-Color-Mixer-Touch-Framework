package com.example.srimalaya_ladha_a5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnDragListener {

    SeekBar red_seek, green_seek, blue_seek;
    TextView red_txt, green_txt, blue_txt, mixed_txt;
    Boolean firstDrop = true;

    private static final String FIRSTDROP = "FIRSTDROP";
    private static final String MIXCOLOR = "MIXCOLOR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTextViews();
        initSeekBars();

        registerChangeListeners();
        registerDragAndDrop();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);

            v.startDrag(data, shadowBuilder, v, 0);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case (DragEvent.ACTION_DROP):
                View view = (View) event.getLocalState();
                TextView dropTarget = (TextView) v;
                TextView dropped = (TextView) view;
                Drawable background = dropTarget.getBackground();

                int targetColor = 0;
                int red = 0;
                int green = 0;
                int blue = 0;
                if (background instanceof ColorDrawable) {
                    targetColor = ((ColorDrawable) background).getColor();
                    red = Color.red(targetColor);
                    green = Color.green(targetColor);
                    blue = Color.blue(targetColor);

                    if(firstDrop) {
                        red = 0;
                        green = 0;
                        blue = 0;
                        firstDrop = false;
                    }
                }

                // parse color of dropped textView
                String color = dropped.getText().toString();
                String colorVal = color.split(" = ")[1];
                String colorName = color.split(" = ")[0];

                // set color of target
                if(colorName.equals("R")) {
                    dropTarget.setBackgroundColor(Color.rgb(Integer.parseInt(colorVal), green, blue));
                } else if(colorName.equals("G")) {
                    dropTarget.setBackgroundColor(Color.rgb(red, Integer.parseInt(colorVal), blue));
                } else {
                    dropTarget.setBackgroundColor(Color.rgb(red, green, Integer.parseInt(colorVal)));
                }

                break;

            default:
                break;
        }
        return true;
    }

    private void registerDragAndDrop() {
        red_txt.setOnTouchListener(this);
        blue_txt.setOnTouchListener(this);
        green_txt.setOnTouchListener(this);

        mixed_txt.setOnDragListener(this);
    }

    private void registerChangeListeners() {
        red_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                red_txt.setText("R = " + progress);
                red_txt.setBackgroundColor(Color.rgb(progress, 0, 0));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        green_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                green_txt.setText("G = " + progress);
                green_txt.setBackgroundColor(Color.rgb(0, progress, 0));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        blue_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blue_txt.setText("B = " + progress);
                blue_txt.setBackgroundColor(Color.rgb(0, 0, progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void initSeekBars() {
        red_seek = findViewById(R.id.red_seek);
        green_seek = findViewById(R.id.green_seek);
        blue_seek = findViewById(R.id.blue_seek);
    }

    private void initTextViews() {
        red_txt = findViewById(R.id.red_txt);
        green_txt = findViewById(R.id.green_txt);
        blue_txt = findViewById(R.id.blue_txt);
        mixed_txt = findViewById(R.id.mixed_txt);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(FIRSTDROP, firstDrop);
        Drawable mixed_color = mixed_txt.getBackground();
        if (mixed_color instanceof ColorDrawable) {
            int color = ((ColorDrawable) mixed_color).getColor();
            outState.putInt(MIXCOLOR, color);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        firstDrop = savedInstanceState.getBoolean(FIRSTDROP);
        mixed_txt.setBackgroundColor(savedInstanceState.getInt(MIXCOLOR));
    }
}