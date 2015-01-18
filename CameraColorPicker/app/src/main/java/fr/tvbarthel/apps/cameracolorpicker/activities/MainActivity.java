package fr.tvbarthel.apps.cameracolorpicker.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.adapters.ColorAdapter;

/**
 * TODO comment
 */
public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ColorAdapter colorAdapter = new ColorAdapter(this, fakeColors());
        final View emptyView = findViewById(R.id.activity_main_empty_view);
        final ListView colorList = (ListView) findViewById(R.id.activity_main_list_view);
        colorList.setAdapter(colorAdapter);
        colorList.setEmptyView(emptyView);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_main_fab);
        fab.attachToListView(colorList);
    }

    // TODO remove (for test purpose only)
    private List<Integer> fakeColors() {
        final List<Integer> fakeColors = new ArrayList<>(11);
        fakeColors.add(Color.BLACK);
        fakeColors.add(Color.BLUE);
        fakeColors.add(Color.CYAN);
        fakeColors.add(Color.DKGRAY);
        fakeColors.add(Color.GRAY);
        fakeColors.add(Color.GREEN);
        fakeColors.add(Color.LTGRAY);
        fakeColors.add(Color.MAGENTA);
        fakeColors.add(Color.RED);
        fakeColors.add(Color.WHITE);
        fakeColors.add(Color.YELLOW);
        return fakeColors;
    }
}
