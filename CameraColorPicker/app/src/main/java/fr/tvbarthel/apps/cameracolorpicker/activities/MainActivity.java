package fr.tvbarthel.apps.cameracolorpicker.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.adapters.ColorAdapter;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItems;

/**
 * TODO comment
 */
public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    protected ColorAdapter mColorAdapter;

    /**
     * The user-visible label for the clip {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}.
     */
    protected String mClipColorItemLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mClipColorItemLabel = getString(R.string.activity_main_clip_color_item_label);

        mColorAdapter = new ColorAdapter(this);
        final View emptyView = findViewById(R.id.activity_main_empty_view);
        final ListView colorList = (ListView) findViewById(R.id.activity_main_list_view);
        colorList.setAdapter(mColorAdapter);
        colorList.setEmptyView(emptyView);
        colorList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final ColorItem colorItem = mColorAdapter.getItem(position);
                final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                final ClipData clip = ClipData.newPlainText(mClipColorItemLabel, colorItem.getHexString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(view.getContext(), R.string.activity_main_color_success_copy_message, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_main_fab);
        fab.attachToListView(colorList);
        fab.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mColorAdapter.clear();
        mColorAdapter.addAll(ColorItems.getSavedColorItems(this));
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
