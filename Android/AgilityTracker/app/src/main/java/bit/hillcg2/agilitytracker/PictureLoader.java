package bit.hillcg2.agilitytracker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PictureLoader {

    //Constructor
    public PictureLoader(){
    }

    //Loads in a picture from drawable folder in a reduced scale to save memory
    public Bitmap getBitmapFromDrawable(Resources res, int resourceID){
        final int IMAGE_MAX_SIZE = 1200000; // 1.2MP

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resourceID, o);


        int scale = 1;
        while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                IMAGE_MAX_SIZE) {
            scale++;
        }

        Bitmap b = null;

        if (scale > 1)
        {
            scale--;
            // scale to max possible inSampleSize that still yields an image
            // larger than target
            o = new BitmapFactory.Options();
            o.inSampleSize = scale;
            b = BitmapFactory.decodeResource(res, resourceID, o);

            // resize to desired dimensions
            int height = b.getHeight();
            int width = b.getWidth();

            double y = Math.sqrt(IMAGE_MAX_SIZE
                    / (((double) width) / height));
            double x = (y / height) * width;

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                    (int) y, true);
            b.recycle();
            b = scaledBitmap;

            System.gc();
        }
        else
        {
            b = BitmapFactory.decodeResource(res, resourceID);
        }
        return b;
    }

    //Loads in a picture from a file path in a reduced scale to save memory
    public Bitmap getBitmap(String path) {

        final int IMAGE_MAX_SIZE = 1200000; // 1.2MP

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, o);


        int scale = 1;
        while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                IMAGE_MAX_SIZE) {
            scale++;
        }

        Bitmap b = null;

        if (scale > 1)
        {
            scale--;
            // scale to max possible inSampleSize that still yields an image
            // larger than target
            o = new BitmapFactory.Options();
            o.inSampleSize = scale;
            b = BitmapFactory.decodeFile(path, o);

            // resize to desired dimensions
            int height = b.getHeight();
            int width = b.getWidth();

            double y = Math.sqrt(IMAGE_MAX_SIZE
                    / (((double) width) / height));
            double x = (y / height) * width;

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                    (int) y, true);
            b.recycle();
            b = scaledBitmap;

            System.gc();
        }
        else
        {
            b = BitmapFactory.decodeFile(path);
        }
        return b;
    }
}
