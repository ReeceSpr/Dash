package reece.com.dash.Recording;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;

import org.jcodec.api.android.AndroidSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Rational;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


import reece.com.dash.ui.main.Activities.ReplayActivity;

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
        long startTime = System.currentTimeMillis();
        System.out.println("ENCODING..." + index);
        SeekableByteChannel out = null;
        String path = "";
        /*try {
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
        /*Intent intent = new Intent(mContext, ReplayActivity.class);
        mContext.startActivity(intent);*/
        writeImages();
        convertImg_to_vid();
        System.out.println("DONE ENCODING!");

        String filePath = file.getAbsolutePath();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.DATA, filePath);
        mContext.getContentResolver().insert(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);

        //mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        long endTime = System.currentTimeMillis();
        System.out.println("ENCODING OF SIZE: " + array.length + " FRAMES. TIME TAKEN:" + (startTime-endTime/1000) + "sec");
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
                Intent intent = new Intent(mContext, ReplayActivity.class);
                intent.putExtra("fpath", file.getAbsolutePath());
                mContext.startActivity(intent);
            }
        });
    }

    private Bitmap convertToBitmapFromJpeg(byte[] imageBytes){
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return bitmap;
    }

    void convertImg_to_vid()
    {
        Process chperm;
        try {
            chperm=Runtime.getRuntime().exec("su");
            DataOutputStream os =
                    new DataOutputStream(chperm.getOutputStream());

            os.writeBytes("ffmpeg -f image2 -i img%d.jpg /tmp/a.mpg\n");
            os.flush();

            chperm.waitFor();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    String writeImages() {
        String filename = "image";
        ContextWrapper cw = new ContextWrapper(mContext);
        File directory = cw.getDir("image2", Context.MODE_PRIVATE);
        for (int i = 0; i < array.length; i++) {
            Bitmap bitmapImage = convertToBitmapFromJpeg(array[i]);
            // path to /data/data/yourapp/app_data/imageDir
            // Create imageDir
            File mypath = new File(directory, ((String) filename + i));

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            }
            return directory.getAbsolutePath();
    }
}
