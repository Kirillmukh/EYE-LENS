package com.example.eyelens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Change_recept extends AppCompatActivity {

    public EditText l1, l2, l3, l4, l5, r1, r2, r3, r4, r5;
    public Button cnfrmBtn, goBack, reset;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_recept);

        l1 = findViewById(R.id.OS_1);
        l2 = findViewById(R.id.OS_2);
        l3 = findViewById(R.id.OS_3);
        l4 = findViewById(R.id.OS_4);
        l5 = findViewById(R.id.OS_5);
        r1 = findViewById(R.id.OD_1);
        r2 = findViewById(R.id.OD_2);
        r3 = findViewById(R.id.OD_3);
        r4 = findViewById(R.id.OD_4);
        r5 = findViewById(R.id.OD_5);

        cnfrmBtn = findViewById(R.id.page3_confirm_button);
        reset = findViewById(R.id.page3_reset_button);
        goBack = findViewById(R.id.button_page3);

        dbHelper = new DBHelper(this);

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_RECEPT, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int indL1 = cursor.getColumnIndex(DBHelper.L1);
            int indL2 = cursor.getColumnIndex(DBHelper.L2);
            int indL3 = cursor.getColumnIndex(DBHelper.L3);
            int indL4 = cursor.getColumnIndex(DBHelper.L4);
            int indL5 = cursor.getColumnIndex(DBHelper.L5);

            int indR1 = cursor.getColumnIndex(DBHelper.R1);
            int indR2 = cursor.getColumnIndex(DBHelper.R2);
            int indR3 = cursor.getColumnIndex(DBHelper.R3);
            int indR4 = cursor.getColumnIndex(DBHelper.R4);
            int indR5 = cursor.getColumnIndex(DBHelper.R5);

            if (cursor.getString(indL1).equals("nullValue")) l1.setText("");
            else l1.setText(cursor.getString(indL1));
            if (cursor.getString(indL2).equals("nullValue")) l2.setText("");
            else l2.setText(cursor.getString(indL2));
            if (cursor.getString(indL3).equals("nullValue")) l3.setText("");
            else l3.setText(cursor.getString(indL3));
            if (cursor.getString(indL4).equals("nullValue")) l4.setText("");
            else l4.setText(cursor.getString(indL4));
            if (cursor.getString(indL5).equals("nullValue")) l5.setText("");
            else l5.setText(cursor.getString(indL5));

            if (cursor.getString(indR1).equals("nullValue")) r1.setText("");
            else r1.setText(cursor.getString(indR1));
            if (cursor.getString(indR2).equals("nullValue")) r2.setText("");
            else r2.setText(cursor.getString(indR2));
            if (cursor.getString(indR3).equals("nullValue")) r3.setText("");
            else r3.setText(cursor.getString(indR3));
            if (cursor.getString(indR4).equals("nullValue")) r4.setText("");
            else r4.setText(cursor.getString(indR4));
            if (cursor.getString(indR5).equals("nullValue")) r5.setText("");
            else r5.setText(cursor.getString(indR5));

        } else Log.d("mLog", "Not yeat");
        cursor.close();

        cnfrmBtn.setOnClickListener(v -> {
            String vl1, vl2, vl3, vl4, vl5;
            String vr1, vr2, vr3, vr4, vr5;

            ContentValues contentValues = new ContentValues();

            if (l1.getText().toString().equals("")) vl1 = "nullValue";
            else vl1 = l1.getText().toString();
            if (l2.getText().toString().equals("")) vl2 = "nullValue";
            else vl2 = l2.getText().toString();
            if (l3.getText().toString().equals("")) vl3 = "nullValue";
            else vl3 = l3.getText().toString();
            if (l4.getText().toString().equals("")) vl4 = "nullValue";
            else vl4 = l4.getText().toString();
            if (l5.getText().toString().equals("")) vl5 = "nullValue";
            else vl5 = l5.getText().toString();

            if (r1.getText().toString().equals("")) vr1 = "nullValue";
            else vr1 = r1.getText().toString();
            if (r2.getText().toString().equals("")) vr2 = "nullValue";
            else vr2 = r2.getText().toString();
            if (r3.getText().toString().equals("")) vr3 = "nullValue";
            else vr3 = r3.getText().toString();
            if (r4.getText().toString().equals("")) vr4 = "nullValue";
            else vr4 = r4.getText().toString();
            if (r5.getText().toString().equals("")) vr5 = "nullValue";
            else vr5 = r5.getText().toString();

            contentValues.put(DBHelper.L1, vl1);
            contentValues.put(DBHelper.L2, vl2);
            contentValues.put(DBHelper.L3, vl3);
            contentValues.put(DBHelper.L4, vl4);
            contentValues.put(DBHelper.L5, vl5);

            contentValues.put(DBHelper.R1, vr1);
            contentValues.put(DBHelper.R2, vr2);
            contentValues.put(DBHelper.R3, vr3);
            contentValues.put(DBHelper.R4, vr4);
            contentValues.put(DBHelper.R5, vr5);

            database.delete(DBHelper.TABLE_RECEPT, null, null);
            database.insert(DBHelper.TABLE_RECEPT, null, contentValues);

            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
        });

        reset.setOnClickListener(v -> {

            l1.setText("");
            l2.setText("");
            l3.setText("");
            l4.setText("");
            l5.setText("");

            r1.setText("");
            r2.setText("");
            r3.setText("");
            r4.setText("");
            r5.setText("");

            Toast.makeText(this, "Нажмите подтвердить для сохранения", Toast.LENGTH_LONG).show();
        });

        goBack.setOnClickListener(v -> {

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}