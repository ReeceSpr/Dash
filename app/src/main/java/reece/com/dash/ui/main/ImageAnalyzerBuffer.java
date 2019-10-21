package reece.com.dash.ui.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.SystemClock;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/*
Image conversion from YUV_420_888 to Bitmap can be found
https://stackoverflow.com/questions/56772967/converting-imageproxy-to-bitmap
 */



public class ImageAnalyzerBuffer implements ImageAnalysis.Analyzer{
    @Override
    public void analyze(ImageProxy image, int rotationDegrees) {
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
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);


    }
}
