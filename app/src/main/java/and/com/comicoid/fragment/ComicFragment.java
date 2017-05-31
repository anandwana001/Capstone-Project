package and.com.comicoid.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import and.com.comicoid.R;
import and.com.comicoid.activity.MainActivity;
import and.com.comicoid.adapter.GalleryAdapter;
import and.com.comicoid.config.ApiClient;
import and.com.comicoid.model.Image;
import and.com.comicoid.network.MarvelAsyncTaskLoader;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dell on 29-05-2017.
 */

public class ComicFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Image>> {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.no_fav)
    TextView noFav;

    private List<Image> imageList;
    private GalleryAdapter galleryAdapter;

    private static final int LOADER_ID = 22;
    private Parcelable mListState;
    private GridLayoutManager mLayoutManager;
    private String LIST_STATE_KEY = "list_state";

    private ConnectivityManager connectivityManager;
    private NetworkInfo isConnectedOrConnecting;

    public ComicFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        LoaderManager loaderManager = getActivity().getSupportLoaderManager();

        imageList = new ArrayList<>();
        galleryAdapter = new GalleryAdapter(getContext(), imageList);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(galleryAdapter);

        connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        isConnectedOrConnecting = connectivityManager.getActiveNetworkInfo();

        if (isConnectedOrConnecting != null && isConnectedOrConnecting.isConnectedOrConnecting()) {
            getLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            noFav.setVisibility(View.VISIBLE);
            noFav.setText(getContext().getString(R.string.no_internet));
        }
        imageList.clear();

        if (isConnectedOrConnecting != null && isConnectedOrConnecting.isConnectedOrConnecting()) {
            if (savedInstanceState != null)
                mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
            else
                loaderManager.restartLoader(LOADER_ID, null, this).forceLoad();
        } else {
            noFav.setVisibility(View.VISIBLE);
            noFav.setText(getContext().getString(R.string.no_internet));
        }

        return rootView;
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
        return new MarvelAsyncTaskLoader(getContext(), MainActivity.TAG_CO, apiClient.getTs(), apiClient.getApiKey(), apiClient.getHash());
    }

    @Override
    public void onLoadFinished(Loader<List<Image>> loader, List<Image> data) {
        if(data.size() == 0){
            noFav.setVisibility(View.VISIBLE);
            noFav.setText(getContext().getString(R.string.no_com));
        }else{
            imageList = data;
            galleryAdapter = new GalleryAdapter(getContext(), data);
            recyclerView.setAdapter(galleryAdapter);
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Image>> loader) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, mListState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
        getActivity().setTitle(MainActivity.TAG_CO);
    }
}
