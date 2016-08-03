package fr.tvbarthel.apps.cameracolorpicker.activities;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.melnykov.fab.FloatingActionButton;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItems;
import fr.tvbarthel.apps.cameracolorpicker.data.Palette;
import fr.tvbarthel.apps.cameracolorpicker.views.FlavorColorItemListWrapper;
import fr.tvbarthel.apps.cameracolorpicker.views.PaletteMakerView;
import fr.tvbarthel.apps.cameracolorpicker.wrappers.ColorItemListWrapper;

/**
 * A base {@link AppCompatActivity} for creating a {@link Palette} from the {@link ColorItem}s that the user saved.
 * <p/>
 * This provide the base interface of the creation of a {@link Palette}.
 * <p/>
 * The creation of the {@link Palette} is left to the {@link PaletteCreationActivity} implementation.
 */
public abstract class PaletteCreationBaseActivity extends AppCompatActivity implements OnClickListener, ColorItemListWrapper.ColorItemListWrapperListener {

    /**
     * The duration of the fab animation. (in milliseconds)
     */
    private static final int FAB_ANIMATION_DURATION = 300;

    /**
     * The {@link PaletteMakerView} used for building a palette of colors.
     */
    protected PaletteMakerView mPaletteMakerView;

    /**
     * An {@link ObjectAnimator} used for animating the button that removes the last color from the color palette builder.
     */
    private ObjectAnimator mRemoveLastColorBtnAnimator;

    /**
     * A {@link FloatingActionButton} for the creation of the {@link Palette}.
     */
    private FloatingActionButton mFab;

    /**
     * A {@link LinearInterpolator} for animating the {@link FloatingActionButton}.
     */
    private LinearInterpolator mLinearInterpolator;

    /**
     * A {@link DecelerateInterpolator} for animating the {@link FloatingActionButton}.
     */
    private DecelerateInterpolator mDecelerateInterpolator;

    /**
     * The y translation used for hiding the {@link FloatingActionButton}.
     */
    private int mHideTranslationYFab;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette_creation);

        mPaletteMakerView = (PaletteMakerView) findViewById(R.id.activity_palette_creation_color_palette_builder);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_palette_creation_list_view);
        final ColorItemListWrapper colorItemListWrapper = new FlavorColorItemListWrapper(recyclerView, this);
        colorItemListWrapper.installRecyclerView();
        colorItemListWrapper.setItems(ColorItems.getSavedColorItems(this));

        final View removeButton = findViewById(R.id.activity_palette_creation_remove_button);
        removeButton.setOnClickListener(this);
        removeButton.setScaleX(0f);
        mRemoveLastColorBtnAnimator = ObjectAnimator.ofFloat(removeButton, View.SCALE_X, 0f, 1f);

        mLinearInterpolator = new LinearInterpolator();
        mDecelerateInterpolator = new DecelerateInterpolator();

        mFab = (FloatingActionButton) findViewById(R.id.activity_palette_creation_fab);
        mFab.setOnClickListener(this);

        ViewTreeObserver viewTreeObserver = mFab.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    final ViewTreeObserver viewTreeObserver = mFab.getViewTreeObserver();
                    viewTreeObserver.removeOnPreDrawListener(this);

                    mHideTranslationYFab = ((View) mFab.getParent()).getHeight() - mFab.getTop();
                    mFab.setTranslationY(mHideTranslationYFab);
                    return true;
                }
            });
        }

        PaletteCreationBaseActivityFlavor.onCreate(this);
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        switch (viewId) {
            case R.id.activity_palette_creation_remove_button:
                mPaletteMakerView.removeLastColor();
                if (mPaletteMakerView.isEmpty()) {
                    mRemoveLastColorBtnAnimator.setFloatValues(0f);
                    mRemoveLastColorBtnAnimator.start();

                    mFab.animate()
                            .translationY(mHideTranslationYFab)
                            .setDuration(FAB_ANIMATION_DURATION)
                            .setInterpolator(mLinearInterpolator);
                }
                break;

            case R.id.activity_palette_creation_fab:
                if (!mPaletteMakerView.isEmpty()) {
                    createPalette(mPaletteMakerView);
                }
                break;

            default:
                throw new IllegalArgumentException("Unsupported view clicked. Found: " + v);
        }
    }

    @Override
    public void onColorItemClicked(@NonNull ColorItem colorItem, @NonNull View colorPreview) {
        mPaletteMakerView.addColor(colorItem);
        if (mPaletteMakerView.size() == 1) {
            mRemoveLastColorBtnAnimator.setFloatValues(1f);
            mRemoveLastColorBtnAnimator.start();

            mFab.animate()
                    .translationY(0)
                    .setDuration(FAB_ANIMATION_DURATION)
                    .setInterpolator(mDecelerateInterpolator);
        }
    }

    /**
     * Create a palette.
     * <p/>
     * Called when the user want to create a {@link Palette}.
     *
     * @param paletteMakerView the {@link PaletteMakerView} that should be used to create a {@link Palette}.
     */
    protected abstract void createPalette(PaletteMakerView paletteMakerView);
}
