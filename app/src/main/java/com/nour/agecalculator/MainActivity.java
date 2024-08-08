package com.nour.agecalculator;

import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nour.agecalculator.databinding.ActivityMainBinding;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    DatePickerDialog.OnDateSetListener dateSetListenerFromDate, dateSetListenerToDate;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String todayDate = simpleDateFormat.format(calendar.getTime());
        binding.birthTV.setText(todayDate);

        binding.fromAgeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        dateSetListenerFromDate, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        dateSetListenerFromDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String birthDate = dayOfMonth + "/" + month + "/" + year;
                binding.birthTV.setText(birthDate);
            }
        };

        binding.toAgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        dateSetListenerToDate, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        dateSetListenerToDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String todayDate = dayOfMonth + "/" + month + "/" + year;
                binding.todayTV.setText(todayDate);
            }
        };


        binding.calculateAgeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.errorTV.setText("");
                animateTextWithResult(0,  binding.yearTV);
                animateTextWithResult(0,  binding.monthTV);
                animateTextWithResult(0,  binding.dayTV);

                String birthDate =  binding.birthTV.getText().toString();
                String todayDate =  binding.todayTV.getText().toString();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

                try {
                    Date date1 = simpleDateFormat.parse(birthDate);
                    Date date2 = simpleDateFormat.parse(todayDate);

                    long startDate = date1.getTime();
                    long endDate = date2.getTime();

                    if (startDate <= endDate) {

                        Period period = new Period(startDate, endDate, PeriodType.yearMonthDay());
                        int year = period.getYears();
                        int month = period.getMonths();
                        int day = period.getDays();


                        animateTextWithResult(year,  binding.yearTV);
                        animateTextWithResult(month,  binding.monthTV);
                        animateTextWithResult(day,  binding.dayTV);


                    } else {
                        binding.errorTV.setText(R.string.birthdate_should_not_be_larger_than_today_s_date);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });


    }

    private void animateTextWithResult(int result, TextView textView) {

        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, result);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText(String.valueOf(animation.getAnimatedValue()));
            }
        });
        animator.setDuration(2000);
        animator.start();
    }
}