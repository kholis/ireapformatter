package com.kiosafiy.ireapformatter;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //FloatingActionButton fab;
    //Toolbar toolbar;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText editText, etOngkir, etCatatan, edNamaDropshipper, edTelpDropshipper;
    Button btnCopy, btnShare;
    String namaEkspedisi, ongkir, catatan;
    String namaToko = "Kios Afiy";
    String noTelpon = "08989225632";
    Spinner spEkspedisi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.editText);

        /*
        InputStream inputStream = getResources().openRawResource(R.raw.report);
        CSVFile csvFile = new CSVFile(inputStream);
        List mycsv = csvFile.read();
        Collections.sort(mycsv);
        String mycsv2 = String.join("\n", mycsv);

        EditText editText = (EditText)findViewById(R.id.editText);
        editText.setText(mycsv2);
        */

        /*
        // https://developer.android.com/training/sharing/receive
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();


        if (Intent.ACTION_SEND.equals(action) && type != null) {
            // https://stackoverflow.com/questions/40028941/how-to-fix-null-uri-data-intent-getdata-in-android
            for (String key : intent.getExtras().keySet()) {
                Log.e("KEYS", action + " | " + type + " | " + key);
            }
//            if ("text/csv".equals(type)) {
            if (type.startsWith("plain/")) {
                //Uri uri = intent.getData();
                //Log.i(uri);
                Uri uri = (Uri) getIntent().getExtras().get(Intent.EXTRA_STREAM);
                if (uri != null) {
//                    Toast.makeText(this, uri.toString(), Toast.LENGTH_LONG).show();
                    Log.e("URI", uri.toString());
                    handleSendFile(uri);
                } else {
                    handleSendText(intent); // Handle text being sent
                }



            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        } else if (Intent.ACTION_GET_CONTENT.equals(action) && type != null) {
            // Handle other intents, such as being started from the home screen
            if (type.startsWith("text/")) {
                for (String key : intent.getExtras().keySet()) { Log.e("KEYS", key + "Z2"); }
                Uri uri = (Uri) getIntent().getExtras().get(Intent.EXTRA_STREAM);
                handleSendFile(uri); // Handle single file
            }
        }
        */
    }

    @Override
    protected void onResume() {
        super.onResume();

        showChoiceAlert();
    }

    void tanganiIntent() {
        // https://developer.android.com/training/sharing/receive
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();


        if (Intent.ACTION_SEND.equals(action) && type != null) {
            for (String key : intent.getExtras().keySet()) {
                Log.e("KEYS", action + " | " + type + " | " + key);
            }
            // if ("text/csv".equals(type)) {
            if (type.startsWith("plain/")) {
                Uri uri = (Uri) getIntent().getExtras().get(Intent.EXTRA_STREAM);
                if (uri != null) {
                    // Toast.makeText(this, uri.toString(), Toast.LENGTH_LONG).show();
                    Log.e("URI", uri.toString());
                    if (uri.toString().contains("inventoryreport.csv")) {
                        handleSendFileStok(uri);
                    } else {
                        handleSendFile(uri);
                    }
                } else {
                    handleSendText(intent); // Handle text being sent
                }
            } else if (type.startsWith("text/")) {
                handleSendText(intent);
            }
        }
    }

    void handleSendText(Intent intent) {
        Log.e("TEXT", "hello");
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        //Uri uri = intent.getD

        //Bundle bundle = intent.getExtras();
        //Uri uri = (Uri)bundle.get(Intent.EXTRA_STREAM);


//        String foo = intent.getDataString();
        //Toast.makeText(this, uri.toString(), Toast.LENGTH_LONG).show();
        if (sharedText != null) {
            // Update UI to reflect text being shared
            editText = (EditText)findViewById(R.id.editText);
            editText.setText(sharedText);

        }
    }

    void handleSendTextInvoice() {
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("text/")) {
                String a = intent.getStringExtra(Intent.EXTRA_TEXT);
                //Toast.makeText(this, uri.toString(), Toast.LENGTH_LONG).show();
                if (a != null) {
                    // Update UI to reflect text being shared
                    a = a.replace("Store\n", "");
                    //a = a.replaceall("===\n0", "");
                    a = a.replace("Tax:  0\n", "");
                    a = a.replace("Change:  IDR 0\n", "");
                    a = a.replace("\nPowered by iReap POS Lite", "");
                    editText = (EditText) findViewById(R.id.editText);
                    editText.setText(a + "\nOngkir: " + ongkir + "\nNotes: " + namaEkspedisi);
                }
            }
        }
    }

    void handleSendFile(Uri uri) {
        //String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        //Uri uri = (Uri) getIntent().getExtras().get(Intent.EXTRA_STREAM);
        //Toast.makeText(this, uri.toString(), Toast.LENGTH_LONG).show();
//        String foo = intent.getAction();
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        Uri uri = Uri.fromFile(testfile);
        //ContentResolver cr = contex.getContentResolver();
        //InputStream istr = cr.openInputStream(uri);
        //Toast.makeText(this, inputStream.toString(), Toast.LENGTH_LONG).show();

        /*if (uri != null) {
            // Update UI to reflect text being shared
            EditText editText = (EditText)findViewById(R.id.editText);
            editText.setText(uri.toString());

        }*/

        //InputStream inputStream = getResources().openRawResource(uri);
        CSVFile csvFile = new CSVFile(inputStream);
        List mycsv = csvFile.read();
        Collections.sort(mycsv);
        String mycsv2 = TextUtils.join("\n", mycsv);

        EditText editText = (EditText)findViewById(R.id.editText);
        //editText.setText("NAMA BARANG | HARGA | STOK\n=============================\n" + mycsv2);
    }

    void handleSendFileStok(Uri uri) {
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        CSVFile csvFile = new CSVFile(inputStream);
        List mycsv = csvFile.read();
        Collections.sort(mycsv);
        String mycsv2 = TextUtils.join("\n", mycsv);

        editText = (EditText)findViewById(R.id.editText);
        editText.setText("NAMA BARANG | HARGA | STOK\n==========================\n" + mycsv2);
    }

    /*
    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
        }
    }
    */

    void showChoiceAlert() {
        final EditText editText = (EditText)findViewById(R.id.editText);

        // https://stackoverflow.com/questions/15762905/how-can-i-display-a-list-view-in-an-android-alert-dialog
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose action:");

        // add a list
        final String[] animals = {"Format Invoice", "Format Stok"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Format Invoice
                        dialogForm();
                        //tanganiIntent();
                        break;
                    case 1: // Format Stock
                        tanganiIntent();
                        break;
                }
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // https://www.codepolitan.com/membuat-custom-alert-dialog-di-android-studio-5a9620bb20e59
    private void dialogForm() {
        dialog = new AlertDialog.Builder(MainActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_invoice, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        //dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("     Tambahan Info Invoice:");

        editText = (EditText)findViewById(R.id.editText);
        etOngkir = (EditText) dialogView.findViewById(R.id.etOngkir);
        etCatatan = (EditText) dialogView.findViewById(R.id.etCatatan);
        edNamaDropshipper = (EditText) dialogView.findViewById(R.id.edNamaDropshipper);
        edTelpDropshipper = (EditText) dialogView.findViewById(R.id.edTelpDropshipper);

        // Spinner Ekspedisi
        String[] items = new String[]{"JNE", "JNT", "TIKI", "POS", "GOJEK", "GRAB", "WAHANA", "SiCepat", "NinjaExpress"};
        spEkspedisi = (Spinner) dialogView.findViewById(R.id.spEkspedisi);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spEkspedisi.setAdapter(adapter);
        namaEkspedisi = String.valueOf(spEkspedisi.getSelectedItem());


        dialog.setPositiveButton("Proses", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ongkir = etOngkir.getText().toString();
                catatan = etCatatan.getText().toString();
                if (edNamaDropshipper.getText().toString() != null) {
                    namaToko = edNamaDropshipper.getText().toString();
                }

                if (edTelpDropshipper.getText().toString() != null) {
                    noTelpon = edTelpDropshipper.getText().toString();
                }

                namaEkspedisi = String.valueOf(spEkspedisi.getSelectedItem());

                //editText.setText("Ongkir : " + ongkir + "\n" + "Catatan : " + catatan);
                dialog.dismiss();
                handleSendTextInvoice();
            }
        });

        dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // https://developer.android.com/guide/topics/ui/controls/button
    public void onClickBtnCopy(View view) {
        // Do something in response to button click
        // https://stackoverflow.com/questions/238284/how-to-copy-text-programmatically-in-my-android-app
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", editText.getText());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show();
    }

    public void onClickBtnShare(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, editText.getText());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }
}
