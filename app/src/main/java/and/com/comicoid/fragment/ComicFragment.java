package and.com.comicoid.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import and.com.comicoid.R;
import and.com.comicoid.activity.MainActivity;
import and.com.comicoid.adapter.GalleryAdapter;
import and.com.comicoid.config.ApiClient;
import and.com.comicoid.model.Image;
import and.com.comicoid.network.MarvelAsyncTaskLoader;
import and.com.comicoid.touch.RecyclerTouchListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dell on 29-05-2017.
 */

public class ComicFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Image>>{

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;

    private List<Image> imageList;
    private GalleryAdapter galleryAdapter;

    private static final int LOADER_ID = 22;

    private String KEY_LAYOUT_MANAGER = "list_state";
    private static final int SPAN_COUNT = 2;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;

    private ConnectivityManager connectivityManager;
    private NetworkInfo isConnectedOrConnecting;

    public ComicFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState.getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        LoaderManager loaderManager = getLoaderManager();

        imageList = new ArrayList<>();
        galleryAdapter = new GalleryAdapter(getContext(),imageList);
        mLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(galleryAdapter);

        connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        isConnectedOrConnecting = connectivityManager.getActiveNetworkInfo();

        if (isConnectedOrConnecting != null && isConnectedOrConnecting.isConnectedOrConnecting()) {
            loaderManager.initLoader(LOADER_ID,null,this);
        } else {
            Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
        }

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }
            @Override
            public void onLongClick(View view, int position) {}
        }));

        imageList.clear();
        loaderManager.restartLoader(LOADER_ID,null,this).forceLoad();

        return rootView;
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getResources().getString(R.string.nav_com));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public Loader<List<Image>> onCreateLoader(int id, Bundle args) {
        ApiClient apiClient = new ApiClient();
        apiClient.creatUrl();
        return new MarvelAsyncTaskLoader(getContext(), MainActivity.TAG_CO,apiClient.getTs(),apiClient.getApiKey(),apiClient.getHash());
    }

    @Override
    public void onLoadFinished(Loader<List<Image>> loader, List<Image> data) {
        imageList = data;
        galleryAdapter = new GalleryAdapter(getContext(),data);
        recyclerView.setAdapter(galleryAdapter);
        mLayoutManager.onSaveInstanceState();
    }

    @Override
    public void onLoaderReset(Loader<List<Image>> loader) {

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }
}
