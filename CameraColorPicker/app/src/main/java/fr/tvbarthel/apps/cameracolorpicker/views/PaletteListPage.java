package fr.tvbarthel.apps.cameracolorpicker.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.util.List;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.activities.PaletteDetailActivity;
import fr.tvbarthel.apps.cameracolorpicker.data.Palette;
import fr.tvbarthel.apps.cameracolorpicker.data.Palettes;
import fr.tvbarthel.apps.cameracolorpicker.wrappers.PaletteListWrapper;

/**
 * A simple {@link FrameLayout} used in the {@link android.support.v4.view.ViewPager} of the {@link fr.tvbarthel.apps.cameracolorpicker.activities.MainActivity}
 * to display the list of the {@link Palette}s that the user created.
 */
public class PaletteListPage extends FrameLayout implements PaletteListWrapper.PaletteListWrapperListener {

    /**
     * An {@link Palettes.OnPaletteChangeListener} that will be notified when the palettes of the user change.
     */
    private Palettes.OnPaletteChangeListener mOnPaletteChangeListener;

    /**
     * Listener used to catch internal event in order to avoid {@link PaletteListPage} exposing
     * this onClick callback.
     */
    private OnClickListener internalListener;

    /**
     * Listener used to catch view events.
     */
    private Listener listener;

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
     * Listener used to catch view events.
     *
     * @param listener listener used to catch view events.
     */
    public void setListener(Listener listener) {
        this.listener = listener;
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

        initInternalListener();

        final View emptyView = view.findViewById(R.id.view_palette_list_empty_view);
        emptyView.setOnClickListener(internalListener);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.view_palette_list_page_list_view);
        final FlavorPaletteListWrapper wrapper = FlavorPaletteListWrapper.create(recyclerView, this);
        final PaletteListWrapper.Adapter adapter = wrapper.installRecyclerView();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                emptyView.setVisibility(adapter.getItemCount() == 0 ? VISIBLE : GONE);
            }
        });

        adapter.setPalettes(Palettes.getSavedColorPalettes(context));
        mOnPaletteChangeListener = new Palettes.OnPaletteChangeListener() {
            @Override
            public void onColorPaletteChanged(List<Palette> palettes) {
                adapter.setPalettes(palettes);
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

    @Override
    public void onPaletteClicked(@NonNull Palette palette, @NonNull View palettePreview) {
        PaletteDetailActivity.startWithColorPalette(getContext(), palette, palettePreview);
    }

    /**
     * Initialize internal listener.
     */
    private void initInternalListener() {
        internalListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onEmphasisOnPaletteCreationRequested();
                }
            }
        };
    }

    /**
     * Listener used to catch view events.
     */
    public interface Listener {
        /**
         * Called when the user requested emphasis on the palette creation action.
         * <p/>
         * Currently, when the user touch the empty view.
         */
        void onEmphasisOnPaletteCreationRequested();
    }
}
