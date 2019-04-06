package com.mmarkley.imgursearchjava.fragments;


import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.mmarkley.imgursearchjava.MainActivity;
import com.mmarkley.imgursearchjava.R;
import com.mmarkley.imgursearchjava.adapters.ImgurListAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.selection.OnContextClickListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import datamodel.DataModel;
import datamodel.imgurdata.ImgurDataObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements OnContextClickListener {
    private static final String TAG = SearchFragment.class.getSimpleName();

    private WeakReference<Context> contextWeakReference;
    private RecyclerView imgurObjectRecycler;
    private List<ImgurDataObject> dataObjects = new ArrayList<>();

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        // Get the SearchView and set the searchable configuration
        Activity activity = getActivity();
        SearchView searchEditView = view.findViewById(R.id.main_search_view);
        imgurObjectRecycler = view.findViewById(R.id.main_search_results_view);
        if (null != activity) {
            SearchManager searchManager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
            // Assumes current activity is the searchable activity
            searchEditView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));
            searchEditView.setIconifiedByDefault(false);
            imgurObjectRecycler.setLayoutManager(new LinearLayoutManager(activity));
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof MainActivity) {
            ((MainActivity) context).setSearchFragment(this);
            contextWeakReference = new WeakReference<>(context);
        }
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        if (DataModel.getInstance().getCurrentObjectCount() > 0) {
            imgurObjectRecycler.setFocusable(true);
            imgurObjectRecycler.requestFocus();
        }

    }

    public void updateData(@NonNull List<ImgurDataObject> imageObjects) {
        List<ImgurDataObject> safeObjects = new ArrayList<>();
        boolean updateAdapter = dataObjects.size() > 0;

        if(0 == imageObjects.size()) {
            return;
        }

        for (int i = 0; i < imageObjects.size(); i++) {
            ImgurDataObject obj = imageObjects.get(i);

            if (null != obj) {
                safeObjects.add(obj);
            } else {
                Log.e(TAG, "Got null object in array at index " + i);
            }
        }

        if (updateAdapter) {
            dataObjects.addAll(safeObjects);
            RecyclerView.Adapter adapter = imgurObjectRecycler.getAdapter();
            if (null != adapter) {
                adapter.notifyDataSetChanged();
            }
        } else {
            Context context = contextWeakReference.get();
            if (null != context) {
                ImgurListAdapter adapter = new ImgurListAdapter(context, safeObjects);
                adapter.setHasStableIds(true);
                imgurObjectRecycler.setAdapter(adapter);
            }
        }
    }

    @Override
    public boolean onContextClick(@NonNull MotionEvent e) {
        return false;
    }
}
