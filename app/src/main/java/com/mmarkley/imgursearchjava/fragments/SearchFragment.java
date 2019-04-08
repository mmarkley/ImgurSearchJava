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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mmarkley.imgursearchjava.MainActivity;
import com.mmarkley.imgursearchjava.R;
import com.mmarkley.imgursearchjava.adapters.ImgurListAdapter;
import com.mmarkley.imgursearchjava.datamodel.DataModel;
import com.mmarkley.imgursearchjava.datamodel.imgurdata.ImgurDataObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.selection.OnContextClickListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements OnContextClickListener {
    private static final String TAG = SearchFragment.class.getSimpleName();

    private WeakReference<MainActivity> activityWeakReference;
    private RecyclerView imgurObjectRecycler;
    private SearchView searchEditView;
    private List<ImgurDataObject> dataObjects = new ArrayList<>();
    private String filterType;
    private Spinner spinner;
    private RecyclerView.LayoutManager layoutManager;

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
        searchEditView = view.findViewById(R.id.main_search_view);
        imgurObjectRecycler = view.findViewById(R.id.main_search_results_view);
        imgurObjectRecycler.addOnScrollListener(scrollListener);
        spinner = view.findViewById(R.id.main_filter_spinner);
        if (null != activity) {
            SearchManager searchManager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
            // Assumes current activity is the searchable activity
            searchEditView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));
            searchEditView.setIconifiedByDefault(false);
            layoutManager = new LinearLayoutManager(activity);
            imgurObjectRecycler.setLayoutManager(layoutManager);
            if (null != spinner) {
                ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
                        activity,
                        R.array.filter_labels,
                        android.R.layout.simple_spinner_item);
                spinner.setAdapter(arrayAdapter);
                filterType = getString(R.string.filter_first_selection);
                spinner.setSelection(arrayAdapter.getPosition(filterType));
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        onSpinnerSelected();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                });
            }

        }
        return view;
    }

    private void onSpinnerSelected() {
        String newFilter = (String)spinner.getSelectedItem();
        if(newFilter.equals(filterType)) {
            return;
        }
        filterType = newFilter;
        String queryString = searchEditView.getQuery().toString();
        searchEditView.setQuery(queryString, true);
    }

    public String getFilterType() {
        return filterType;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof MainActivity) {
            ((MainActivity) context).setSearchFragment(this);
            activityWeakReference = new WeakReference<>((MainActivity)context);
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
            MainActivity activity = activityWeakReference.get();
            if (null != activity) {
                ImgurListAdapter adapter = new ImgurListAdapter(activity, safeObjects);
                adapter.setHasStableIds(true);
                imgurObjectRecycler.setAdapter(adapter);
            }
        }
    }

    @Override
    public boolean onContextClick(@NonNull MotionEvent e) {
        return false;
    }

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int childCount = recyclerView.getChildCount();
            if(0 < childCount) {
                View child = recyclerView.getChildAt(childCount - 1);
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                if(adapter instanceof ImgurListAdapter) {
                    if(((ImgurListAdapter) adapter).needMoreData(child)) {
                        MainActivity activity = activityWeakReference.get();
                        if(null != activity) {
                            activity.nextPage(searchEditView.getQuery().toString());
                        }
                    }
                }
            }
        }
    };
}
