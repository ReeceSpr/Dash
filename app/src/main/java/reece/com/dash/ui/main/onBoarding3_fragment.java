package reece.com.dash.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import reece.com.dash.CameraActivity;
import reece.com.dash.R;


public class onBoarding3_fragment extends Fragment {

    private MainViewModel mViewModel;

    public static onBoarding3_fragment newInstance() {
        return new onBoarding3_fragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.onboarding3_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        ImageView swipeButton = getActivity().findViewById(R.id.imageViewSwipe);
        swipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoCamera();
            }
        });
        // TODO: Use the ViewModel
    }

    private void gotoCamera(){
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        startActivity(intent);
    }


}
