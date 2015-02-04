package fr.tvbarthel.apps.cameracolorpicker.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;

public class ColorDetailActivity extends ActionBarActivity {

    /**
     * A key for passing a color item as extra.
     */
    protected static final String EXTRA_COLOR_ITEM = "ColorDetailActivity.Extras.EXTRA_COLOR_ITEM";

    public static void startWithColorItem(Context context, ColorItem colorItem) {
        final Intent intent = new Intent(context, ColorDetailActivity.class);
        intent.putExtra(EXTRA_COLOR_ITEM, colorItem);

        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * A {@link android.view.View} for showing the color preview.
     */
    protected View mPreview;

    /**
     * A {@link android.widget.TextView} for showing the hexadecimal value of the color.
     */
    protected TextView mHexadecimal;

    /**
     * A {@link android.widget.TextView} for showing the RGB value of the color.
     */
    protected TextView mRgb;

    /**
     * A {@link android.widget.TextView} for showing the HSV value of the color.
     */
    protected TextView mHsv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_detail);

        // ensure correct extras
        final Intent intent = getIntent();
        if (!intent.hasExtra(EXTRA_COLOR_ITEM)) {
            throw new IllegalStateException("No color item extra found. Please use startWithColorItem.");
        }

        // Retrieve the color item extra.
        final ColorItem colorItem = intent.getParcelableExtra(EXTRA_COLOR_ITEM);

        // Find the views.
        mPreview = findViewById(R.id.activity_color_detail_preview);
        mHexadecimal = (TextView) findViewById(R.id.activity_color_detail_hexadecimal);
        mRgb = (TextView) findViewById(R.id.activity_color_detail_rgb);
        mHsv = (TextView) findViewById(R.id.activity_color_detail_hsv);

        // Display the color item data.
        mPreview.setBackgroundColor(colorItem.getColor());
        mHexadecimal.setText(colorItem.getHexString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_color_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
