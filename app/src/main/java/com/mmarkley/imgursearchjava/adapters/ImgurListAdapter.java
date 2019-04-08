package com.mmarkley.imgursearchjava.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mmarkley.imgursearchjava.MainActivity;
import com.mmarkley.imgursearchjava.R;
import com.mmarkley.imgursearchjava.datamodel.imgurdata.ImgurDataObject;
import com.mmarkley.imgursearchjava.datamodel.imgurdata.ImgurImage;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImgurListAdapter extends RecyclerView.Adapter<ImgurListAdapter.ImgurListAdapterViewHolder>  {
    private static final String TAG = ImgurListAdapter.class.getSimpleName();

    private LayoutInflater inflater;
    private List<ImgurDataObject> data;
    private WeakReference<Context> contextWeakReference;

    public ImgurListAdapter(@NonNull Context context, List<ImgurDataObject> strings) {
        this.inflater = LayoutInflater.from(context);
        this.data = strings;
        this.contextWeakReference = new WeakReference<>(context);
    }

    @NonNull
    @Override
    public ImgurListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.imgur_list_element, parent, false);
        Log.i(TAG, "onCreateViewHolder ");
        return new ImgurListAdapterViewHolder(view);
    }

    /**
     * Method to add new items to the data
     * @param dataObjects @ {@link List} of {@link ImgurDataObject}s
     */
    public void addData(List<ImgurDataObject> dataObjects) {
        if (null != data) {
            data.addAll(dataObjects);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ImgurListAdapterViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder " + position);
        if(null != data && position >= 0 && position <= data.size()) {
            ImgurDataObject item = data.get(position);
            Integer imageCount = item.getImagesCount();
            if (null != imageCount && imageCount > 0) {
                List<ImgurImage> images = item.getImages();
                if(0 < images.size()) {
                    ImgurImage image = images.get(0);
                    Log.i(TAG, "\tImage URL " + image.getLink());
                    String link = image.getLink();
                    if(!link.endsWith("mp4")) {
                        Picasso.get().load(link).resize(800,600).placeholder(R.drawable.loading).into(holder.imageView);
                    }
                }
            }
            if(position + 1 == data.size()) {
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.imageView.setVisibility(View.GONE);
                holder.textView.setVisibility(View.GONE);
            } else {
                holder.progressBar.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.VISIBLE);
                holder.textView.setVisibility(View.VISIBLE);
            }
            holder.position = position;
            holder.linearLayout.setTag(holder);
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
        int itemCount = 0;
        if(null != data) {
            itemCount = data.size();
        }
        return itemCount;
    }

    /**
     * Method to get object at a specific index
     *
     * @param index {@link int} with the index value
     * @return A {@link ImgurDataObject} if the index is in range, null otherwise
     */
    public ImgurDataObject getObjectAtIndex(int index) {
        ImgurDataObject dataObject = null;
        if(index >= 0 && index < data.size()) {
            dataObject = data.get(index);
        }
        Log.i(TAG, "At index: " + index + " found " + dataObject);
        return dataObject;
    }

    @Override
    public void onViewRecycled(@NonNull ImgurListAdapterViewHolder viewHolder) {
        Log.i(TAG, "onViewRecycled " + viewHolder);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Method to determine if we need to try and load more data.
     * @param view {@link View} to test with
     * @return {@link boolean} true if we are near the end, false otherwise
     */
    public boolean needMoreData(@NonNull View view) {
        ImgurListAdapterViewHolder holder = (ImgurListAdapterViewHolder)view.getTag();
        if(null != holder) {
            return (holder.position + 4) > data.size();
        }
        return false;
    }

    /**
     * Class used to hold data for our list
     */
    class ImgurListAdapterViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView textView;
        ImageView imageView;
        String idTag;
        ImgurDataObject dataObject;
        ProgressBar progressBar;
        int position;

        ImgurListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.imgur_list_element_label);
            imageView = itemView.findViewById(R.id.imgur_list_element_image);
            linearLayout = itemView.findViewById(R.id.imgur_list_element_layout);
            progressBar = itemView.findViewById(R.id.imgur_list_element_loading);
        }
    }
}
