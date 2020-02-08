package reece.com.dash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import reece.com.dash.ui.main.OnSwipeTouchListener;
import reece.com.dash.ui.main.onBoarding1_fragment;
import reece.com.dash.ui.main.onBoarding2_fragment;
import reece.com.dash.ui.main.onBoarding3_fragment;

/*
    Drive for Mobile App Assignment
    ID: 17046764 and 13049535
    Name: Reece and Josh


    x Sort onBoarding Function
    x Add Animation

    x Create CameraLayout
    x CameraActivity Permissions
    - Opt 1. Recording, double tap starts recording until long tapped

    - Gallery for viewing recordings/info. Time/Date
    - Submittable.

    X Opt 2. Circular encoding.
    - Speed in info.
    - Music Control.

 */



public class MainActivity extends AppCompatActivity {
    private int currentFragmentFeature;

    /*
    Creates the layout
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, onBoarding1_fragment.newInstance())
                    .commitNow();
        }
        FrameLayout cont = findViewById(R.id.container);
        cont.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                changeFragment('R');
            }

            @Override
            public void onSwipeUp() {
                if (currentFragmentFeature == 2) {
                    Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onSwipeRight() {
                changeFragment('L');

            }
        });
    }

    /*
    Changes the fragment inside the activity.
     */
    public void changeFragment(char direction){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        switch (direction){
            case 'L':
                currentFragmentFeature = currentFragmentFeature - 1;
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                if(currentFragmentFeature<0){
                    currentFragmentFeature = 0;
                    return;
                }
                break;
            case 'R':
                currentFragmentFeature = currentFragmentFeature + 1;

                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                if(currentFragmentFeature>2){
                    currentFragmentFeature = 2;
                    return;
                }
                break;
        }


        switch (currentFragmentFeature){
            case 0:
                transaction.replace(R.id.container,  onBoarding1_fragment.newInstance());

                /*getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, onBoarding1_fragment.newInstance())
                        .commitNow();*/
                break;
            case 1:
                transaction.replace(R.id.container,  onBoarding2_fragment.newInstance());
                /*getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, onBoarding2_fragment.newInstance())
                        .commitNow();*/
                break;
            case 2:
                transaction.replace(R.id.container,  onBoarding3_fragment.newInstance());
                /*getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, onBoarding3_fragment.newInstance())
                        .commitNow();*/
                break;
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
