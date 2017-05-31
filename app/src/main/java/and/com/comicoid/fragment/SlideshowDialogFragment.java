package and.com.comicoid.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import and.com.comicoid.R;
import and.com.comicoid.adapter.MyViewPagerAdapter;
import and.com.comicoid.model.Image;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dell on 31-05-2017.
 */

public class SlideshowDialogFragment extends DialogFragment {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.count)
    TextView count;
    @BindView(R.id.description)
    TextView description;
    Unbinder unbinder;

    private String TAG = SlideshowDialogFragment.class.getSimpleName();
    private List<Image> imageArrayList;
    private MyViewPagerAdapter myViewPagerAdapter;
    private int selectedPosition = 0;

    public static SlideshowDialogFragment newInstance() {
        SlideshowDialogFragment f = new SlideshowDialogFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_slider, container, false);
        ButterKnife.bind(this,rootView);

        if((getArguments().getParcelableArrayList("imagesList") != null) && (getArguments().getInt("position") < 0 )){
            imageArrayList = getArguments().getParcelableArrayList("imagesList");
            selectedPosition = getArguments().getInt("position");
        }

        myViewPagerAdapter = new MyViewPagerAdapter(imageArrayList,getContext());
        viewpager.setAdapter(myViewPagerAdapter);
        viewpager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void setCurrentItem(int position) {
        viewpager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {
        Image image = imageArrayList.get(position);
        count.setText((position + 1) + " of " + imageArrayList.size());
        description.setText(image.getDescription());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
