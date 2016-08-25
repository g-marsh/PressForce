package com.gregmarsh.pressforce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import static java.lang.Math.PI;

public class MainActivity extends AppCompatActivity {

    // Controls
    ScrollView svDisplay;
    EditText etID;
    EditText etOD;
    EditText etTD;
    EditText etLength;
    Spinner spnInterference;
    Spinner spnMaterial;
    Spinner spnLubricated;
    RadioButton rbLubricatedYes;
    TextView tvCFriction;
    TextView tvModE;
    TextView tvInRad;
    TextView tvOutRad;
    TextView tvTransRad;
    TextView tvRadInterf;
    TextView tvPress;
    TextView tvStressT;
    TextView tvStressR;
    TextView tvKStress;
    TextView tvForce;
    TextView tvLblKStress;
    TextView tvLblForce;

    // Variables
    String strInterference;
    String strMaterial;
    boolean blnLubricated;
    double dblCFriction;
    double dblModE;
    double dblInRad;
    double dblOutRad;
    double dblTransRad;
    double dblRadInterf;
    double dblPress;
    double dblStressT;
    double dblStressR;
    double dblKStress;
    double dblForce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate Controls

        svDisplay = (ScrollView) findViewById(R.id.scrollView);
        etID = (EditText) findViewById(R.id.editText);
        etOD = (EditText) findViewById(R.id.editText2);
        etTD = (EditText) findViewById(R.id.editText3);
        etLength = (EditText) findViewById(R.id.editText4);
        spnInterference = (Spinner) findViewById(R.id.spinner);
        spnMaterial = (Spinner) findViewById(R.id.spinner2);
        spnLubricated = (Spinner) findViewById(R.id.spinner);
        rbLubricatedYes = (RadioButton)findViewById(R.id.radioButton2);
        tvCFriction = (TextView) findViewById(R.id.textView9);
        tvModE = (TextView) findViewById(R.id.textView11);
        tvInRad = (TextView) findViewById(R.id.textView13);
        tvOutRad = (TextView) findViewById(R.id.textView15);
        tvTransRad = (TextView) findViewById(R.id.textView17);
        tvRadInterf = (TextView) findViewById(R.id.textView19);
        tvPress = (TextView) findViewById(R.id.textView21);
        tvStressT = (TextView) findViewById(R.id.textView23);
        tvStressR = (TextView) findViewById(R.id.textView25);
        tvKStress = (TextView) findViewById(R.id.textView27);
        tvForce = (TextView) findViewById(R.id.textView29);
        tvLblKStress = (TextView) findViewById(R.id.textView28);
        tvLblForce = (TextView) findViewById(R.id.textView30);

        // Declare and populate arrays
        String [] aryMaterial = getResources().getStringArray(R.array.aryMaterial);
        String [] aryInterference = getResources().getStringArray(R.array.aryInterference);

        // Bind the arrays and apply settings to the Spinners.
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, aryMaterial);
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, aryInterference);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMaterial.setAdapter(dataAdapter);
        spnInterference.setAdapter(dataAdapter1);

        // Calculate and View Results
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strMaterial = String.valueOf(spnMaterial.getSelectedItem());
                if(rbLubricatedYes.isChecked()) blnLubricated = true;
                else blnLubricated = false;

                // http://www.engineeringtoolbox.com/friction-coefficients-d_778.html
                if (strMaterial.equals("Steel")){
                    if (blnLubricated) dblCFriction=.16;
                    else dblCFriction=.65;
                    dblModE=30000000.0;
                }
                else if (strMaterial.equals("Alum.")){
                    if (blnLubricated) dblCFriction=.3;
                    else dblCFriction=1.2;
                    dblModE=10100000.0;
                }
                else if (strMaterial.equals("Acetal")){
                    dblCFriction=.35;
                    dblModE=300000.0;
                }
                tvCFriction.setText(String.format("%.2f",dblCFriction));
                tvModE.setText(String.format("%.2e",dblModE));

                dblInRad = Double.valueOf(etID.getText().toString())/2;
                tvInRad.setText(String.format("%.3f",dblInRad));

                dblOutRad = Double.valueOf(etOD.getText().toString())/2;
                tvOutRad.setText(String.format("%.3f",dblOutRad));

                dblTransRad = Double.valueOf(etTD.getText().toString())/2;
                tvTransRad.setText(String.format("%.3f",dblTransRad));

                dblRadInterf = Double.valueOf(spnInterference.getSelectedItem().toString())/2;
                tvRadInterf.setText(String.format("%.5f",dblRadInterf));

                dblPress = dblModE*dblRadInterf*(Math.pow(dblOutRad,2)-Math.pow(dblTransRad,2))
                        *(Math.pow(dblTransRad,2)-Math.pow(dblInRad,2))
                        /(2*Math.pow(dblTransRad,3)*(Math.pow(dblOutRad,2)-Math.pow(dblInRad,2)));
                tvPress.setText(String.format("%.0f",dblPress));

                dblStressT = dblPress*Math.pow(dblTransRad,2)
                        *(1+Math.pow(dblOutRad,2)/Math.pow(dblTransRad,2))
                        /(Math.pow(dblOutRad,2)-Math.pow(dblTransRad,2));
                tvStressT.setText(String.format("%.0f",dblStressT));

                dblStressR = dblPress*Math.pow(dblTransRad,2)
                        *(1-Math.pow(dblOutRad,2)/Math.pow(dblTransRad,2))
                        /(Math.pow(dblOutRad,2)-Math.pow(dblTransRad,2));
                tvStressR.setText(String.format("%.0f",dblStressR));

                dblKStress = Math.pow((Math.pow(dblStressT-dblStressR,2)+Math.pow(dblStressR,2)+Math.pow(dblStressT,2))/2,.5)/1000;
                tvKStress.setText(String.format("%.2f",dblKStress));

                dblForce = 2*dblTransRad*PI*Double.valueOf(etLength.getText().toString())*dblPress*dblCFriction;
                tvForce.setText(String.format("%.0f",dblForce));

                svDisplay.post(new Runnable() {
                    @Override
                    public void run() {
                        svDisplay.fullScroll(View.FOCUS_DOWN);
                    }
                });









            }
        });







    }
}
