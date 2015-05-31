package fr.tvbarthel.apps.cameracolorpicker.fragments;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.data.Palette;
import fr.tvbarthel.apps.cameracolorpicker.utils.Views;

/**
 * A simple {@link DialogFragment} with an {@link android.widget.EditText}.
 */
public class EditTextDialogFragment extends DialogFragment {

    /**
     * A Key for passing a request code.
     * <p/>
     * The request code will be passed in the callbacks methods.
     * {@link fr.tvbarthel.apps.cameracolorpicker.fragments.EditTextDialogFragment.Callback#onEditTextDialogFragmentPositiveButtonClick(int, String)}
     * {@link fr.tvbarthel.apps.cameracolorpicker.fragments.EditTextDialogFragment.Callback#onEditTextDialogFragmentNegativeButtonClick(int)}
     */
    private static final String ARG_REQUEST_CODE = "EditTextDialogFragment.Args.ARG_REQUEST_CODE";

    /**
     * A key for passing the resource id of the title.
     */
    private static final String ARG_TITLE_RESOURCE_ID = "EditTextDialogFragment.Args.ARG_TITLE_RESOURCE_ID";

    /**
     * A key for passing the resource id of the positive button.
     */
    private static final String ARG_POSITIVE_BUTTON_RESOURCE_ID = "EditTextDialogFragment.Args.ARG_POSITIVE_BUTTON_RESOURCE_ID";

    /**
     * A key for passing the resource id of the negative button.
     */
    private static final String ARG_NEGATIVE_BUTTON_RESOURCE_ID = "EditTextDialogFragment.Args.ARG_NEGATIVE_BUTTON_RESOURCE_ID";

    /**
     * A key for passing the resource id of the hint text.
     */
    private static final String ARG_EDIT_TEXT_HINT = "EditTextDialogFragment.Args.ARG_EDIT_TEXT_HINT";

    /**
     * A key for passing the initial text value.
     */
    private static final String ARG_EDIT_TEXT_INITIAL_TEXT = "EditTextDialogFragment.Args.ARG_EDIT_TEXT_INITIAL_TEXT";

    /**
     * A key for allowing empty string.
     */
    private static final String ARG_ALLOW_EMPTY_STRING = "EditTextDialogFragment.Args.ARG_ALLOW_EMPTY_STRING";

    /**
     * Create a new instance of a {@link EditTextDialogFragment} to ask the user to define the name of a {@link Palette}.
     *
     * @param requestCode              the request code
     * @param titleResourceId          the resource id of the title.
     * @param positiveButtonResourceId the resource id of the positive button.
     * @param negativeButtonResourceId the resource id of the negative button.
     * @param editTextHint             the edit text hint.
     * @param editTextInitialText      the initial text of the edit text.
     * @return the newly created {@link EditTextDialogFragment}.
     */
    public static EditTextDialogFragment newInstance(int requestCode,
                                                     @StringRes int titleResourceId,
                                                     @StringRes int positiveButtonResourceId,
                                                     @StringRes int negativeButtonResourceId,
                                                     String editTextHint,
                                                     String editTextInitialText) {
        return newInstance(requestCode, titleResourceId, positiveButtonResourceId,
                negativeButtonResourceId, editTextHint, editTextInitialText, false);
    }

    /**
     * Create a new instance of a {@link EditTextDialogFragment} to ask the user to define the name of a {@link Palette}.
     *
     * @param requestCode              the request code
     * @param titleResourceId          the resource id of the title.
     * @param positiveButtonResourceId the resource id of the positive button.
     * @param negativeButtonResourceId the resource id of the negative button.
     * @param editTextHint             the edit text hint.
     * @param editTextInitialText      the initial text of the edit text.
     * @param allowEmptyString         if true empty string will be allowed, otherwise a 'nope' animation will be played if the user tries to validate an empty string.
     * @return the newly created {@link EditTextDialogFragment}.
     */
    public static EditTextDialogFragment newInstance(int requestCode,
                                                     @StringRes int titleResourceId,
                                                     @StringRes int positiveButtonResourceId,
                                                     @StringRes int negativeButtonResourceId,
                                                     String editTextHint,
                                                     String editTextInitialText,
                                                     boolean allowEmptyString) {
        final EditTextDialogFragment instance = new EditTextDialogFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_REQUEST_CODE, requestCode);
        args.putInt(ARG_TITLE_RESOURCE_ID, titleResourceId);
        args.putInt(ARG_POSITIVE_BUTTON_RESOURCE_ID, positiveButtonResourceId);
        args.putInt(ARG_NEGATIVE_BUTTON_RESOURCE_ID, negativeButtonResourceId);
        args.putString(ARG_EDIT_TEXT_HINT, editTextHint);
        args.putString(ARG_EDIT_TEXT_INITIAL_TEXT, editTextInitialText);
        args.putBoolean(ARG_ALLOW_EMPTY_STRING, allowEmptyString);
        instance.setArguments(args);
        return instance;
    }

    /**
     * A {@link fr.tvbarthel.apps.cameracolorpicker.fragments.EditTextDialogFragment.Callback} that will be notified.
     */
    private Callback mCallback;

    /**
     * The {@link EditText}
     */
    private EditText mEditText;

    /**
     * An {@link ObjectAnimator} for playing a nope animation when the users tries to validate an empty string,
     * and mAllowEmptyString is false.
     */
    private ObjectAnimator mNopeAnimator;

    /**
     * The request code.
     */
    private int mRequestCode;

    /**
     * If true the users can validate empty string.
     */
    private boolean mAllowEmptyString;

    /**
     * Default Constructor.
     * <p/>
     * lint [ValidFragment]
     * http://developer.android.com/reference/android/app/Fragment.html#Fragment()
     * Every fragment must have an empty constructor, so it can be instantiated when restoring its activity's state.
     */
    public EditTextDialogFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Callback) {
            mCallback = (Callback) activity;
        } else {
            throw new IllegalStateException("Activity must implements EditTextDialogFragment#Callback.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle args = getArguments();

        // Ensure sane arguments.
        ensureSaneArgs(args);

        // Extract the arguments
        mRequestCode = args.getInt(ARG_REQUEST_CODE);
        mAllowEmptyString= args.getBoolean(ARG_ALLOW_EMPTY_STRING);
        final int titleResourceId = args.getInt(ARG_TITLE_RESOURCE_ID);
        final int positiveButtonResourceId = args.getInt(ARG_POSITIVE_BUTTON_RESOURCE_ID);
        final int negativeButtonResourceId = args.getInt(ARG_NEGATIVE_BUTTON_RESOURCE_ID);
        final String editTextHint = args.getString(ARG_EDIT_TEXT_HINT);
        final String editTextInitialText = args.getString(ARG_EDIT_TEXT_INITIAL_TEXT);

        final Context context = getActivity();
        final View view = LayoutInflater.from(context).inflate(R.layout.fragment_dialog_edit_text, null);

        mEditText = (EditText) view.findViewById(R.id.fragment_dialog_edit_text_edit_text);
        mEditText.setHint(editTextHint);
        mEditText.setText(editTextInitialText);
        mNopeAnimator = Views.nopeAnimation(mEditText, mEditText.getPaddingLeft());

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view)
                .setTitle(titleResourceId)
                .setCancelable(true)
                        // We don't want the positive button to always dismiss the alert dialog.
                        // The onClickListener is set in an OnShowListener bellow.
                .setPositiveButton(positiveButtonResourceId, null)
                .setNegativeButton(negativeButtonResourceId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handleNegativeClick();
                    }
                });

        // Set the positive button OnClickListener.
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                final Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handlePositiveClick();
                    }
                });
            }
        });

        // If the user pressed the ime action done.
        // We consider it is a positive click.
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    handlePositiveClick();
                    return true;
                }
                return false;
            }
        });

        return dialog;
    }

    private void handlePositiveClick() {
        final String text = mEditText.getText().toString();
        if (TextUtils.isEmpty(text) && !mAllowEmptyString) {
            if (mNopeAnimator.isRunning()) {
                mNopeAnimator.cancel();
            }
            mNopeAnimator.start();
        } else {
            mCallback.onEditTextDialogFragmentPositiveButtonClick(mRequestCode, text);
            getDialog().dismiss();
        }
    }

    private void handleNegativeClick() {
        mCallback.onEditTextDialogFragmentNegativeButtonClick(mRequestCode);
    }

    /**
     * Check if all the required arguments are present in the given {@link Bundle}.
     *
     * @param args the {@link Bundle} representing the arguments of the fragment.
     */
    private void ensureSaneArgs(Bundle args) {
        if (args == null) {
            throw new IllegalArgumentException("Args can't be null.");
        }

        if (!args.containsKey(ARG_REQUEST_CODE)) {
            throw new IllegalArgumentException("Missing request code. Please use EditTextDialogFragment#newInstance()");
        }

        if (!args.containsKey(ARG_TITLE_RESOURCE_ID)) {
            throw new IllegalArgumentException("Missing title resource id. Please use EditTextDialogFragment#newInstance()");
        }

        if (!args.containsKey(ARG_POSITIVE_BUTTON_RESOURCE_ID)) {
            throw new IllegalArgumentException("Missing positive button resource id. Please use EditTextDialogFragment#newInstance()");
        }

        if (!args.containsKey(ARG_NEGATIVE_BUTTON_RESOURCE_ID)) {
            throw new IllegalArgumentException("Missing negative button resource id. Please use EditTextDialogFragment#newInstance()");
        }

        if (!args.containsKey(ARG_EDIT_TEXT_HINT)) {
            throw new IllegalArgumentException("Missing edit text hint resource id. Please use EditTextDialogFragment#newInstance()");
        }

        if (!args.containsKey(ARG_EDIT_TEXT_INITIAL_TEXT)) {
            throw new IllegalArgumentException("Missing edit text initial text. Please use EditTextDialogFragment#newInstance()");
        }

        if (!args.containsKey(ARG_ALLOW_EMPTY_STRING)) {
            throw new IllegalArgumentException("Missing edit text initial text. Please use EditTextDialogFragment#newInstance()");
        }
    }

    /**
     * A simple interface for the callbacks of {@link EditTextDialogFragment}s.
     */
    public interface Callback {

        /**
         * Called when the user has just clicked on the positive button.
         *
         * @param requestCode the request code passed in the {@link EditTextDialogFragment#newInstance(int, int, int, int, String, String)} method.
         * @param text        the text of the edit text.
         */
        void onEditTextDialogFragmentPositiveButtonClick(int requestCode, String text);

        /**
         * Called when the user has just clicked the negative button.
         *
         * @param requestCode the request code passed in the {@link EditTextDialogFragment#newInstance(int, int, int, int, String, String)} method.
         */
        void onEditTextDialogFragmentNegativeButtonClick(int requestCode);
    }
}
