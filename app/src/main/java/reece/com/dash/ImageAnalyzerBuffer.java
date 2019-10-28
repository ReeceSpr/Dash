package reece.com.dash;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;

import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import static android.content.Context.ACTIVITY_SERVICE;

/*
Image conversion from YUV_420_888 to Bitmap can be found
https://stackoverflow.com/questions/56772967/converting-imageproxy-to-bitmap
 */



public class ImageAnalyzerBuffer implements ImageAnalysis.Analyzer{

    private CircularBitmapArray buffer;
    ActivityManager.MemoryInfo memoryInfo;
    Context mContext;

    public ImageAnalyzerBuffer(Context mContext) {
        this.mContext=mContext;
        memoryInfo = getAvailableMemory();
        long availableMemory = memoryInfo.availMem;
        long maxSize = availableMemory/27028; //TODO: Change to a frame size or something
        maxSize = (long) (maxSize/1.25);
        this.mContext=mContext;
        buffer = new CircularBitmapArray(250, mContext);//(int)maxSize);
    }

    @Override
    public void analyze(ImageProxy image, int rotationDegrees) {
        try{
        memoryInfo = getAvailableMemory();
        //System.out.println(memoryInfo.availMem);
        ImageProxy.PlaneProxy[] planes = image.getPlanes();

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
        /*Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);*/
        buffer.addBitmap(imageBytes);
        }
        catch (OutOfMemoryError e){
            buffer.resize();
        }
    }

    private ActivityManager.MemoryInfo getAvailableMemory() {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        if (activityManager != null) {
            activityManager.getMemoryInfo(memoryInfo);
        }
        return memoryInfo;
    }

    private boolean isLowMemory() {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        if (activityManager != null) {
            activityManager.getMemoryInfo(memoryInfo);
        }
        return memoryInfo.lowMemory;
    }
}
