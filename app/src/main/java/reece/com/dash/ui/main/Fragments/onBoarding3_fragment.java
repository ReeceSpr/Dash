package reece.com.dash.ui.main.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import reece.com.dash.R;
import reece.com.dash.ui.main.MainViewModel;


public class onBoarding3_fragment extends Fragment {

    private MainViewModel mViewModel;

    public static onBoarding3_fragment newInstance() {
        return new onBoarding3_fragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.onboarding3_fragment, container, false);
    }
}