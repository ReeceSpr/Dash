package reece.com.dash.ui.main.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import reece.com.dash.R;


/*
Plays the last recorded buffer
 */
public class ReplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replay_activity);

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



        VideoView videoView =(VideoView)findViewById(R.id.videoView);
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        final Uri uri=Uri.parse(path);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();

        Button closeButton = findViewById(R.id.button_Close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final String requestPath = path;
        Button shareButton = findViewById(R.id.button_share);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(
                        android.content.Intent.ACTION_SEND);
                shareIntent.setType("video/*");
                shareIntent.putExtra(
                        android.content.Intent.EXTRA_SUBJECT, "VIDEO CAPTURED ON DASH");
                shareIntent.putExtra(
                        android.content.Intent.EXTRA_TITLE, "VIDEO CAPTURED ON DASH");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "NOT IMPLEMENT YET"));
            }
        });
    }
}
