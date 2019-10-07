package reece.com.dash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;

import reece.com.dash.ui.main.onBoarding1_fragment;
import reece.com.dash.ui.main.onBoarding2_fragment;

/*
    Drive for Mobile App Assignment

    - Sort onBoarding Function
    - Add Animation

    - Create CameraLayout
    - Camera Permissions
    - Opt 1. Recording, double tap starts recording until long tapped

    - Gallery for viewing recordings/info. Time/Date
    - Submittable.

    - Opt 2. Circular encoding.
    - Speed in info.
    - Music Control.



 */



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, onBoarding1_fragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, onBoarding2_fragment.newInstance())
                .commitNow();
        return super.onTouchEvent(event);
    }
}
