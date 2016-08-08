package fr.tvbarthel.apps.cameracolorpicker.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.melnykov.fab.FloatingActionButton;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cameracolorpicker.flavors.MainActivityFlavor;
import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.adapters.MainPagerAdapter;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItems;
import fr.tvbarthel.apps.cameracolorpicker.fragments.AboutDialogFragment;
import fr.tvbarthel.apps.cameracolorpicker.views.ColorItemListPage;
import fr.tvbarthel.apps.cameracolorpicker.views.PaletteListPage;


/**
 * An {@link android.support.v7.app.AppCompatActivity} that shows the list of the colors that the user saved.
 * <p/>
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener, ColorItemListPage.Listener, PaletteListPage.Listener {

    @IntDef({PAGE_ID_COLOR_ITEM_LIST, PAGE_ID_PALETTE_LIST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PageId {
    }

    /**
     * The id associated with the color item list page.
     */
    private static final int PAGE_ID_COLOR_ITEM_LIST = 1;

    /**
     * The id associated with the palette list page.
     */
    private static final int PAGE_ID_PALETTE_LIST = 2;

    /**
     * A reference to the current {@link android.widget.Toast}.
     * <p/>
     * Used for hiding the current {@link android.widget.Toast} before showing a new one or when the activity is paused.
     * {@link }
     */
    private Toast mToast;

    /**
     * A {@link cameracolorpicker.flavors.MainActivityFlavor} for behaviors specific to flavors.
     */
    private MainActivityFlavor mMainActivityFlavor;

    /**
     * The {@link Toolbar} of this {@link MainActivity}.
     */
    private Toolbar mToolbar;

    /**
     * The {@link PagerSlidingTabStrip} for displaying the tabs.
     */
    private PagerSlidingTabStrip mTabs;

    /**
     * A {@link com.melnykov.fab.FloatingActionButton} for launching the {@link ColorPickerBaseActivity}.
     */
    private FloatingActionButton mFab;

    /**
     * A {@link ViewPager} that displays two pages: {@link ColorItemListPage} and {@link PaletteListPage}.
     */
    private ViewPager mViewPager;

    /**
     * The {@link ColorItemListPage} being displayed in the {@link ViewPager}.
     */
    private ColorItemListPage mColorItemListPage;

    /**
     * The {@link PaletteListPage} being displayed in the {@link ViewPager}.
     */
    private PaletteListPage mPaletteListPage;

    /**
     * The id of the current page selected.
     * <p/>
     * {@link fr.tvbarthel.apps.cameracolorpicker.activities.MainActivity.PageId}
     * Used for updating the icon of the {@link FloatingActionButton} when the user scrolls between pages.
     */
    private int mCurrentPageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);

        mCurrentPageId = PAGE_ID_COLOR_ITEM_LIST;
        mColorItemListPage = new ColorItemListPage(this);
        mColorItemListPage.setListener(this);
        mPaletteListPage = new PaletteListPage(this);
        mPaletteListPage.setListener(this);

        mFab = (FloatingActionButton) findViewById(R.id.activity_main_fab);
        mFab.setOnClickListener(this);

        final MyPagerAdapter adapter = new MyPagerAdapter();
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.activity_main_tabs);
        mViewPager = (ViewPager) findViewById(R.id.activity_main_view_pager);
        mViewPager.setAdapter(adapter);
        mTabs.setViewPager(mViewPager);
        mTabs.setOnPageChangeListener(this);

        mMainActivityFlavor = new MainActivityFlavor(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ColorItems.getSavedColorItems(this).size() <= 1) {
            animateFab(mFab);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideToast();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Inflate the menu specific to the flavor.
        mMainActivityFlavor.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        boolean handled;

        switch (itemId) {
            case R.id.menu_main_action_licenses:
                handled = true;
                final Intent intent = new Intent(this, LicenseActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_main_action_about:
                handled = true;
                AboutDialogFragment.newInstance().show(getSupportFragmentManager(), null);
                break;

            case R.id.menu_main_action_contact_us:
                handled = true;
                final String uriString = getString(R.string.contact_us_uri,
                        Uri.encode(getString(R.string.contact_us_email)),
                        Uri.encode(getString(R.string.contact_us_default_subject)));
                final Uri mailToUri = Uri.parse(uriString);
                final Intent sendToIntent = new Intent(Intent.ACTION_SENDTO);
                sendToIntent.setData(mailToUri);
                startActivity(sendToIntent);
                break;

            default:
                handled = mMainActivityFlavor.onOptionsItemSelected(item);
                if (!handled) {
                    handled = super.onOptionsItemSelected(item);
                }
        }

        return handled;
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();

        switch (viewId) {
            case R.id.activity_main_fab:
                if (mCurrentPageId == PAGE_ID_COLOR_ITEM_LIST) {
                    final Intent intentColorPickerActivity = new Intent(this, ColorPickerActivity.class);
                    startActivity(intentColorPickerActivity);
                } else if (mCurrentPageId == PAGE_ID_PALETTE_LIST) {
                    // Check if there is at least two color items.
                    // Creating a color palette with 1 or 0 colors make no sense.
                    if (ColorItems.getSavedColorItems(this).size() <= 1) {
                        onEmphasisOnPaletteCreationRequested();
                        showToast(R.string.activity_main_error_not_enough_colors);
                    } else {
                        final Intent intentColorPaletteActivity = new Intent(this, PaletteCreationActivity.class);
                        startActivity(intentColorPaletteActivity);
                    }
                }
                break;

            default:
                throw new IllegalArgumentException("View clicked unsupported. Found " + v);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int pageId;
        if (position == 0) {
            if (positionOffset <= 0.5) {
                // In range [0; 0.5]
                // Scrolling from/to the first tab
                // We re-map the positionOffset in range [0; 1]
                // With 0 being the position where the first tab is fully visible.
                positionOffset *= 2;
                pageId = PAGE_ID_COLOR_ITEM_LIST;
            } else {
                // In range [0.5; 1]
                // Scrolling from/to the second tab
                // We re-map the positionOffset in range [0;1]
                // With 0 being the position where the second tab is fully visible.
                positionOffset = (1 - positionOffset) * 2;
                pageId = PAGE_ID_PALETTE_LIST;
            }
        } else {
            positionOffset = 0;
            pageId = PAGE_ID_PALETTE_LIST;
        }

        mFab.setTranslationY((((FrameLayout) mFab.getParent()).getHeight() - mFab.getTop()) * positionOffset);
        if (pageId != mCurrentPageId) {
            setCurrentPage(pageId);
        }
    }

    @Override
    public void onPageSelected(int position) {
        // Nothing to do.
        // The current page is already set in the onPageScrolled.
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // Nothing to do.
    }

    @Override
    public void onEmphasisOnAddColorActionRequested() {
        animateFab(mFab, 0);
    }

    @Override
    public void onEmphasisOnPaletteCreationRequested() {
        if (ColorItems.getSavedColorItems(this).size() <= 1) {
            // needs more color to create a palette.
            mViewPager.setCurrentItem(0, true);
            animateFab(mFab, 300);
        } else {
            // touch the fab to create a palette.
            animateFab(mFab, 0);
        }
    }

    /**
     * Set the current page id.
     *
     * @param pageId the {@link fr.tvbarthel.apps.cameracolorpicker.activities.MainActivity.PageId} of the current selected page.
     */
    private void setCurrentPage(@PageId int pageId) {
        mCurrentPageId = pageId;
        if (pageId == PAGE_ID_COLOR_ITEM_LIST) {
            mFab.setImageResource(R.drawable.ic_fab_color_picker_action);
        } else if (pageId == PAGE_ID_PALETTE_LIST) {
            mFab.setImageResource(R.drawable.ic_fab_palette_creation_action);
        }
    }

    /**
     * Hide the current {@link android.widget.Toast}.
     */
    private void hideToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

    /**
     * Show a toast text message.
     *
     * @param resId The resource id of the string resource to use.
     */
    private void showToast(@StringRes int resId) {
        hideToast();
        String toastText = getString(resId);
        if (!TextUtils.isEmpty(toastText)) {
            mToast = Toast.makeText(this, resId, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    /**
     * Make a subtle animation for a {@link com.melnykov.fab.FloatingActionButton} drawing attention to the button.
     * Apply a default delay of 400ms.
     * <p/>
     * See also : {@link MainActivity#animateFab(FloatingActionButton, int)}
     *
     * @param fab the {@link com.melnykov.fab.FloatingActionButton} to animate.
     */
    private void animateFab(final FloatingActionButton fab) {
        animateFab(fab, 400);
    }

    /**
     * Make a subtle animation for a {@link com.melnykov.fab.FloatingActionButton} drawing attention to the button.
     *
     * @param fab   the {@link com.melnykov.fab.FloatingActionButton} to animate.
     * @param delay delay before the animation start.
     */
    private void animateFab(final FloatingActionButton fab, int delay) {
        fab.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Play a subtle animation
                final long duration = 300;

                final ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(fab, View.SCALE_X, 1f, 1.2f, 1f);
                scaleXAnimator.setDuration(duration);
                scaleXAnimator.setRepeatCount(1);

                final ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(fab, View.SCALE_Y, 1f, 1.2f, 1f);
                scaleYAnimator.setDuration(duration);
                scaleYAnimator.setRepeatCount(1);

                scaleXAnimator.start();
                scaleYAnimator.start();

                final AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(scaleXAnimator).with(scaleYAnimator);
                animatorSet.start();
            }
        }, delay);
    }

    private class MyPagerAdapter extends MainPagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.activity_main_view_pager_title_color);

                case 1:
                    return getString(R.string.activity_main_view_pager_title_palette);

                default:
                    return getString(R.string.activity_main_view_pager_title_unknown);
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final View view;

            if (position == 0) {
                view = mColorItemListPage;
            } else if (position == 1) {
                view = mPaletteListPage;
            } else {
                throw new IllegalArgumentException("Invalid position. Positions supported are 0 & 1, found " + position);
            }

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
