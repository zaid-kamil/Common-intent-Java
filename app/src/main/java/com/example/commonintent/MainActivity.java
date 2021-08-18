package com.example.commonintent;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MotionEvent;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_SELECT_CONTACT = 239;
    private EditText editPhone;
    private EditText editPlace;
    private TextView textContact;
    private TextView textLabel;
    private LinearLayout touchArea;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnDial = findViewById(R.id.btnDial);
        Button btnMap = findViewById(R.id.btnMap);
        editPhone = findViewById(R.id.editPhone);
        editPlace = findViewById(R.id.editPlace);
        textContact = findViewById(R.id.textPhone);
        Button btnPick = findViewById(R.id.btnPick);
        touchArea = findViewById(R.id.touchArea);
        textLabel = findViewById(R.id.textLabel);
        btnDial.setOnClickListener(v -> openDialer());
        btnMap.setOnClickListener(v -> openMap());
        btnPick.setOnClickListener(v -> selectContact());
        touchArea.setOnTouchListener((view, motionEvent) -> {
            int action = motionEvent.getAction();
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (action == MotionEvent.ACTION_DOWN) {
                textLabel.setText("You are touching Screen");
                textLabel.animate()
                        .x(x)
                        .y(y)
                        .setDuration(200)
                        .setStartDelay(100)
                        .setInterpolator(new BounceInterpolator()).start();
            }
            return true;
        });
    }

    private void openMap() {
        String place = editPlace.getText().toString();
        if (place.length() > 0) {
            Uri loc = Uri.parse("geo:0,0?q=" + place);
            Intent launchMap = new Intent(Intent.ACTION_VIEW, loc);
            if (launchMap.resolveActivity(getPackageManager()) != null) {
                startActivity(launchMap);
            } else {
                Toast.makeText(this, "no activity for map found", Toast.LENGTH_SHORT).show();
            }
        } else {
            editPlace.setError("empty place data");
            editPlace.requestFocus();
        }
    }

    private void openDialer() {
        String phone = editPhone.getText().toString();
        if (phone.length() > 0) {
            Uri dialer = Uri.parse("tel:" + phone);
            Intent launchDialer = new Intent(Intent.ACTION_DIAL, dialer);
            if (launchDialer.resolveActivity(getPackageManager()) != null) {
                startActivity(launchDialer);
            } else {
                Toast.makeText(this, "no activity can perform this operation", Toast.LENGTH_SHORT).show();
            }
        } else {
            editPhone.setError("empty phone number");
            editPhone.requestFocus();
        }
    }

    public void selectContact() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_CONTACT);
        }
    }

    public static int wordCount(String sentence) {
        String[] words = sentence.split(" ");
        return words.length;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor = getContentResolver().query(contactUri, projection,
                    null, null, null);
            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberIndex);
                textContact.setText(number);
            }
        }
    }

}