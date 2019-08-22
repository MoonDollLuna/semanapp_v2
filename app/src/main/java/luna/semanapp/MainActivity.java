package luna.semanapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements Runnable {

    //Months (for strings)
    private String[] months = {
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    //Handler used to run other threads
    Handler handler;

    //Original date, used as reference
    GregorianCalendar anniversary = new GregorianCalendar(2015,4,15);
    //Last current date (used to know if it is necessary to update everything)
    GregorianCalendar currentDate;

    //Elements on the screen
    TextView currentDateTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Identify relevant elements on the screen
        currentDateTV = findViewById(R.id.fecha);

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

    private void updateScreen(){

        //Day, month and year
        int day, month, year;
        day = currentDate.get(Calendar.DAY_OF_MONTH);
        month = currentDate.get(Calendar.MONTH);
        year = currentDate.get(Calendar.YEAR);

        //Update the currently displayed date
        String date =  day + " de " + months[month] + " de " + year;
        currentDateTV.setText(date);

    }
}
