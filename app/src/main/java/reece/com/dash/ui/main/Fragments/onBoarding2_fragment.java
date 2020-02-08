package reece.com.dash.ui.main.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import reece.com.dash.R;
import reece.com.dash.ui.main.MainViewModel;


public class onBoarding2_fragment extends Fragment {

    private MainViewModel mViewModel;

    public static onBoarding2_fragment newInstance() {
        return new onBoarding2_fragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.onboarding2_fragment, container, false);
    }
}