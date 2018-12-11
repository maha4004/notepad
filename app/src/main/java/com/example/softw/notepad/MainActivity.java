package com.example.softw.notepad;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText editText ;
    Button buttonsave;
    String stringText ;
    private static final int write_external_code =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=(EditText)findViewById(R.id.textt);
        buttonsave=(Button) findViewById(R.id.btn);
        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stringText = editText.getText().toString().trim();

                if (stringText.isEmpty()){
                    Toast.makeText(MainActivity.this,"you did not enter anything",Toast.LENGTH_LONG).show();
                }

                else {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if (checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                PackageManager.PERMISSION_DENIED) {
                            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permissions,write_external_code);


                        }
                        else {
                            saveToFileText(stringText);
                        }
                    }
                    else {
                        saveToFileText(stringText);


                    }

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case write_external_code:{
                if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    saveToFileText(stringText);
                }

                else {

                    Toast.makeText(MainActivity.this,"storage permission is required to store data",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void saveToFileText(String stringText) {
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());

        try {

            File path = Environment.getExternalStorageDirectory();
            File dir = new File(path + "/My Files/");
            dir.mkdirs();
            String fileName = "MyFile_" + time + ".txt";
            File file = new File(dir,fileName);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(stringText);
            bw.close();

            Toast.makeText(MainActivity.this,fileName+ "is saved" + dir,Toast.LENGTH_LONG).show();

        }

        catch (Exception e){

            Toast.makeText(MainActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();

        }
    }
}
