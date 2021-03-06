package reece.com.dash.ui.main.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import reece.com.dash.R;
import reece.com.dash.ui.main.Fragments.onBoarding1_fragment;
import reece.com.dash.ui.main.Fragments.onBoarding2_fragment;
import reece.com.dash.ui.main.Fragments.onBoarding3_fragment;

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



public class OnboardingActivity extends AppCompatActivity {
    /**
     * Number of onBoardingPages
     */
    private static final int NUM_PAGES = 3;

    private int REQUEST_CODE_PERMISSIONS = 10; //arbitrary number, can be changed accordingly
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};//,"android.permission.WRITE_EXTERNAL_STORAGE"}; //array w/ permissions from manifest


    ImageView[] dots =  new ImageView[3];

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager2 viewPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStateAdapter pagerAdapter;

    private View.OnClickListener getStartedClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_activity);


        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if(position == 2) {
                    createGetStartedListener();
                    findViewById(R.id.getStartedButton).setOnClickListener(getStartedClickListener);
                }
                dotChange(position);
                super.onPageSelected(position);
            }
        });

        dots[0] = findViewById(R.id.dot0);
        dots[1] = findViewById(R.id.dot1);
        dots[2] = findViewById(R.id.dot2);


    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {

            switch (position){
                case 0:
                    return  new onBoarding1_fragment();
                case 1:
                    return new onBoarding2_fragment();
                default:
                    return new onBoarding3_fragment();
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }

    }

    public void dotChange(int pos){
        switch (pos){
            case 0:
                dots[0].setImageResource(R.drawable.ic_buttonon);
                dots[1].setImageResource(R.drawable.ic_buttonoff);
                dots[2].setImageResource(R.drawable.ic_buttonoff);
                break;
            case 1:
                dots[1].setImageResource(R.drawable.ic_buttonon);
                dots[0].setImageResource(R.drawable.ic_buttonoff);
                dots[2].setImageResource(R.drawable.ic_buttonoff);
                break;
            case 2:
                dots[2].setImageResource(R.drawable.ic_buttonon);
                dots[0].setImageResource(R.drawable.ic_buttonoff);
                dots[1].setImageResource(R.drawable.ic_buttonoff);
                break;
        }

    }


    public void createGetStartedListener (){
        getStartedClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissions();
            }
        };
    }

    public void permissions(){
        if(allPermissionsGranted()){
            openCamera();
        } else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private boolean allPermissionsGranted(){
        //check if req permissions have been granted
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //start camera when permissions have been granted otherwise exit app
        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){
                openCamera();
            } else{
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                //finish();
            }
        }
    }
    public void openCamera(){
        startActivity(new Intent(this , CameraActivity.class));
    }
}