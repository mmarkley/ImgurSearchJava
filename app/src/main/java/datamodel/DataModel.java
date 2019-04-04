package datamodel;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import datamodel.imgurdata.ImgurDataObject;
import datamodel.imgurdata.ImgurResponse;
import networking.ImgurRequest;

public class DataModel {
    private static final String TAG = DataModel.class.getSimpleName();

    private static final DataModel theInstance = new DataModel();

    public static DataModel getInstance() {
        return theInstance;
    }

    public interface DataModelCallback {
        void onSuccess(@NonNull DataCallbackSuccess success);
        void onFailure(@NonNull DataCallbackFailure failure);
    }

    HashMap<String, ImgurDataObject> objectHashMap = new HashMap<>();

    RequestQueue queue = null;
    public void init(Context context) {
        queue = Volley.newRequestQueue(context);
        queue.start();
    }

    public void cleanup() {
        if(null != queue) {
            queue.stop();
            queue.cancelAll(TAG);
        }
    }
    /**
     * Method to retrieve data from Imgur
     * @param searchString A {@link String} containing the terms to search for
     * @param callback A {@link DataModelCallback} to use to notify of success or failure
     */
    public void fetchImages(@NonNull String searchString, @NonNull final DataModelCallback callback) {
        if(null == queue) {
            return;
        }

        if(validateSearchString(searchString)) {
            ImgurRequest request = new ImgurRequest(0, searchString);

            StringRequest getRequest = new StringRequest(Request.Method.GET, request.getUrl(),
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response", response);
                            Gson gson = new Gson();
                            try {
                                ImgurResponse responseObject = gson.fromJson(response, ImgurResponse.class);
                                for(ImgurDataObject dataObject : responseObject.getData()) {
                                    objectHashMap.put(dataObject.getId(), dataObject);
                                }
                                DataCallbackSuccess success = new DataCallbackSuccess();
                                success.imgurResponse = responseObject;
                                callback.onSuccess(success);
                            } catch(Exception e) {
                                Log.e(TAG, "could not process json", e);
                            }
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            Log.d("ERROR","error => "+error.toString());
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("Authorization", "Client-ID 126701cd8332f32");

                    return params;
                }
            };
            queue.add(getRequest);
        } else {
            DataCallbackFailure failure = new DataCallbackFailure(DataCallbackErrors.DATA_CALLBACK_ERROR_INVALID_TERM, "Bad search term " + searchString);
            callback.onFailure(failure);
        }

    }

    public void fetchBitmap(final String key, String uri, final BitmapCache.BitmapFoundCallback callback) {
        ImageRequest imageRequest = new ImageRequest(uri, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                BitmapCache.getInstance().setBitmapForKey(key, bitmap);
                callback.success(bitmap);
            }
        }, 0, 0, null, null);
        queue.add(imageRequest);
    }

    /**
     * Method to test the search terms to verify that they are SFW
     * @param searchString The {@link String} to validate
     * @return {@link boolean} true if the searchString is safe, {@link boolean} false otherwise
     */
    private boolean validateSearchString(String searchString) {
        boolean isSafeForWork = true;

        // TODO: flesh this out
        return isSafeForWork;
    }
}
