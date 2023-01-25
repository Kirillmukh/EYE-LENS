package com.example.eyelens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    ProgressBar pb;
    Button howToUse, changeRecept, changeTimeLine, typeOfLens, finalPage;
    DBHelperPeriod dbHelperPeriod;
    TextView tv;
    int i = 0;

    public int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        howToUse = findViewById(R.id.page1_button1);
        changeRecept = findViewById(R.id.page1_button3);
        changeTimeLine = findViewById(R.id.period);
        typeOfLens = findViewById(R.id.page1_button2);
        finalPage = findViewById(R.id.page1_button4);

        pb = findViewById(R.id.pb);
        pb.setMax(100);
        pb.setProgress(0);

        tv = findViewById(R.id.page1_switch);
        tv.setVisibility(View.INVISIBLE);


        howToUse.setOnClickListener(v -> {
            Intent intent = new Intent(this, Videos.class);
            startActivity(intent);
        });

        changeRecept.setOnClickListener(v -> {
            Intent intent = new Intent(this, Change_recept.class);
            startActivity(intent);
        });

        changeTimeLine.setOnClickListener(v -> {
            Intent intent = new Intent(this, Change_timeline.class);
            startActivity(intent);
        });

        typeOfLens.setOnClickListener(v -> {
            Intent intent = new Intent(this, How_to_use.class);
            startActivity(intent);
        });

        finalPage.setOnClickListener(v -> {
            if (progress < 100)
                Toast.makeText(this, "Эта кнопка станет доступна по истечении срока линз", Toast.LENGTH_LONG).show();
            else {
                Intent intent = new Intent(this, FinalActivity.class);
                startActivity(intent);
            }
        });

        dbHelperPeriod = new DBHelperPeriod(this);
    }

    public int getToday() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return (int) Math.floor(((double) calendar.getTimeInMillis()) / 1000 / 60 / 60 / 24);
    }

    public String getDay(int n) {
        for (int i = 1; i < 800; i += 10) {
            if (i != 11 && i != 111 && i != 211 && i != 311 && i != 411 && i != 511 && i != 611 && i != 711 && n == i)
                return " день";
        }
        if ((n % 10 == 2 || n % 10 == 3 || n % 10 == 4) && (n % 100 / 10 != 1)) return " дня";
        else return " дней";
    }

    public void setProgressValue() {
        final Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                pb.setProgress(i);
                i += 1;
                if (i > progress)
                    t.cancel();
            }
        };

        t.schedule(tt, 200, 15);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pb.setProgress(0);
        SQLiteDatabase database = dbHelperPeriod.getReadableDatabase();
        Cursor cursor = database.query(DBHelperPeriod.TABLE_PERIOD, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int index1 = cursor.getColumnIndex(DBHelperPeriod.FINALTIME);
            int index2 = cursor.getColumnIndex(DBHelperPeriod.LENGTH);

            int today = getToday();
            int finalDay = cursor.getInt(index1);
            int length = cursor.getInt(index2);
            float dif = (((float) (length - (finalDay - today)) / ((float) length)));
            progress = (int) Math.floor(dif * 100);

            int daysLeft = finalDay - today;
            String text;
            if (daysLeft > 0) {
                String string = String.valueOf(daysLeft);
                String day = getDay(daysLeft);
                text = "До замены " + string + day;
            } else {
                text = "Пора менять линзы!!";
            }
            tv.setText(text);
            tv.setVisibility(View.VISIBLE);

        } else {
            progress = 0;
        }
        if (progress >= 100) progress = 100;
        cursor.close();
        setProgressValue();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pb.setProgress(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelperPeriod.close();
    }
}