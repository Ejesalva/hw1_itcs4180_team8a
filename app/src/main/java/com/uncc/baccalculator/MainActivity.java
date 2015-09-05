package com.uncc.baccalculator;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    User currentUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SeekBar alcoholLevelBar = (SeekBar) findViewById(R.id.idAlcoholProgress);
        final ProgressBar bacLevelBar = (ProgressBar) findViewById(R.id.idBacProgress);
        final TextView alcoholLevel = (TextView) findViewById(R.id.idAlcoholSeeker);
        final TextView bacLevelView = (TextView) findViewById(R.id.idBacLevel);
        final TextView savedUser = (TextView) findViewById(R.id.idSavedUserValue);
        final TextView statusView = (TextView) findViewById(R.id.idStatus);

        final Button saveButton = (Button) findViewById(R.id.idSave);
        final Button addButton = (Button) findViewById(R.id.idAddDrinkButton);
        Button resetButton = (Button) findViewById(R.id.idAResetButton);

        final int alarmColor = getResources().getColor(R.color.caution_background_color);
        final int cautionColor = getResources().getColor(R.color.warning_background_color);
        final int safeColor = getResources().getColor(R.color.safe_background_color);

        alcoholLevelBar.setProgress(1);
        alcoholLevel.setText("5%");

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser = new User();
                alcoholLevelBar.setProgress(1);
                alcoholLevel.setText("5%");
                bacLevelBar.setProgress(0);

                savedUser.setText("");
                bacLevelView.setText("BAC Level: 0.00");

                statusView.setText("You're safe");
                statusView.setBackgroundColor(safeColor);

                addButton.setEnabled(true);
                saveButton.setEnabled(true);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText weightInput = (EditText) findViewById(R.id.idWeight);
                Switch genderInput = (Switch) findViewById(R.id.idGender);

                String weightValue = weightInput.getText().toString();
                String gender = genderInput.isChecked() ? genderInput.getTextOn().toString() : genderInput.getTextOff().toString();

                if (Integer.parseInt(weightValue) > 0) {
                    currentUser.setGender(gender);
                    currentUser.setWeight(Integer.parseInt(weightValue));
                    currentUser.setAsActive();
                    savedUser.setText("Current User: " + currentUser.getWeight() + " lbs " + currentUser.getGender());

                    if(currentUser.getBac() > 0){
                        bacLevelView.setText("BAC Level: " + String.format("%.2f", currentUser.getBac()));

                        if (bacLevelBar.getProgress() >= 20) {
                            statusView.setText("Over the limit!");
                            statusView.setBackgroundColor(alarmColor);

                            addButton.setEnabled(false);
                            saveButton.setEnabled(false);
                        }
                        else if ( 8 <= bacLevelBar.getProgress() && bacLevelBar.getProgress() < 20){
                            statusView.setText("Be careful...");
                            statusView.setBackgroundColor(cautionColor);
                        }
                        else{
                            statusView.setText("You're safe");
                            statusView.setBackgroundColor(safeColor);
                        }
                    }

                } else {
                    weightInput.requestFocus();
                    weightInput.setError("valid weight values must be over zero.");
                }

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup drinkSizeSelection = (RadioGroup) findViewById(R.id.idDrinkSizeGrp);
                RadioButton drinkSizeInput = (RadioButton) findViewById(drinkSizeSelection.getCheckedRadioButtonId());

                String drinkSizeText = drinkSizeInput.getText().toString();
                Integer alcoholLevel = alcoholLevelBar.getProgress();

                if (currentUser.getActiveValue()) {
                    currentUser.setDrinkSelection(drinkSizeText, alcoholLevel);
                    Toast.makeText(getApplicationContext(), (alcoholLevel * 5) + "%, " + drinkSizeText + " drink added.", Toast.LENGTH_LONG).show();

                    bacLevelView.setText("BAC Level: " + String.format("%.2f", currentUser.getBac()));

                    Double tempBac = currentUser.getBac() * 100;
                    bacLevelBar.setProgress(tempBac.intValue());

                    if (bacLevelBar.getProgress() >= 20) {
                        statusView.setText("Over the limit!");
                        statusView.setBackgroundColor(alarmColor);

                        addButton.setEnabled(false);
                        saveButton.setEnabled(false);
                    }
                    else if ( 8 <= bacLevelBar.getProgress() && bacLevelBar.getProgress() < 20){
                        statusView.setText("Be careful...");
                        statusView.setBackgroundColor(cautionColor);
                    }
                    else{
                        statusView.setText("You're safe");
                        statusView.setBackgroundColor(safeColor);
                    }

                } else
                    Toast.makeText(getApplicationContext(), "please set a user before adding a drink", Toast.LENGTH_LONG).show();

            }
        });

        alcoholLevelBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                alcoholLevel.setText(String.valueOf(progress * 5) + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
