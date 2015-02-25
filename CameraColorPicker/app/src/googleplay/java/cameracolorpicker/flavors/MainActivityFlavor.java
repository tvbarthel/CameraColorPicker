package cameracolorpicker.flavors;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import fr.tvbarthel.apps.billing.SupportActivity;
import fr.tvbarthel.apps.billing.utils.SupportUtils;
import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.activities.MainActivity;

/**
 * The behavior of the {@link fr.tvbarthel.apps.cameracolorpicker.activities.MainActivity} specific to the googleplay product flavor.
 * This class should be present in all the product flavors.
 */
public class MainActivityFlavor {

    /**
     * A {@link fr.tvbarthel.apps.cameracolorpicker.activities.MainActivity}.
     */
    private MainActivity mMainActivity;

    /**
     * A reference to a {@link android.widget.Toast} for displaying thanks messages.
     */
    private Toast mToast;

    public MainActivityFlavor(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    /**
     * A flavor behavior for {@link android.app.Activity#onCreateOptionsMenu(android.view.Menu)}
     */
    public boolean onCreateOptionsMenu(final Menu menu) {
        mMainActivity.getMenuInflater().inflate(R.menu.menu_support, menu);
        // If the user is supporting us, add a thanks action.
        SupportUtils.checkSupport(mMainActivity.getApplicationContext(), new SupportUtils.OnCheckSupportListener() {
            @Override
            public void onCheckSupport(boolean supporting) {
                if (supporting) {
                    mMainActivity.getMenuInflater().inflate(R.menu.menu_thanks, menu);
                }
            }
        });
        return true;
    }

    /**
     * A flavor behavior for {@link android.app.Activity#onCreateOptionsMenu(android.view.Menu)}.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        boolean handled;
        switch (itemId) {
            case R.id.menu_support_action_support:
                handled = true;
                final Intent intent = new Intent(mMainActivity, SupportActivity.class);
                mMainActivity.startActivity(intent);
                break;

            case R.id.menu_thanks_action_thanks:
                handled = true;
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(mMainActivity,
                        R.string.support_has_supported_us, Toast.LENGTH_SHORT);
                mToast.show();
                break;

            default:
                handled = false;
        }

        return handled;
    }
}
