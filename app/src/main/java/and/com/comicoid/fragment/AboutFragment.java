package and.com.comicoid.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import and.com.comicoid.R;
import and.com.comicoid.activity.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AboutFragment extends Fragment {

    @BindView(R.id.webView)
    WebView webView;
    Unbinder unbinder;

    public AboutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        InputStream is;
        String htmlData = "";
        try {
            is = getContext().getAssets().open("html/about.html");
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while( (line=r.readLine()) != null ) {
                stringBuilder.append(line);
            }
            htmlData = stringBuilder.toString();
        } catch( IOException error ) {}
        webView.loadDataWithBaseURL("file:///android_asset/", htmlData, "text/html", "utf-8", "about:blank");
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(MainActivity.TAG_AB);
    }
}
