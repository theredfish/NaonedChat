package naoned.sil.lp.naonedchat.Util;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

/**
 * Created by julian on 31/01/2016.
 */
public class DrawableUtil {

    /**
     * Convert drawable to byte array.
     * See http://stackoverflow.com/a/4435827
     *
     * @param drawable the element to convert
     * @return byte array
     */
    public static byte[] getDrawableByte(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] drawableArray = stream.toByteArray();

        return drawableArray;
    }
}
