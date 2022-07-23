package com.tasklist.sharemyinformation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class GetDataActivity extends AppCompatActivity {

    TextInputEditText name,phone,mail,notes,s_phone,job,postal;
    MaterialButton submit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);

        name = findViewById(R.id.input_name);
        phone = findViewById(R.id.input_phone);
        mail = findViewById(R.id.input_email);
        notes = findViewById(R.id.input_notes);
        s_phone = findViewById(R.id.input_secondary_phone);
        job = findViewById(R.id.input_job_title);
        postal = findViewById(R.id.input_postal);

        submit = findViewById(R.id.save_button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput())
                {
                    SharedPreferences sharedPreferences = getSharedPreferences("ShareMyInformation",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name",name.getText().toString());
                    editor.putString("phone",phone.getText().toString());
                    editor.putString("email",mail.getText().toString());
                    editor.putString("notes",notes.getText().toString());
                    editor.putString("s_phone",s_phone.getText().toString());
                    editor.putString("job",job.getText().toString());
                    editor.putString("postal",postal.getText().toString());
                    editor.putBoolean("isSet",true);
                    editor.apply();

                    Intent intent = new Intent(GetDataActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public boolean validateInput()
    {
        String sNAme = name.getText().toString();

        if (sNAme.length()<=0)
        {
            name.setError("Name is Requited");
            return false;
        }

        String sPhone = phone.getText().toString();
        if (sPhone.length()<=9)
        {
            phone.setError("Phone is Required");
            return false;
        }

        return true;
    }
}