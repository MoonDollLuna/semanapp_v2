package luna.semanapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements Runnable {

    //Handler used to run other threads
    Handler handler;

    //Original date, used as reference
    GregorianCalendar anniversary = new GregorianCalendar(2015,4,15);
    //Last current date (used to know if it is necessary to update everything)
    GregorianCalendar currentDate;

    //Elements on the screen
    TextView currentDateTV, weeksTV, totalTimeTV, specialTV;
    ImageView backgroundIV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Identify relevant elements on the screen
        currentDateTV = findViewById(R.id.fecha);
        weeksTV = findViewById(R.id.semanas);
        totalTimeTV = findViewById(R.id.fechaTotal);
        specialTV = findViewById(R.id.special);
        backgroundIV = findViewById(R.id.background);

        //Get current date and update the screen
        currentDate = new GregorianCalendar();
        updateScreen();

        //Initiate the thread a second later
        handler = new Handler();
        handler.postDelayed(this, 1000);
    }

    @Override
    public void run() {

        //Relaunch the thread in a second
        handler.postDelayed(this, 1000);

        //Get the new date
        GregorianCalendar newDate = new GregorianCalendar();

        //If the date is different, update everything
        if (!compareDates(newDate, currentDate)){
            currentDate = newDate;
            updateScreen();
        }

    }

    //Takes two dates and compares if the year, month and day are equal
    private boolean compareDates(GregorianCalendar date1, GregorianCalendar date2){
        return date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)
                && date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH)
                && date1.get(Calendar.DAY_OF_MONTH) == date2.get(Calendar.DAY_OF_MONTH);
    }

    //Returns the number of days between two dates
    public static int daysBetween(Calendar day1, Calendar day2){
        Calendar dayOne = (Calendar) day1.clone(),
                dayTwo = (Calendar) day2.clone();

        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
            return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
        } else {
            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
                //swap them
                Calendar temp = dayOne;
                dayOne = dayTwo;
                dayTwo = temp;
            }
            int extraDays = 0;

            int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);

            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
                dayOne.add(Calendar.YEAR, -1);
                // getActualMaximum() important for leap years
                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
            }

            return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays ;
        }
    }

    private void updateScreen(){

        //Day, month and year
        int day, month, year;
        day = currentDate.get(Calendar.DAY_OF_MONTH);
        month = currentDate.get(Calendar.MONTH);
        year = currentDate.get(Calendar.YEAR);

        //Months (for strings)
        String[] months = {
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };

        //Update the currently displayed date
        String date =  day + " de " + months[month] + " de " + year;
        currentDateTV.setText(date);

        //Update the number of weeks on screen
        updateWeeks(daysBetween(anniversary, currentDate));
        //Update the number of years, months, weeks and days on screen
        updateDate();

        //Check if there is a special date
        specialDate();

    }

    private void updateWeeks(int totalDays){

        //Get the number of weeks and the number of days
        int weeks = totalDays / 7;
        int days = totalDays % 7;

        //String to be set
        String setText = "";

        //POSSIBLE CASES:
        //No weeks
        if (weeks == 0){
            //1 day
            if (days == 1) setText = "★ 1 día";
                //More than 1 day
            else setText = "★ " + days + " días";
        }
        //1 week
        else if (weeks == 1){
            //0 days
            if (days == 0) setText = "★ 1 semana";
                //1 day
            else if (days == 1) setText = "★ 1 semana y 1 día";
                //More than 1 day
            else setText = "★ 1 semana y " + days + " días";
        }
        //More than 1 week
        else{
            //0 days
            if (days == 0) setText = "★ " + weeks + " semanas";
                //1 day
            else if (days == 1) setText = "★ " + weeks + " semanas y 1 día";
                //More than 1 day
            else setText = "★ " + weeks + " semanas y " + days + " días";
        }

        //Set the text
        weeksTV.setText(setText);
    }

    private void updateDate(){

        //Create clones of each calendar to avoid terrible problems
        Calendar calendarAn = (Calendar) anniversary.clone();
        Calendar calendarNew = (Calendar) currentDate.clone();

        //Necessary variables
        int years, months, weeks, days;
        years = months = weeks = days = 0;

        //Increase month by month until both are in the same year and month

        //HOTFIX
        calendarAn.add(Calendar.MONTH,1);

        while(calendarAn.compareTo(calendarNew) < 0){
            calendarAn.add(Calendar.MONTH,1);
            months++;
            if(months >= 12){
                months = 0;
                years++;
            }
        }

        //Once both are in the same month, get the number of days between dates
        calendarAn = (Calendar) anniversary.clone();
        calendarAn.set(Calendar.YEAR, calendarNew.get(Calendar.YEAR));
        calendarAn.set(Calendar.MONTH, calendarNew.get(Calendar.MONTH));

        days = daysBetween(calendarAn, calendarNew);
        //HOTFIX
        days--;
        weeks = days / 7;
        days = days % 7;

        //Prepare the string
        String text = "★ ";

        //Count how many elements will be in the string
        int elements = 0;
        if (years != 0) elements++;
        if (months != 0) elements++;
        if (weeks != 0) elements++;
        if (days != 0) elements++;

        //Years
        if(years != 0){
            text = text.concat(years + " año");
            if(years != 1) text = text.concat("s");
            if(elements > 2) text = text.concat(", ");
            else if (elements == 2) text = text.concat(" y ");
            elements--;
        }
        //Months
        if(months != 0){
            text = text.concat(months + " mes");
            if(months != 1) text = text.concat("es");
            if(elements > 2) text = text.concat(", ");
            else if (elements == 2) text = text.concat(" y ");
            elements--;
        }

        //Weeks
        if(weeks != 0){
            text = text.concat(weeks + " semana");
            if(weeks != 1) text = text.concat("s");
            if(elements == 2) text = text.concat(" y ");
            elements--;
        }

        //Days
        if(days != 0){
            text = text.concat(days + " día");
            if(days != 1) text = text.concat("s");
            elements--;
        }

        //Set the text
        totalTimeTV.setText(text);
    }

    private void specialDate(){

        //Check by order of priority

        //Anniversary
        if(currentDate.get(Calendar.MONTH) == 4 && currentDate.get(Calendar.DAY_OF_MONTH) == 15){
            specialTV.setText("☽¡Feliz aniversario!☽");
        }
        //Month
        else if(currentDate.get(Calendar.DAY_OF_MONTH) == 15){
            specialTV.setText("☽¡Otro mes juntas!☽");
        }
        //Week
        else if(currentDate.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
            specialTV.setText("☽¡Otra semana juntas!☽");
        }
        //Nothing
        else{
            specialTV.setVisibility(View.INVISIBLE);
            backgroundIV.setVisibility(View.INVISIBLE);
        }

    }
}
