package eu.kudan.rahasianusantara;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by fauza on 6/8/2017.
 */

public class ImageRequest extends AsyncTask<String, Void, Bitmap> {

    ImageView bmImage;

    public ImageRequest(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null, outBitmap = null;
        int outHeight, outWidth;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
            outBitmap = resizeBitmap(mIcon11, 500);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return outBitmap;
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int size){
        int outHeight, outWidth;
        int inHeight = bitmap.getHeight();
        int inWidth = bitmap.getWidth();
        if (inWidth>inHeight){
            outWidth = size;
            outHeight = (inHeight * size) / inWidth;
        }else{
            outHeight = size;
            outWidth = (inWidth * size) / inHeight;
        }
        return Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, false);
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
