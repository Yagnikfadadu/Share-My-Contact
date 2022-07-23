package com.tasklist.sharemyinformation;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {

    ImageView qrCode;
    int height,width;
    String name,phone,mail,notes,s_phone,job,postal;
    SwitchMaterial sEmail,sNotes,sSecondary,sJob,sPostal;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qrCode = findViewById(R.id.image_qr);
        actionBar = getSupportActionBar();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        actionBar.setTitle("Share Contact");

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FFFF5252"));
        actionBar.setBackgroundDrawable(colorDrawable);

        SharedPreferences sharedPreferences = getSharedPreferences("ShareMyInformation", MODE_PRIVATE);
        name = sharedPreferences.getString("name", "No valid data");
        phone = sharedPreferences.getString("phone", "No valid data");
        mail = sharedPreferences.getString("email", "No valid data");
        notes = sharedPreferences.getString("notes", "No valid data");
        s_phone = sharedPreferences.getString("s_phone", "No valid data");
        job = sharedPreferences.getString("job", "No valid data");
        postal = sharedPreferences.getString("postal", "No valid data");


        sEmail = findViewById(R.id.share_email);
        sNotes = findViewById(R.id.share_notes);
        sSecondary = findViewById(R.id.share_s_phone);
        sJob = findViewById(R.id.share_job);
        sPostal = findViewById(R.id.share_postal);

//        String mergedString = "str_myName " + name + "`~@" + " str_myPhone " + phone + "`~@" + " str_myMail " + mail + "`~@" + " str_myNotes " + notes + "`~@" + " str_s_phone " + s_phone + "`~@" + " str_job " + job + "`~@" + " str_postal " + postal;
//        String mergedString = name + "`~@" + phone + "`~@"  + mail;
//        initializeQR();
//        initializeVCard();

        initializeUniversalQR();

        Boolean isSet = sharedPreferences.getBoolean("isSet", false);
        if (!isSet) {
            Intent getDataActivityIntent = new Intent(MainActivity.this, GetDataActivity.class);
            startActivity(getDataActivityIntent);
        }


    }

    public void generateCode(String qrString)
    {
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(qrString, BarcodeFormat.QR_CODE, (int) (width/1.5), (int) (width/1.5));
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            qrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    public void setContact()
    {
        Intent intent = new Intent((ContactsContract.Intents.Insert.ACTION));
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.NAME,"test1");
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, "test.email");
        intent.putExtra(ContactsContract.Intents.Insert.PHONE,"+919898");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0,1,1,"Scan QR Code");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
            IntentIntegrator intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.setPrompt("Scan a barcode or QR Code ");
            intentIntegrator.setBeepEnabled(true);
            intentIntegrator.setCaptureActivity(CustomOrientationScanner.class);
            intentIntegrator.initiateScan();
            intentIntegrator.setRequestCode(1);

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    String result = intentResult.toString();
                    String[] myContact = result.split("\n");
                    String name = myContact[2];
                    String phone = myContact[3];
                    String email = myContact[4];
                    String notes = myContact[5];
                    String secondary = myContact[6];
                    String job = myContact[7];
                    String postal = myContact[8];
                    name = name.substring(3);
                    phone = phone.substring(4);
                    email = email.substring(6);
                    notes = notes.substring(5);
                    secondary = secondary.substring(18);
                    job = job.substring(6);
                    postal = postal.substring(4);

                    Intent intent = new Intent((ContactsContract.Intents.Insert.ACTION));
                    intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                    intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                    intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
                    intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                    intent.putExtra(ContactsContract.Intents.Insert.NOTES, notes);
                    intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, secondary);
                    intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, job);
                    intent.putExtra(ContactsContract.Intents.Insert.POSTAL, postal);
                    startActivity(intent);

                }
                catch (Exception e)
                {
                    Toast.makeText(this, "Please scan valid QR Code", Toast.LENGTH_SHORT).show();
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void initializeQR()
    {
        String genString = name + "`~@" + phone + "`~@" ;

        if(sEmail.isChecked())
        {
            genString += mail+ "`~@";
        }
        else
        {
            genString +="" + "`~@";
        }

        if (sNotes.isChecked())
        {
            genString += notes+ "`~@";
        }
        else
        {
            genString += "" + "`~@";
        }

        if (sSecondary.isChecked())
        {
            genString += s_phone+ "`~@";
        }
        else
        {
            genString += "" + "`~@";
        }

        if (sJob.isChecked())
        {
            genString += job + "`~@";
        }
        else
        {
            genString += "" + "`~@";
        }

        if (sPostal.isChecked())
        {
            genString += postal + "`~@";
        }
        else
        {
            genString += "" + "`~@";
        }

        generateCode(genString);
    }

    public void initializeUniversalQR()
    {
        String vCard = "BEGIN:VCARD\nFN:"+name+"\nTEL:"+phone+"\nEMAIL:"+mail+"\nNOTE: none\nTEL;TYPE=work:tel: none\nTITLE: none\nADR: none\nEND:VCARD\n";
        generateCode(vCard);
    }

    public void regenerateQR(View view)
    {
        String vCard = "BEGIN:VCARD\nFN:"+name+"\nTEL:"+phone;

        if(sEmail.isChecked())
        {
            vCard += "\nEMAIL:"+mail;
        }
        else
        {
            vCard += "\nEMAIL:"+" ";
        }

        if (sNotes.isChecked())
        {
            vCard += "\nNOTE:"+notes;
        }
        else
        {
            vCard += "\nNOTE:"+" ";
        }

        if (sSecondary.isChecked())
        {
            vCard += "\nTEL;TYPE=work:tel:"+s_phone;
        }
        else
        {
            vCard += "\nTEL;TYPE=work:tel:"+" ";
        }

        if (sJob.isChecked())
        {
            vCard += "\nTITLE:"+job;
        }
        else
        {
            vCard += "\nTITLE:"+" ";
        }

        if (sPostal.isChecked())
        {
            vCard += "\nADR:"+postal;
        }
        else
        {
            vCard += "\nADR:"+" ";
        }



        generateCode(vCard+"\nEND:VCARD\n");

    }

    public void initializeVCard()
    {
        String QRString = String.format("BEGIN:VCARD\nFN:%S\nTEL:%s\nNOTE:%s\nEMAIL;TYPE=INTERNET:%s\nTITLE:%s\nADR:%s\nEND:VCARD\n",name,phone,notes,mail,job,postal);
        generateCode(QRString);
    }
}