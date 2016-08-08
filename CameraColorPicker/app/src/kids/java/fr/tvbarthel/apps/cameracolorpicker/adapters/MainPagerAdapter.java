package fr.tvbarthel.apps.cameracolorpicker.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;

import fr.tvbarthel.apps.cameracolorpicker.R;

/**
 * Adpater used to display the tab of the
 * {@link fr.tvbarthel.apps.cameracolorpicker.activities.MainActivity}
 */
public abstract class MainPagerAdapter extends PagerAdapter implements PagerSlidingTabStrip.CustomTabProvider {
    @Override
    public View getCustomTabView(ViewGroup parent, int position) {
        Context context = parent.getContext();
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setContentDescription(getPageTitle(position));
        switch (position) {
            case 0:
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_round_white));
                break;
            case 1:
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_view_column_white_30dp);
                drawable.setAlpha(170);
                imageView.setImageDrawable(drawable);
                break;
            default:
                throw new IllegalStateException("Can't instantiate tab view for position : " + position);
        }
        return imageView;
    }
}
