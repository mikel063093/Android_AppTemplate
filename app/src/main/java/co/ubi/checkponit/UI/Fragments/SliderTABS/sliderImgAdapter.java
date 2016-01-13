package co.ubi.checkponit.UI.Fragments.SliderTABS;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import co.ubi.checkponit.R;

/**
 * Created by ${Mike} on 1/3/15.
 */
public class sliderImgAdapter extends FragmentPagerAdapter {

    String[] ArrayStringDescription;
    private int[] Images = new int[]{R.drawable.police};
    private int mCount = Images.length;

    public sliderImgAdapter(FragmentManager fm, Context context) {
        super(fm);
        ArrayStringDescription = context.getResources().getStringArray(R.array.ArrayDescription);
    }

    @Override
    public Fragment getItem(int position) {
        return imgSliderFragment.newInstance(Images[position], ArrayStringDescription[position]);
    }

    @Override
    public int getCount() {
        return mCount;
    }


    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }

}
