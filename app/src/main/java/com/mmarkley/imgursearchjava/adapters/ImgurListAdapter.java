package com.mmarkley.imgursearchjava.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mmarkley.imgursearchjava.MainActivity;
import com.mmarkley.imgursearchjava.R;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import datamodel.BitmapCache;
import datamodel.DataModel;
import datamodel.imgurdata.ImgurDataObject;
import datamodel.imgurdata.ImgurImage;

public class ImgurListAdapter extends RecyclerView.Adapter<ImgurListAdapter.ImgurListAdapterViewHolder> implements BitmapCache.BitmapFoundCallback {
    private static final String TAG = ImgurListAdapter.class.getSimpleName();

    private LayoutInflater inflater;
    private List<ImgurDataObject> data;
    private WeakReference<Context> contextWeakReference;
    private int retrieveCount = 0;
    private RecyclerView recyclerView;

    public ImgurListAdapter(@NonNull Context context, List<ImgurDataObject> strings) {
        this.inflater = LayoutInflater.from(context);
        this.data = strings;
        this.contextWeakReference = new WeakReference<>(context);
    }

    @NonNull
    @Override
    public ImgurListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.imgur_list_element, parent, false);
        return new ImgurListAdapterViewHolder(view);
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(@NonNull ImgurListAdapterViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder " + position);
        if(null != data && position > 0 && position <= data.size()) {
            ImgurDataObject item = data.get(position);
            Integer imageCount = item.getImagesCount();
            if (null != imageCount && imageCount > 0) {
                List<ImgurImage> images = item.getImages();
                for (ImgurImage image : images) {
//                    Log.i(TAG, "\tImage URL " + image.getLink());
                    String link = image.getLink();
                    if(!link.endsWith("mp4")) {
                        Bitmap bitmap = BitmapCache.getInstance().bitmapForKey(item.getId(), image.getLink(), this);
                        if (null != bitmap) {
                            holder.imageView.setImageBitmap(bitmap);
                        } else {
                            retrieveCount++;
                            Log.i(TAG, "retrieveCount " + retrieveCount);
                            DataModel.getInstance().fetchBitmap(item.getId(), image.getLink(), this);
                            Context context = contextWeakReference.get();
                            if (null != context) {
                                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.loading);
                                holder.imageView.setImageBitmap(bitmap);
                            }
                        }
                    } else {
                        Log.i(TAG, "Won't load image " + link);
                    }
                    break;
                }
            }
            holder.dataObject = item;
            holder.imageView.setOnClickListener(clickListener);
            holder.textView.setText(item.getTitle());
            holder.idTag = item.getId();
            holder.textView.setTag(holder);
            holder.imageView.setTag(holder);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImgurListAdapterViewHolder holder = (ImgurListAdapterViewHolder)v.getTag();

            if(null != holder) {
                Context context = contextWeakReference.get();
                if(context instanceof MainActivity) {
                    ((MainActivity)context).launchFullScreenImageActivity(holder.dataObject);
                }
            }

        }
    };
    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    /**
     * Method to get object at a specific index
     * @param index {@link int} with the index value
     * @return A {@link ImgurDataObject} if the index is in range, null otherwise
     */
    public ImgurDataObject getObjectAtIndex(int index) {
        ImgurDataObject dataObject = null;
        if(index >= 0 && index < data.size()) {
            dataObject = data.get(index);
        }
        return dataObject;
    }

    @Override
    public void success(Bitmap bitmap) {
        retrieveCount--;
        Log.i(TAG, "onSuccess retrieveCount " + retrieveCount);
        if(retrieveCount == 0) {
            notifyDataSetChanged();
        }
    }

    @Override
    public void failure(String error) {

    }

    class ImgurListAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        String idTag;
        ImgurDataObject dataObject;

        public ImgurListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.imgur_list_element_label);
            imageView = itemView.findViewById(R.id.imgur_list_element_image);
        }
    }
}
