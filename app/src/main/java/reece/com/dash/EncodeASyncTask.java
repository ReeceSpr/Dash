package reece.com.dash;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.jcodec.api.android.AndroidSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Rational;

import java.io.File;
import java.lang.ref.WeakReference;

/*
Base Code From Assignment 1
 */
public class EncodeASyncTask extends AsyncTask<Void, Void, Bitmap> {


    ImageButton imageButton;
    private  byte[][] array;
    File file;
    Context mContext;
    int index;
    int maxSize;

    public EncodeASyncTask(ImageButton imageButton, byte[][] array, File file, Context context, int index, int maxsize) {
        this.imageButton = imageButton;
        this.array = array;
        this.file = file;
        this.mContext = context;
        this.index =index;
        this.maxSize = maxsize;
    }


    @Override
    protected Bitmap doInBackground(Void... voids) {
        System.out.println("ENCODING..." + index);
        SeekableByteChannel out = null;
        String path = "";
        try {
            path = file.getPath();

            //DEBUGGING ENDED
            out = NIOUtils.writableFileChannel(path);
            AndroidSequenceEncoder encoder = new AndroidSequenceEncoder(out, Rational.R(20, 1));

            if(index!=maxSize && array[index+1]==null){
                //Handle
                for(int i=0; i<index; i++){
                    encoder.encodeImage(convertToBitmapFromJpeg(this.array[i]));
                }
            } else {
                for (int i = index + 1; i < maxSize; i++) {
                    encoder.encodeImage(convertToBitmapFromJpeg(this.array[i]));
                }
                for (int i = 0; i < index; i++) {
                    encoder.encodeImage(convertToBitmapFromJpeg(this.array[i]));
                }
            }
            encoder.finish();
        } catch (java.io.IOException e) {
            System.out.println(e);
        } finally {
            NIOUtils.closeQuietly(out);
        }
        //imgview.setBackgroundColor(Color.RED);
        /*Intent intent = new Intent(mContext, Replay.class);
        mContext.startActivity(intent);*/
        System.out.println("DONE ENCODING!");

        String filePath = file.getAbsolutePath();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.DATA, filePath);
        mContext.getContentResolver().insert(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);

        //mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        return convertToBitmapFromJpeg(this.array[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(bitmap==null){
            return;
        }
        imageButton.setImageBitmap(bitmap);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Replay.class);
                intent.putExtra("fpath", file.getAbsolutePath());
                mContext.startActivity(intent);
            }
        });
    }

    private Bitmap convertToBitmapFromJpeg(byte[] imageBytes){
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return bitmap;
    }
}
