package co.ubi.checkponit.UI.Fragments.SliderTABS;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.ubi.checkponit.R;
import co.ubi.checkponit.UI.Fragments.BaseFragment;

/**
 * Created by ${Mike} on 1/3/15.
 */
public class imgSliderFragment extends BaseFragment {
    public static int imageResourceId;
    public static String IMG_KEY = "IMG";
    String description;
    public static String DESCRIPTION_KEY = "DES";

    public static imgSliderFragment newInstance(int i, String description) {
        imgSliderFragment placeSlideFragment = new imgSliderFragment();
        Bundle args = new Bundle();
        args.putString(DESCRIPTION_KEY, description);
        args.putInt(IMG_KEY, i);
        placeSlideFragment.setArguments(args);

        return placeSlideFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageResourceId = getArguments().getInt(IMG_KEY);
            description = getArguments().getString(DESCRIPTION_KEY);
            //font= Typeface.createFromAsset(getActivity().getAssets(),"Cinzel-Regular.ttf");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.slider_login, container, false);
        //ImageView imgSlider = (ImageView) rootView.findViewById(R.id.imageSlinder);
        LinearLayout rl = (LinearLayout) rootView.findViewById(R.id.LinarLayoutSlideImageRoot);
        rl.setBackgroundResource(imageResourceId);
        //imgSlider.setImageResource(imageResourceId);
        TextView TextDescription = (TextView) rootView.findViewById(R.id.TextViewDescription);
        TextDescription.setText(description);
        //  TextDescription.setTypeface(font);
        return rootView;
    }
}
