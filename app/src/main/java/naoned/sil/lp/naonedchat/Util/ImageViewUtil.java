package naoned.sil.lp.naonedchat.Util;

import android.widget.ImageView;

/**
 * Created by julian on 31/01/2016.
 *
 * Use pattern delegation
 */
public class ImageViewUtil {

    /**
     * Resize ImageView ny getting layout parameters.
     */
    public static void resize(ImageView imageView, int height, int width) {
        imageView.getLayoutParams().height = height;
        imageView.getLayoutParams().width = width;
    }

    /**
     * Crop Image
     * @param imageView which represent your ImageView component to crop
     */
    public static void centerCrop(ImageView imageView) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }
}
