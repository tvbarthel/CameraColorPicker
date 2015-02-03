package fr.tvbarthel.apps.cameracolorpicker.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import fr.tvbarthel.apps.cameracolorpicker.R;

public class ColorDetailActivity extends ActionBarActivity {

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

        mPreview = findViewById(R.id.activity_color_detail_preview);
        mHexadecimal = (TextView) findViewById(R.id.activity_color_detail_hexadecimal);
        mRgb = (TextView) findViewById(R.id.activity_color_detail_rgb);
        mHsv = (TextView) findViewById(R.id.activity_color_detail_hsv);
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
