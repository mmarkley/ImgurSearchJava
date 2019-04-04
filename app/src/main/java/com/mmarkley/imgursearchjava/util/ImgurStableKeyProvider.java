package com.mmarkley.imgursearchjava.util;

import com.mmarkley.imgursearchjava.adapters.ImgurListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.widget.RecyclerView;
import datamodel.imgurdata.ImgurDataObject;

public class ImgurStableKeyProvider extends ItemKeyProvider<String> {

    private RecyclerView recyclerView;

    public ImgurStableKeyProvider(RecyclerView recyclerView) {
        super(SCOPE_MAPPED);
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public String getKey(int position) {
        String key = null;
        if(null != recyclerView) {
            ImgurListAdapter adapter = (ImgurListAdapter)recyclerView.getAdapter();
            if(null != adapter) {
                ImgurDataObject dataObject = adapter.getObjectAtIndex(position);
                key = dataObject.getId();
            }
        }
        return key;
    }

    @Override
    public int getPosition(@NonNull String key) {
        return 0;
    }
}
