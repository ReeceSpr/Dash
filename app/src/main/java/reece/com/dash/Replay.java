package reece.com.dash;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;

public class Replay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String path;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                path= null;
            } else {
                path= extras.getString("fpath");
            }
        } else {
            path= (String) savedInstanceState.getSerializable("fpath");
        }



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);
        VideoView videoView =(VideoView)findViewById(R.id.videoView);
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        //String path = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "MyVideo.3gp");
        Uri uri=Uri.parse(path);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
    }
}
