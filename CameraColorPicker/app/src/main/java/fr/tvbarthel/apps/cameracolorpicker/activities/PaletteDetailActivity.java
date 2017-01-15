package fr.tvbarthel.apps.cameracolorpicker.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;
import fr.tvbarthel.apps.cameracolorpicker.data.Palette;
import fr.tvbarthel.apps.cameracolorpicker.data.Palettes;
import fr.tvbarthel.apps.cameracolorpicker.fragments.DeletePaletteDialogFragment;
import fr.tvbarthel.apps.cameracolorpicker.fragments.EditTextDialogFragment;
import fr.tvbarthel.apps.cameracolorpicker.views.FlavorColorItemListWrapper;
import fr.tvbarthel.apps.cameracolorpicker.views.PaletteView;
import fr.tvbarthel.apps.cameracolorpicker.wrappers.ColorItemListWrapper;

/**
 * A simple {@link AppCompatActivity} for displaying a {@link Palette} with its {@link ColorItem}s
 */
public class PaletteDetailActivity extends AppCompatActivity implements DeletePaletteDialogFragment.Callback,
        EditTextDialogFragment.Callback, ColorItemListWrapper.ColorItemListWrapperListener {

    /**
     * The quality of the image compressed before sharing.
     */
    private static final int SHARED_IMAGE_QUALITY = 100;

    /**
     * The size in pixels of the shared image.
     */
    private static final int SHARED_IMAGE_SIZE = 150;

    /**
     * The name of the directory where the shared image is created.
     */
    private static final String SHARED_DIRECTORY = "palettes";

    /**
     * The name of the file used to write the shared image.
     */
    private static final String SHARED_IMAGE_FILE = "shared_palettes.jpg";

    /**
     * The authority of the file provider declared in our manifest.
     */
    private static final String FILE_PROVIDER_AUTHORITY = "fr.tvbarthel.apps.cameracolorpicker.fileprovider";

    /**
     * A key for passing a color palette as extra.
     */
    protected static final String EXTRA_COLOR_PALETTE = "PaletteDetailActivity.Extras.EXTRA_COLOR_PALETTE";

    /**
     * A key for passing the global visible rect of the clicked color preview clicked.
     */
    protected static final String EXTRA_START_BOUNDS = "PaletteDetailActivity.Extras.EXTRA_START_BOUNDS";

    /**
     * Inset of the square shadow which must be take into account we evaluate the scale ratio.
     */
    private int shadowInset;

    public static void startWithColorPalette(Context context, Palette palette, View colorPreviewClicked) {
        final boolean isActivity = context instanceof Activity;
        final Rect startBounds = new Rect();
        colorPreviewClicked.getGlobalVisibleRect(startBounds);

        final Intent intent = new Intent(context, PaletteDetailActivity.class);
        intent.putExtra(EXTRA_COLOR_PALETTE, palette);
        intent.putExtra(EXTRA_START_BOUNDS, startBounds);

        if (!isActivity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);

        if (isActivity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }

    /**
     * A {@link PaletteView} for showing the palette preview being translated during the start animation from the clicked position to the right location.
     */
    protected PaletteView mTranslatedPreview;

    /**
     * A {@link PaletteView} for showing the palette preview being scaled during the start animation to show the {@link Palette}.
     */
    protected PaletteView mScaledPreview;

    /**
     * The {@link Palette} being displayed.
     */
    protected Palette mPalette;

    /**
     * A reference to the current {@link android.widget.Toast}.
     * <p/>
     * Used for hiding the current {@link android.widget.Toast} before showing a new one or the activity is paused.
     * {@link }
     */
    private Toast mToast;

    /**
     * A {@link fr.tvbarthel.apps.cameracolorpicker.data.Palettes.OnPaletteChangeListener} for updating the palette when the user change the name for instance.
     */
    private Palettes.OnPaletteChangeListener mOnPaletteChangeListener;

    /**
     * A {@link ColorItemListWrapper}.
     */
    private ColorItemListWrapper mColorItemListWrapper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette_detail);

        // ensure correct extras.
        final Intent intent = getIntent();
        if (!intent.hasExtra(EXTRA_COLOR_PALETTE) || !intent.hasExtra(EXTRA_START_BOUNDS)) {
            throw new IllegalStateException("Missing extras. Please use startWithColorPalette.");
        }

        // Retrieve the extras.
        if (savedInstanceState == null) {
            mPalette = intent.getParcelableExtra(EXTRA_COLOR_PALETTE);
        } else {
            mPalette = savedInstanceState.getParcelable(EXTRA_COLOR_PALETTE);
        }
        final Rect startBounds = intent.getParcelableExtra(EXTRA_START_BOUNDS);
        setTitle(mPalette.getName());

        // Create a rect that will be used to retrieve the stop bounds.
        final Rect stopBounds = new Rect();

        // Find the views.
        mTranslatedPreview = (PaletteView) findViewById(R.id.activity_palette_detail_preview_translating);
        mScaledPreview = (PaletteView) findViewById(R.id.activity_palette_detail_preview_scaling);
        final View listViewShadow = findViewById(R.id.activity_palette_detail_list_view_shadow);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_palette_detail_list_view);
        mColorItemListWrapper = new FlavorColorItemListWrapper(recyclerView, this);
        mColorItemListWrapper.installRecyclerView();
        mColorItemListWrapper.setItems(mPalette.getColors());

        shadowInset = getResources().getDimensionPixelSize(R.dimen.square_shadow_inset_padding);

        mTranslatedPreview.setPalette(mPalette);
        mScaledPreview.setPalette(mPalette);

        final ViewTreeObserver viewTreeObserver = mTranslatedPreview.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mTranslatedPreview.getViewTreeObserver().removeOnPreDrawListener(this);

                    mTranslatedPreview.getGlobalVisibleRect(stopBounds);
                    final float scale = startBounds.width() / (float) stopBounds.width();
                    mTranslatedPreview.setScaleX(scale);
                    mTranslatedPreview.setScaleY(scale);

                    // compute bounds again to include scale.
                    mTranslatedPreview.getGlobalVisibleRect(stopBounds);
                    final int deltaX = startBounds.left - stopBounds.left;
                    final int deltaY = startBounds.top - stopBounds.top;

                    final float scaleRatioX = (float) (stopBounds.width() - 2 * shadowInset) / (float) (mScaledPreview.getWidth());
                    final float scaleRatioY = (float) (stopBounds.height() - 2 * shadowInset) / (float) mScaledPreview.getHeight();
                    mScaledPreview.setScaleX(scaleRatioX);
                    mScaledPreview.setScaleY(scaleRatioY);
                    mScaledPreview.setVisibility(View.INVISIBLE);

                    final AnimatorSet translationAnimatorSet = new AnimatorSet();
                    translationAnimatorSet.play(ObjectAnimator.ofFloat(mTranslatedPreview, View.TRANSLATION_X, deltaX, 0))
                            .with(ObjectAnimator.ofFloat(mTranslatedPreview, View.TRANSLATION_Y, deltaY, 0));
                    translationAnimatorSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            recyclerView.setVisibility(View.VISIBLE);
                            mScaledPreview.setVisibility(View.VISIBLE);
                            listViewShadow.setVisibility(View.VISIBLE);
                            final AnimatorSet scaleAnimatorSet = new AnimatorSet();
                            scaleAnimatorSet.play(ObjectAnimator.ofFloat(mScaledPreview, View.SCALE_X, scaleRatioX, 1f))
                                    .with(ObjectAnimator.ofFloat(mScaledPreview, View.SCALE_Y, scaleRatioY, 1f))
                                    .with(ObjectAnimator.ofFloat(recyclerView, View.ALPHA, 0f, 1f))
                                    .with(ObjectAnimator.ofFloat(listViewShadow, View.ALPHA, 0f, 1f));
                            scaleAnimatorSet.start();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });

                    translationAnimatorSet.start();
                    return true;
                }
            });
        }

        mOnPaletteChangeListener = new Palettes.OnPaletteChangeListener() {
            @Override
            public void onColorPaletteChanged(List<Palette> palettes) {
                Palette newPalette = null;
                for (Palette candidate : palettes) {
                    if (candidate.getId() == mPalette.getId()) {
                        newPalette = candidate;
                        break;
                    }
                }

                if (newPalette == null) {
                    // The palette opened is not in the saved palettes.
                    // It has been deleted, just finish the activity.
                    finish();
                } else {
                    // Reload the palette.
                    mPalette = newPalette;
                    setTitle(mPalette.getName());
                    mColorItemListWrapper.setItems(mPalette.getColors());
                }
            }
        };

        Palettes.registerListener(this, mOnPaletteChangeListener);

        PaletteDetailActivityFlavor.onCreate(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_COLOR_PALETTE, mPalette);
    }

    @Override
    protected void onPause() {
        hideToast();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Palettes.unregisterListener(this, mOnPaletteChangeListener);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_palette_detail, menu);
        PaletteDetailActivityFlavor.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_palette_detail_action_delete) {
            DeletePaletteDialogFragment.newInstance(mPalette).show(getSupportFragmentManager(), null);
            return true;
        } else if (id == R.id.menu_palette_detail_action_edit) {
            return handleActionEditName();
        } else if (id == R.id.menu_palette_detail_action_share) {
            return handleActionShare();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPaletteDeletionConfirmed(@NonNull Palette paletteToDelete) {
        // Delete the palette
        // Note: we don't finish the activity, it will be finished in mOnPaletteChangeListener.
        Palettes.deleteColorPalette(this, paletteToDelete);
    }

    @Override
    public void onEditTextDialogFragmentPositiveButtonClick(int requestCode, String text) {
        if (!mPalette.getName().equals(text)) {
            // Set the new name and save the palette.
            // Note: we don't update the UI there, it will be updated in mOnPaletteChangeListener.
            mPalette.setName(text);
            Palettes.saveColorPalette(this, mPalette);
        }
    }

    @Override
    public void onEditTextDialogFragmentNegativeButtonClick(int requestCode) {
        // Nothing to do. The user aborted the edition of the name of the palette.
    }

    @Override
    public void onColorItemClicked(@NonNull ColorItem colorItem, @NonNull View colorPreview) {
        ColorDetailActivity.startWithColorItem(this, colorItem, colorPreview, mPalette);
    }

    /**
     * Handle the action 'edit' from the menu item.
     */
    private boolean handleActionEditName() {
        EditTextDialogFragment.newInstance(0,
                R.string.activity_palette_detail_edit_palette_name_dialog_title,
                R.string.activity_palette_detail_edit_palette_name_dialog_positive_action,
                android.R.string.cancel,
                getString(R.string.activity_palette_detail_edit_palette_name_dialog_hint),
                mPalette.getName()).show(getSupportFragmentManager(), null);
        return true;
    }

    /**
     * Handle the share action from the menu item.
     * <p/>
     * Create a bitmap, draw the colors of the palette and send an intent for sharing the palette.
     *
     * @return Returns true if the share action was handled correctly, false otherwise.
     */
    private boolean handleActionShare() {
        boolean handled;
        try {
            // Create a bitmap and draw the color.
            final Bitmap bitmap = Bitmap.createBitmap(SHARED_IMAGE_SIZE, SHARED_IMAGE_SIZE, Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(bitmap);
            final List<ColorItem> colorItems = mPalette.getColors();
            final float itemSize = canvas.getWidth() / ((float) colorItems.size());
            final RectF rectF = new RectF(0, 0, itemSize, canvas.getHeight());
            final Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(mPalette.getName())
                    .append("\n")
                    .append("\n");

            for (ColorItem colorItem : colorItems) {
                paint.setColor(colorItem.getColor());
                canvas.drawRect(rectF, paint);
                rectF.left = rectF.right;
                rectF.right += itemSize;
                stringBuilder.append(colorItem.getHexString())
                        .append("\n");
            }

            // Compress the bitmap before saving and sharing.
            final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, SHARED_IMAGE_QUALITY, bytes);
            bitmap.recycle();

            // Write the compressed bytes to a files
            final File outputDirectory = new File(getFilesDir(), SHARED_DIRECTORY);
            if (outputDirectory.isDirectory() || outputDirectory.mkdirs()) {
                final File shareColorFile = new File(outputDirectory, SHARED_IMAGE_FILE);
                final FileOutputStream fo = new FileOutputStream(shareColorFile);
                fo.write(bytes.toByteArray());
                fo.close();

                // Get the content uri.
                final Uri contentUri = FileProvider.getUriForFile(this,
                        FILE_PROVIDER_AUTHORITY, shareColorFile);

                // Send an intent to share the image.
                final Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, contentUri);
                intent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
                intent.setType("image/jpeg");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(intent, null));
                handled = true;
            } else {
                handled = false;
            }

        } catch (IOException e) {
            handled = false;
        }

        return handled;
    }

    /**
     * Hide the current {@link android.widget.Toast}.
     */
    private void hideToast() {
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
    private void showToast(@StringRes int resId) {
        hideToast();
        mToast = Toast.makeText(this, resId, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
