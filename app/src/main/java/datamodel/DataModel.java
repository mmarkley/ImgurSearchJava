package datamodel;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import datamodel.imgurdata.ImgurDataObject;
import datamodel.imgurdata.ImgurImage;
import datamodel.imgurdata.ImgurResponse;
import networking.ImgurRequest;

public class DataModel {
    private static final String TAG = DataModel.class.getSimpleName();

    private static final DataModel theInstance = new DataModel();

    public static DataModel getInstance() {
        return theInstance;
    }

    public interface DataModelCallback {
        void onSuccess(@NonNull List<ImgurDataObject> imgurDataObjectList);
        void onFailure(@NonNull DataCallbackFailure failure);
    }

    private HashMap<String, ImgurDataObject> objectHashMap = new HashMap<>();

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
     * Method to get all the currently loaded ImgurDataObjects
     * @return A {@link List} containing the objects
     */
    public List<ImgurDataObject> getCurrentObjects() {
        List<ImgurDataObject> objectList = new ArrayList<>();
        if(objectHashMap.keySet().size() > 0) {
            objectList = new ArrayList<>(objectHashMap.values());
        }
        return objectList;
    }

    /**
     * Method to get the number of {@link ImgurDataObject}s that have been loaded
     * @return an {@link int} containing the count
     */
    public int getCurrentObjectCount() {
        return objectHashMap.size();
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
                            Gson gson = new Gson();
                            try {
                                ImgurResponse responseObject = gson.fromJson(response, ImgurResponse.class);
                                List<ImgurDataObject> newObjects = filterResponse(responseObject);

                                callback.onSuccess(newObjects);
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
                public Map<String, String> getHeaders() {
                    Map<String, String>  params = new HashMap<>();
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

    private List<ImgurDataObject> filterResponse(@NonNull ImgurResponse imgurResponse) {
        List<ImgurDataObject> resultsList = new ArrayList<>();
        for(ImgurDataObject dataObject : imgurResponse.getData()) {
            // Only add new items,
            if(null == dataObject) {
                continue;
            }
            if(!objectHashMap.containsKey(dataObject.getId())) {
                // Skip NSFW items
                if(dataObject.getNsfw()) {
                    continue;
                }
                Integer imageCount = dataObject.getImagesCount();
                boolean addToList = false;

                if (null != imageCount && imageCount > 0) {
                    List<ImgurImage> images = dataObject.getImages();
                    for (ImgurImage image : images) {
                        String link = image.getLink();
                        // For now, we're going to exclude videos. Could be a future
                        // enhancement.
                        if(null == link || !link.endsWith("mp4")) {
                            addToList = true;
                            break;
                        }
                    }
                }
                if(addToList) {
                    resultsList.add(dataObject);
                    objectHashMap.put(dataObject.getId(), dataObject);
                }
            }
        }
        return resultsList;
    }

    /**
     * Method to test the search terms to verify that they are SFW
     * @param searchString The {@link String} to validate
     * @return {@link boolean} true if the searchString is safe, {@link boolean} false otherwise
     */
    private boolean validateSearchString(String searchString) {

        // TODO: flesh this out
        return true;
    }
}
