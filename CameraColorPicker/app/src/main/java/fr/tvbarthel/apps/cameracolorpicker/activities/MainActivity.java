package fr.tvbarthel.apps.cameracolorpicker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.adapters.ColorAdapter;
import fr.tvbarthel.apps.cameracolorpicker.data.Colors;

/**
 * TODO comment
 */
public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    protected ColorAdapter mColorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mColorAdapter = new ColorAdapter(this);
        final View emptyView = findViewById(R.id.activity_main_empty_view);
        final ListView colorList = (ListView) findViewById(R.id.activity_main_list_view);
        colorList.setAdapter(mColorAdapter);
        colorList.setEmptyView(emptyView);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_main_fab);
        fab.attachToListView(colorList);
        fab.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mColorAdapter.clear();
        mColorAdapter.addAll(Colors.getSavedColors(this));
        mColorAdapter.notifyDataSetChanged();
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
}
