package reece.com.dash.ui.main.Fragments;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import reece.com.dash.R;
import reece.com.dash.ui.main.MainViewModel;

public class onBoarding1_fragment extends Fragment {

    private MainViewModel mViewModel;

    public static onBoarding1_fragment newInstance() {
        return new onBoarding1_fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.onboarding1_fragment, container, false);
    }
}

