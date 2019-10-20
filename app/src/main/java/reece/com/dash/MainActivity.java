package reece.com.dash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;

import reece.com.dash.ui.main.onBoarding1_fragment;
import reece.com.dash.ui.main.onBoarding2_fragment;
import reece.com.dash.ui.main.onBoarding3_fragment;

/*
    Drive for Mobile App Assignment

    - Sort onBoarding Function
    - Add Animation

    - Create CameraLayout
    - CameraActivity Permissions
    - Opt 1. Recording, double tap starts recording until long tapped

    - Gallery for viewing recordings/info. Time/Date
    - Submittable.

    - Opt 2. Circular encoding.
    - Speed in info.
    - Music Control.

 */



public class MainActivity extends AppCompatActivity {
    private int currentFragmentFeature;

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
        changeFragment('R');
        return super.onTouchEvent(event);
    }

    public void changeFragment(char direction){
        switch (direction){
            case 'L':
                currentFragmentFeature = currentFragmentFeature - 1;
                if(currentFragmentFeature<0){
                    currentFragmentFeature = 0;
                }
                break;
            case 'R':
                currentFragmentFeature = currentFragmentFeature + 1;
                if(currentFragmentFeature>2){
                    currentFragmentFeature = 2;
                }
                break;
        }
        switch (currentFragmentFeature){
            case 0:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, onBoarding1_fragment.newInstance())
                        .commitNow();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, onBoarding2_fragment.newInstance())
                        .commitNow();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, onBoarding3_fragment.newInstance())
                        .commitNow();
                break;
        }

    }
}
