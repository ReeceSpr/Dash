package reece.com.dash.Recording;


/*
Circular array buffer:
Allows an array to be added to INDEX until MAXSIZE
IF INDEX == MAXSIZE then move INDEX to 0 can replace element at INDEX.
- Increment INDEX when addding
- MAXSIZE set in constructor.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class CircularBitmapArray {
    private  byte[][] array;
    private int index;
    private int maxSize;
    private Context mContext;
    private ImageButton mImageButton;

    public CircularBitmapArray(int maxSize, Context context, ImageButton imageButton){
        array = new  byte[maxSize][];
        index = 0;
        this.maxSize = maxSize;
        this.mContext = context;
        this.mImageButton = imageButton;
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
            System.out.println("MAXSIZE");
            index=0;
            if(array[index]==null){

            } else {
                //toVideo();
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

        //For loop to convert array if you have to.
        //Convert to video
        //Write to file.
    public void toVideo() {
        String pathOG = mContext.getFilesDir() + "/output-";
       String path = mContext.getFilesDir() + "/output-1";
       String ext = ".mp4";
       int i=1;
       File file = new File(path+ext);
        try {
            while (!file.createNewFile()){
                path = pathOG + i;
                i++;
                file = new File(path+ext);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        EncodeASyncTask encorder = new EncodeASyncTask(mImageButton,array,file,mContext,index,maxSize);
       encorder.execute();
    }
}