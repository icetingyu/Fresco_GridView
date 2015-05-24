package hsu.icesimon.facebookfresco;

/**
 * Created by Simon Hsu on 15/5/24.
 */
public class ImageItem {
    final int drawableId;
    final String imagePath;

    ImageItem(int drawableId, String imagePath)
    {
        this.drawableId = drawableId;
        this.imagePath = imagePath;
    }
}