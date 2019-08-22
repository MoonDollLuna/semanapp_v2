package luna.semanapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements Runnable {

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
        currentDateTV = (TextView) findViewById(R.id.fecha);

        //IDEA: QUE LO UNICO QUE COMPRUEBE CADA SEGUNDO SEA LA FECHA, Y SI ES DISTINTA A LA INICIAL ENTONCES ES CUANDO ACTUALIZA TODO LO DEMAS

        //Get current date and update the screen
        

        //Initiate the thread a second later
        handler.postDelayed(this, 1000);
    }

    @Override
    public void run() {

        //Relaunch the thread in a second
        handler.postDelayed(this, 1000);

    }

    private void updateScreen(){

    }
}
