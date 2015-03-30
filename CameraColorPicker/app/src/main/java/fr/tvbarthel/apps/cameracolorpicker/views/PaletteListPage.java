package fr.tvbarthel.apps.cameracolorpicker.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.List;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.activities.PaletteDetailActivity;
import fr.tvbarthel.apps.cameracolorpicker.adapters.PaletteAdapter;
import fr.tvbarthel.apps.cameracolorpicker.data.Palette;
import fr.tvbarthel.apps.cameracolorpicker.data.Palettes;

/**
 * A simple {@link FrameLayout} used in the {@link android.support.v4.view.ViewPager} of the {@link fr.tvbarthel.apps.cameracolorpicker.activities.MainActivity}
 * to display the list of the {@link Palette}s that the user created.
 */
public class PaletteListPage extends FrameLayout {

    /**
     * An {@link Palettes.OnPaletteChangeListener} that will be notified when the palettes of the user change.
     */
    private Palettes.OnPaletteChangeListener mOnPaletteChangeListener;

    public PaletteListPage(Context context) {
        super(context);
        init(context);
    }

    public PaletteListPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PaletteListPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PaletteListPage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * Initialize this palette list page.
     * <p/>
     * Must be called once in each constructor.
     *
     * @param context the {@link Context} from the constructor.
     */
    private void init(Context context) {
        final View view = View.inflate(context, R.layout.view_palette_list_page, this);

        final View emptyView = view.findViewById(R.id.view_palette_list_empty_view);
        final ListView listView = (ListView) view.findViewById(R.id.view_palette_list_page_list_view);
        listView.setEmptyView(emptyView);

        final PaletteAdapter adapter = new PaletteAdapter(context);
        adapter.addAll(Palettes.getSavedColorPalettes(context));

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Palette palette = adapter.getItem(position);
                PaletteDetailActivity.startWithColorPalette(view.getContext(), palette,
                        view.findViewById(R.id.row_color_palette_thumbnail));
            }
        });

        mOnPaletteChangeListener = new Palettes.OnPaletteChangeListener() {
            @Override
            public void onColorPaletteChanged(List<Palette> palettes) {
                adapter.clear();
                adapter.addAll(palettes);
                adapter.notifyDataSetChanged();
            }
        };
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Register the OnPaletteChangeListener to be notified when the user create a new palette
        // Or when a user delete a palette.
        Palettes.registerListener(getContext(), mOnPaletteChangeListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        // Unregister the OnPaletteChangeListener.
        // We do not need to be notified anymore.
        Palettes.unregisterListener(getContext(), mOnPaletteChangeListener);
        super.onDetachedFromWindow();
    }
}
