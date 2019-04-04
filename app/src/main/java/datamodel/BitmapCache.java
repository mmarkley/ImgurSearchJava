package datamodel;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.HashMap;

public class BitmapCache {
    private static final String TAG = BitmapCache.class.getSimpleName();

    private static BitmapCache instance = null;

    public static BitmapCache getInstance() {
        if(null == instance) {
            instance = new BitmapCache();
        }
        return instance;
    }

    private BitmapCache() {

    }

    HashMap<String, Bitmap> bitmapCache = new HashMap<>();

    public Bitmap bitmapForKey(final String key, final String uri, final BitmapFoundCallback callback) {
        Bitmap bitmap = bitmapCache.get(key);
        Log.i(TAG, "Found " + bitmap + " for " + key);
        return bitmap;
    }

    void setBitmapForKey(String key, Bitmap bitmap) {
        bitmapCache.put(key, bitmap);
    }

    public interface BitmapFoundCallback {
        void success(Bitmap bitmap);
        void failure(String error);
    }
}
