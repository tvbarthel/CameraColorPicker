package fr.tvbarthel.apps.cameracolorpicker.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import cameracolorpicker.flavors.MainActivityFlavor;
import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.adapters.ColorAdapter;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItems;
import fr.tvbarthel.apps.cameracolorpicker.fragments.AboutDialogFragment;
import fr.tvbarthel.apps.cameracolorpicker.utils.ClipDatas;

/**
 * An {@link android.support.v7.app.ActionBarActivity} that shows the list of the colors that the user saved.
 */
public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    /**
     * A {@link fr.tvbarthel.apps.cameracolorpicker.adapters.ColorAdapter} used for adapting the {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}s.
     */
    protected ColorAdapter mColorAdapter;

    /**
     * The user-visible label for the clip {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}.
     */
    protected String mClipColorItemLabel;

    /**
     * A reference to the current {@link android.widget.Toast}.
     * <p/>
     * Used for hiding the current {@link android.widget.Toast} before showing a new one or the activity is paused.
     * {@link }
     */
    protected Toast mToast;

    /**
     * A {@link android.widget.ListView} used for displaying the {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}s
     */
    protected ListView mListView;

    /**
     * A {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItems.OnColorItemChangeListener} for listening the creation of new {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}s.
     */
    protected ColorItems.OnColorItemChangeListener mOnColorItemChangeListener;

    /**
     * A {@link com.melnykov.fab.FloatingActionButton} for launching the {@link fr.tvbarthel.apps.cameracolorpicker.activities.ColorPickerActivity}.
     */
    private FloatingActionButton mFab;

    /**
     * A {@link cameracolorpicker.flavors.MainActivityFlavor} for behaviors specific to flavors.
     */
    private MainActivityFlavor mMainActivityFlavor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainActivityFlavor = new MainActivityFlavor(this);
        mClipColorItemLabel = getString(R.string.color_clip_color_label_hex);

        mColorAdapter = new ColorAdapter(this);
        mColorAdapter.addAll(ColorItems.getSavedColorItems(this));
        final View emptyView = findViewById(R.id.activity_main_empty_view);
        mListView = (ListView) findViewById(R.id.activity_main_list_view);
        mListView.setAdapter(mColorAdapter);
        mListView.setEmptyView(emptyView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ColorItem colorItem = mColorAdapter.getItem(position);
                ColorDetailActivity.startWithColorItem(MainActivity.this, colorItem, view.findViewById(R.id.row_color_item_preview));
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final ColorItem colorItem = mColorAdapter.getItem(position);
                ClipDatas.clipPainText(MainActivity.this, mClipColorItemLabel, colorItem.getHexString());
                showToast(R.string.color_clip_success_copy_message);
                return true;
            }
        });

        mFab = (FloatingActionButton) findViewById(R.id.activity_main_fab);
        mFab.attachToListView(mListView);
        mFab.setOnClickListener(this);

        mOnColorItemChangeListener = new ColorItems.OnColorItemChangeListener() {
            @Override
            public void onColorItemChanged(List<ColorItem> colorItems) {
                mColorAdapter.clear();
                mColorAdapter.addAll(colorItems);
                mColorAdapter.notifyDataSetChanged();
            }
        };

        ColorItems.registerListener(this, mOnColorItemChangeListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mColorAdapter.getCount() == 0) {
            animateFab(mFab);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideToast();
    }

    @Override
    protected void onDestroy() {
        ColorItems.unregisterListener(this, mOnColorItemChangeListener);
        super.onDestroy();
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
                final Intent intent = new Intent(this, ColorPickerActivity.class);
                startActivity(intent);
                break;

            default:
                throw new IllegalArgumentException("View clicked unsupported. Found " + v);
        }
    }

    /**
     * Hide the current {@link android.widget.Toast}.
     */
    protected void hideToast() {
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
    protected void showToast(int resId) {
        hideToast();
        mToast = Toast.makeText(this, resId, Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * Make a subtle animation for a {@link com.melnykov.fab.FloatingActionButton} drawing attention to the button.
     *
     * @param fab the {@link com.melnykov.fab.FloatingActionButton} to animate.
     */
    private void animateFab(final FloatingActionButton fab) {
        fab.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Play a subtle animation
                final long duration = 450;

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
        }, 400);
    }
}
