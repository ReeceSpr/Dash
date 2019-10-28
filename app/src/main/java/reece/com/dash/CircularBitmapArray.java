package reece.com.dash;


/*
Circular array buffer:
Allows an array to be added to INDEX until MAXSIZE
IF INDEX == MAXSIZE then move INDEX to 0 can replace element at INDEX.
- Increment INDEX when addding
- MAXSIZE set in constructor.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.net.Uri;

import org.jcodec.api.android.AndroidSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Rational;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.ByteBuffer;

import static android.content.Intent.ACTION_VIEW;

public class CircularBitmapArray {
    private  byte[][] array;
    private int index;
    private int maxSize;
    private Context mContext;

    public CircularBitmapArray(int maxSize, Context context){
        array = new  byte[maxSize][];
        index = 0;
        this.maxSize = maxSize;
        this.mContext = context;
    }

    public void addBitmap( byte[] bitmap){
        System.out.println(index);
        if(maxSize==0){
            return;
        }
        if(index<maxSize){
            array[index] = bitmap;
            index++;
        } else if(index == maxSize){
            index=0;
            if(array[index]==null){
                System.out.println("Garbage Collection");
            } else {
                byte[] b = (array[0]);
                Bitmap x = convertToBitmapFromJpeg(b);
                toVideo();
        }
            index=0;
            array[index] = bitmap;
        }
    }
    private Bitmap convertToBitmapFromImage(Image image){

        Image.Plane[] planes = image.getPlanes();

        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize+uSize+vSize];

        yBuffer.get(nv21,0,ySize);
        vBuffer.get(nv21,ySize,vSize);
        uBuffer.get(nv21,ySize+vSize,uSize);

        YuvImage yuv = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuv.compressToJpeg(new Rect(0,0,yuv.getWidth(),yuv.getHeight()), 50, out);
        byte[] imageBytes = out.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return bitmap;
    }

    private Bitmap convertToBitmapFromJpeg(byte[] imageBytes){
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return bitmap;
    }

    public void resize(){
        maxSize = index;
        index = 0;
    }

    public void toVideo() {
        SeekableByteChannel out = null;
        String path = "";
        try {
            // DEBUGING
            File file = new File(mContext.getFilesDir(), "output.mp4");
            file.createNewFile();
            path = file.getPath();

            //DEBUGGING ENDED
            out = NIOUtils.writableFileChannel(path);
            AndroidSequenceEncoder encoder = new AndroidSequenceEncoder(out, Rational.R(25, 1));
            for (int i = 0; i < maxSize; i++) {
                System.out.println(i);
                encoder.encodeImage( convertToBitmapFromJpeg(this.array[i]));
            }
            encoder.finish();
        } catch (java.io.IOException e) {
            System.out.println(e);
        } finally {
            NIOUtils.closeQuietly(out);
        }
        Intent intent = new Intent(mContext, Replay.class);
        mContext.startActivity(intent);
    }
}