package fr.tvbarthel.apps.cameracolorpicker.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItems;
import fr.tvbarthel.apps.cameracolorpicker.data.Palette;
import fr.tvbarthel.apps.cameracolorpicker.data.Palettes;
import fr.tvbarthel.apps.cameracolorpicker.fragments.DeleteColorDialogFragment;
import fr.tvbarthel.apps.cameracolorpicker.fragments.EditTextDialogFragment;
import fr.tvbarthel.apps.cameracolorpicker.utils.ClipDatas;

public class ColorDetailActivity extends AppCompatActivity implements View.OnClickListener,
        DeleteColorDialogFragment.Callback, EditTextDialogFragment.Callback {

    /**
     * A key for passing a color item as extra.
     */
    private static final String EXTRA_COLOR_ITEM = "ColorDetailActivity.Extras.EXTRA_COLOR_ITEM";

    /**
     * A key for passing the global visible rect of the clicked color preview clicked.
     */
    private static final String EXTRA_START_BOUNDS = "ColorDetailActivity.Extras.EXTRA_START_BOUNDS";

    /**
     * A key for passing an optional palette that is associated with the color item displayed.
     */
    private static final String EXTRA_PALETTE = "ColorDetailActivity.Extras.EXTRA_PALETTE";

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
    private static final String SHARED_DIRECTORY = "colors";

    /**
     * The name of the file used to write the shared image.
     */
    private static final String SHARED_IMAGE_FILE = "shared_colors.jpg";

    /**
     * The authority of the file provider declared in our manifest.
     */
    private static final String FILE_PROVIDER_AUTHORITY = ".fileprovider";

    /**
     * A request code to use in {@link EditTextDialogFragment#newInstance(int, int, int, int, String, String, boolean)}.
     */
    private static final int REQUEST_CODE_EDIT_COLOR_ITEM_NAME = 15;

    public static void startWithColorItem(Context context, ColorItem colorItem,
                                          View colorPreviewClicked) {
        startWithColorItem(context, colorItem, colorPreviewClicked, null);
    }

    public static void startWithColorItem(Context context, ColorItem colorItem,
                                          View colorPreviewClicked, Palette palette) {
        final boolean isActivity = context instanceof Activity;
        final Rect startBounds = new Rect();
        colorPreviewClicked.getGlobalVisibleRect(startBounds);

        final Intent intent = new Intent(context, ColorDetailActivity.class);
        intent.putExtra(EXTRA_COLOR_ITEM, colorItem);
        intent.putExtra(EXTRA_START_BOUNDS, startBounds);
        intent.putExtra(EXTRA_PALETTE, palette);

        if (!isActivity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);

        if (isActivity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }

    /**
     * A {@link android.view.View} for showing the color preview being translated during the start animation from the clicked position to the right location.
     */
    private View mTranslatedPreview;

    /**
     * A {@link android.view.View} for showing the color preview being scaled during the start animation to fill the preview container.
     */
    private View mScaledPreview;

    /**
     * A {@link android.widget.TextView} for showing the hexadecimal value of the color.
     */
    private TextView mHex;

    /**
     * A {@link android.widget.TextView} for showing the RGB value of the color.
     */
    private TextView mRgb;

    /**
     * A {@link android.widget.TextView} for showing the HSV value of the color.
     */
    private TextView mHsv;

    /**
     * A reference to the current {@link android.widget.Toast}.
     * <p/>
     * Used for hiding the current {@link android.widget.Toast} before showing a new one or the activity is paused.
     * {@link }
     */
    private Toast mToast;

    /**
     * The {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem} being displayed.
     */
    private ColorItem mColorItem;

    /**
     * An optional {@link Palette} that is associated with the {@link ColorItem}.
     */
    private Palette mPalette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // ensure correct extras.
        final Intent intent = getIntent();
        if (!intent.hasExtra(EXTRA_COLOR_ITEM) || !intent.hasExtra(EXTRA_START_BOUNDS)
                || !intent.hasExtra(EXTRA_PALETTE)) {
            throw new IllegalStateException("Missing extras. Please use startWithColorItem.");
        }

        // Retrieve the extras.
        final Rect startBounds = intent.getParcelableExtra(EXTRA_START_BOUNDS);
        if (savedInstanceState == null) {
            mColorItem = intent.getParcelableExtra(EXTRA_COLOR_ITEM);
            mPalette = intent.getParcelableExtra(EXTRA_PALETTE);
        } else {
            mColorItem = savedInstanceState.getParcelable(EXTRA_COLOR_ITEM);
            mPalette = savedInstanceState.getParcelable(EXTRA_PALETTE);
        }

        // Set the title of the activity with the name of the color, if not null.
        if (!TextUtils.isEmpty(mColorItem.getName())) {
            setTitle(mColorItem.getName());
        } else {
            setTitle(mColorItem.getHexString());
        }

        // Create a rect that will be used to retrieve the stop bounds.
        final Rect stopBounds = new Rect();

        // Find the views.
        mTranslatedPreview = findViewById(R.id.activity_color_detail_preview_translating);
        mScaledPreview = findViewById(R.id.activity_color_detail_preview_scaling);
        mHex = (TextView) findViewById(R.id.activity_color_detail_hex);
        mRgb = (TextView) findViewById(R.id.activity_color_detail_rgb);
        mHsv = (TextView) findViewById(R.id.activity_color_detail_hsv);

        // Set the click listeners.
        mHex.setOnClickListener(this);
        mRgb.setOnClickListener(this);
        mHsv.setOnClickListener(this);

        // Display the color item data.
        mTranslatedPreview.getBackground().setColorFilter(mColorItem.getColor(), PorterDuff.Mode.MULTIPLY);
        mScaledPreview.getBackground().setColorFilter(mColorItem.getColor(), PorterDuff.Mode.MULTIPLY);
        mHex.setText(mColorItem.getHexString());
        mRgb.setText(mColorItem.getRgbString());
        mHsv.setText(mColorItem.getHsvString());

        final View previewContainer = findViewById(R.id.activity_color_detail_preview_container);
        final ViewTreeObserver viewTreeObserver = previewContainer.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    previewContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                    mTranslatedPreview.getGlobalVisibleRect(stopBounds);
                    final int deltaX = startBounds.left - stopBounds.left;
                    final int deltaY = startBounds.top - stopBounds.top;
                    final float scale = startBounds.width() / stopBounds.width();
                    mTranslatedPreview.setScaleX(scale);
                    mTranslatedPreview.setScaleY(scale);
                    final AnimatorSet translationAnimatorSet = new AnimatorSet();
                    translationAnimatorSet.play(ObjectAnimator.ofFloat(mTranslatedPreview, View.TRANSLATION_X, deltaX, 0))
                            .with(ObjectAnimator.ofFloat(mTranslatedPreview, View.TRANSLATION_Y, deltaY, 0));
                    translationAnimatorSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mScaledPreview.setVisibility(View.VISIBLE);
                            mHex.setVisibility(View.VISIBLE);
                            mRgb.setVisibility(View.VISIBLE);
                            mHsv.setVisibility(View.VISIBLE);
                            final float maxContainerSize = (float) Math.sqrt(Math.pow(previewContainer.getWidth(), 2) + Math.pow(previewContainer.getHeight(), 2));
                            final float maxSize = Math.max(mScaledPreview.getWidth(), mScaledPreview.getHeight());
                            final float scaleRatio = maxContainerSize / maxSize;
                            final AnimatorSet scaleAnimatorSet = new AnimatorSet();
                            scaleAnimatorSet.play(ObjectAnimator.ofFloat(mScaledPreview, View.SCALE_X, 1f, scaleRatio))
                                    .with(ObjectAnimator.ofFloat(mScaledPreview, View.SCALE_Y, 1f, scaleRatio))
                                    .with(ObjectAnimator.ofFloat(mHex, View.ALPHA, 0f, 1f))
                                    .with(ObjectAnimator.ofFloat(mRgb, View.ALPHA, 0f, 1f))
                                    .with(ObjectAnimator.ofFloat(mHsv, View.ALPHA, 0f, 1f));
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

        ColorDetailActivityFlavor.onCreate(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideToast();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_COLOR_ITEM, mColorItem);
        outState.putParcelable(EXTRA_PALETTE, mPalette);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_color_detail, menu);
        if (mPalette != null) {
            // A color associated with a palette can't be deleted.
            menu.removeItem(R.id.menu_color_detail_action_delete);
        }
        ColorDetailActivityFlavor.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_color_detail_action_delete) {
            DeleteColorDialogFragment.newInstance(mColorItem).show(getSupportFragmentManager(), null);
            return true;
        } else if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.menu_color_detail_action_share) {
            return handleActionShare(this);
        } else if (id == R.id.menu_color_detail_action_edit) {
            EditTextDialogFragment.newInstance(REQUEST_CODE_EDIT_COLOR_ITEM_NAME,
                    R.string.activity_color_detail_edit_text_dialog_fragment_title,
                    R.string.activity_color_detail_edit_text_dialog_fragment_positive_button,
                    android.R.string.cancel,
                    mColorItem.getHexString(),
                    mColorItem.getName(), true).show(getSupportFragmentManager(), null);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();

        switch (viewId) {
            case R.id.activity_color_detail_hex:
                clipColor(R.string.color_clip_color_label_hex, mHex.getText());
                break;

            case R.id.activity_color_detail_rgb:
                clipColor(R.string.color_clip_color_label_rgb, mRgb.getText());
                break;

            case R.id.activity_color_detail_hsv:
                clipColor(R.string.color_clip_color_label_hsv, mHsv.getText());
                break;

            default:
                throw new IllegalArgumentException("Unsupported view clicked. Found: " + view);
        }
    }

    @Override
    public void onColorDeletionConfirmed(@NonNull ColorItem colorItemToDelete) {
        if (ColorItems.deleteColorItem(this, colorItemToDelete)) {
            finish();
        }
    }

    @Override
    public void onEditTextDialogFragmentPositiveButtonClick(int requestCode, String text) {
        if (requestCode == REQUEST_CODE_EDIT_COLOR_ITEM_NAME) {
            // Update the title of the activity.
            if (TextUtils.isEmpty(text)) {
                setTitle(mColorItem.getHexString());
            } else {
                setTitle(text);
            }

            // Set the new name.
            mColorItem.setName(text);

            // Persist the change.
            if (mPalette == null) {
                // The color item is a standalone color.
                // It's not associated with a palette.
                // Just save the color item.
                ColorItems.saveColorItem(this, mColorItem);
            } else {
                // The color item is associated with a palette.
                // Edit and save the palette.
                final List<ColorItem> colorItems = mPalette.getColors();
                for (ColorItem candidate : colorItems) {
                    if (candidate.getId() == mColorItem.getId()) {
                        candidate.setName(text);
                        break;
                    }
                }
                Palettes.saveColorPalette(this, mPalette);
            }
        }
    }

    @Override
    public void onEditTextDialogFragmentNegativeButtonClick(int requestCode) {
        // nothing to do here.
    }


    protected void clipColor(int labelResourceId, CharSequence colorString) {
        ClipDatas.clipPainText(this, getString(labelResourceId), colorString);
        showToast(R.string.color_clip_success_copy_message);
    }


    /**
     * Hide the current {@link android.widget.Toast}.
     */
    protected void hideToast() {
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
    protected void showToast(int resId) {
        hideToast();
        mToast = Toast.makeText(this, resId, Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * Handle the share action from the menu item.
     * <p/>
     * Create a bitmap, draw the color and send an intent for sharing the color.
     *
     * @param context context used to initialize internal component.
     * @return Returns true if the share action was handled correctly, false otherwise.
     */
    private boolean handleActionShare(Context context) {
        boolean handled;
        try {
            // Create a bitmap and draw the color.
            final Bitmap bitmap = Bitmap.createBitmap(SHARED_IMAGE_SIZE, SHARED_IMAGE_SIZE, Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(mColorItem.getColor());

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
                        context.getPackageName() + FILE_PROVIDER_AUTHORITY, shareColorFile);

                // Send an intent to share the image.
                final Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, contentUri);
                intent.putExtra(Intent.EXTRA_TEXT, mColorItem.getHexString() + "\n"
                        + mColorItem.getRgbString() + "\n" + mColorItem.getHsvString());
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
}
