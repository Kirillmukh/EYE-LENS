package com.example.eyelens;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class Change_timeline extends AppCompatActivity {

    private EditText number, period, ethours, etminutes;
    Calendar cal;
    Button cnfrmBtn, reset, choose;
    DBHelperPeriod dbHelperPeriod;
    DBHelperUnique dbHelperUnique;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    int valHour, valMinute;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_timeline);

        createNotificationChannel();

        number = findViewById(R.id.timeline_num);
        period = findViewById(R.id.timeline_period);
        ethours = findViewById(R.id.etChoose1);
        etminutes = findViewById(R.id.etChoose2);

        cnfrmBtn = findViewById(R.id.page4_confirm_button);
        reset = findViewById(R.id.page4_resetbutton);
        choose = findViewById(R.id.page4_chooseBtn);

        tv = findViewById(R.id.umolch);

        dbHelperPeriod = new DBHelperPeriod(this);
        SQLiteDatabase database = dbHelperPeriod.getWritableDatabase();

        dbHelperUnique = new DBHelperUnique(this);
        SQLiteDatabase databaseUniq = dbHelperUnique.getWritableDatabase();

        Cursor cursor = databaseUniq.query(DBHelperUnique.TABLE_UNIQUE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int indexHour = cursor.getColumnIndex(DBHelperUnique.NOTIFY_HOURS);
            int indexMin = cursor.getColumnIndex(DBHelperUnique.NOTIFY_MINUTES);
            valHour = cursor.getInt(indexHour);
            valMinute = cursor.getInt(indexMin);
            ethours.setText(String.valueOf(valHour));
            etminutes.setText(String.valueOf(valMinute));
            tv.setText("Нынешнее время: ");
        }
        else {
            valHour = 12;
            valMinute = 0;
        }
        cursor.close();

        cnfrmBtn.setOnClickListener(v -> {
            String secondaryNum = number.getText().toString();
            String secondaryPeriod = period.getText().toString();
            if ((secondaryNum.equals("") || secondaryNum.equals("0")) && (secondaryPeriod.equals("") || isNotPeriod(secondaryPeriod))) {
                showToast("Оба поля введены некорректно");
            } else if (!secondaryNum.equals("") && (secondaryPeriod.equals("") || isNotPeriod(secondaryPeriod))) {
                showToast("Период введен некорректно");
            } else if (secondaryNum.equals("") || secondaryNum.equals("0")) {
                showToast("Число введено некорректно");
            } else {
                int num = Integer.parseInt(number.getText().toString());
                String timeline = period.getText().toString();
                cal = getFinalDate(num, timeline);
                setNotify();
                int length = getLengthOfPeriod(num, timeline);
                int finalDay = getFinalDayOfPeriod(cal);
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelperPeriod.FINALTIME, finalDay);
                contentValues.put(DBHelperPeriod.LENGTH, length);

                database.delete(DBHelperPeriod.TABLE_PERIOD, null, null);
                database.insert(DBHelperPeriod.TABLE_PERIOD, null, contentValues);
                showToast("Сохранено");
            }
        });

        reset.setOnClickListener(v -> {
            database.delete(DBHelperPeriod.TABLE_PERIOD, null, null);
            Toast.makeText(this, "Успешно сброшено", Toast.LENGTH_SHORT).show();
            cancelNotify();
        });

        choose.setOnClickListener(v -> {
            if (isNotTime()) Toast.makeText(this, "Укажите корректное время слева от кнопки, когда должно приходить уведомление", Toast.LENGTH_LONG).show();
            else if(isNotHour() || isNotMinute()) Toast.makeText(this, "Укажите корректное время слева от кнопки, когда должно приходить уведомление", Toast.LENGTH_LONG).show();
            else if (isNotHour()) Toast.makeText(this, "Введите корректное кол-во часов в промежутке от 0 до 24 часов", Toast.LENGTH_LONG).show();
            else if (isNotMinute()) Toast.makeText(this, "Введите корректное кол-во минут в промежутке от 0 до 59 часов", Toast.LENGTH_LONG).show();
            else {
                int h = Integer.parseInt(ethours.getText().toString());
                int m = Integer.parseInt(etminutes.getText().toString());
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelperUnique.NOTIFY_HOURS, h);
                contentValues.put(DBHelperUnique.NOTIFY_MINUTES, m);
                databaseUniq.delete(DBHelperUnique.TABLE_UNIQUE, null, null);
                databaseUniq.insert(DBHelperUnique.TABLE_UNIQUE, null, contentValues);
                tv.setText("Нынешнее время: ");
                Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isNotTime() {
        return ethours.getText().length() == 0 || etminutes.getText().length() == 0;
    }

    public boolean isNotHour() {
        return Integer.parseInt(ethours.getText().toString()) > 24;
    }

    public boolean isNotMinute() {
        return Integer.parseInt(etminutes.getText().toString()) > 59;
    }

    public boolean isNotPeriod(String period) {
        return !period.equals("d") && !period.equals("w") && !period.equals("m");
    }

    public Calendar getFinalDate(int num, String period) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.MINUTE, valMinute);
        calendar.set(Calendar.HOUR_OF_DAY, valHour);

        switch (period) {
            case "d":
                calendar.add(Calendar.DAY_OF_MONTH, num);
                break;
            case "w":
                calendar.add(Calendar.WEEK_OF_MONTH, num);
                break;
            case "m":
                calendar.add(Calendar.DAY_OF_MONTH, 30 * num);
                break;

        }
        return calendar;
    }

    private void setNotify() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ActionNotify.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }

    public void cancelNotify() {
        Intent intent = new Intent(this, ActionNotify.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        if (alarmManager == null){
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "EYELENS";
            String description = "Срок истек. Замените линзы!";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("EYELENS", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private int getLengthOfPeriod(int n, String period) {
        int time = 0;
        switch (period) {
            case "d":
                time = n;
                break;
            case "w":
                time = n * 7;
                break;
            case "m":
                time = n * 30;
                break;
        }
        return time;
    }

    private int getFinalDayOfPeriod(Calendar calendar) {
        return (int) Math.floor(((double) calendar.getTimeInMillis()) / 1000 / 60 / 60 / 24);
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void goBack(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelperPeriod.close();
        dbHelperUnique.close();
    }
}