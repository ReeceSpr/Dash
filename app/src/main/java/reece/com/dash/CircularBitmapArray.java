package reece.com.dash;


/*
Circular array buffer:
Allows an array to be added to INDEX until MAXSIZE
IF INDEX == MAXSIZE then move INDEX to 0 can replace element at INDEX.
- Increment INDEX when addding
- MAXSIZE set in constructor.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class CircularBitmapArray {
    private  byte[][] array;
    private int index;
    private int maxSize;

    public CircularBitmapArray(int maxSize){
        array = new  byte[maxSize][];
        index = 0;
        this.maxSize = maxSize;
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
}
