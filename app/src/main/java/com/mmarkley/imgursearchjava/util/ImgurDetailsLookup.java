package com.mmarkley.imgursearchjava.util;

import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

public class ImgurDetailsLookup extends ItemDetailsLookup<String> {

    private RecyclerView recyclerView;

    public ImgurDetailsLookup(RecyclerView recyclerView) {
        super();

        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails<String> getItemDetails(@NonNull MotionEvent e) {
        return null;
    }
}
