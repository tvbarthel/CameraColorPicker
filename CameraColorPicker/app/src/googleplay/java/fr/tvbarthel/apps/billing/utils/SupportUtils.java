package fr.tvbarthel.apps.billing.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.vending.billing.util.IabHelper;
import com.android.vending.billing.util.IabResult;
import com.android.vending.billing.util.Inventory;

import fr.tvbarthel.apps.cameracolorpicker.R;


public class SupportUtils {

    /**
     * Shared preferences key to store support action
     */
    public static final String SUPPORT_SHARED_KEY = "shared_preferences_support";

    /**
     * Shared preferences value for fresh install
     */
    public static final int SUPPORT_UNSET = 0x00000000;

    /**
     * Shared preferences value for supporter
     */
    public static final int SUPPORT_DONATE = 0x00000001;

    /**
     * Shared preferences value for future supporter
     */
    public static final int SUPPORT_NOT_YET = 0x00000002;

    /**
     * id for espresso product
     */
    public static final String SKU_ESPRESSO = "espresso";

    /**
     * id for cappuccino product
     */
    public static final String SKU_CAPPUCCINO = "cappuccino";

    /**
     * id for iced coffee product
     */
    public static final String SKU_ICED_COFFEE = "iced_coffee";

    /**
     * id for earl grey product
     */
    public static final String SKU_EARL_GREY = "earl_grey";

    /**
     * arbitrary request code for the purchase flow
     */
    public static final int REQUEST_CODE_SUPPORT_DEV = 42;

    /**
     * state for a purchased purchase
     * http://developer.android.com/google/play/billing/v2/billing_reference.html
     */
    public static final int SUPPORT_STATE_PURCHASED = 0;

    /**
     * check if user has already supported us
     *
     * @param i user inventory
     * @return true if already purchased
     */
    private static boolean hasPurchased(final Inventory i) {
        if (i.hasPurchase(SKU_ESPRESSO) && i.getPurchase(SKU_ESPRESSO).getPurchaseState() == SUPPORT_STATE_PURCHASED) {
            return true;
        }

        if (i.hasPurchase(SKU_CAPPUCCINO) && i.getPurchase(SKU_CAPPUCCINO).getPurchaseState() == SUPPORT_STATE_PURCHASED) {
            return true;
        }

        if (i.hasPurchase(SKU_ICED_COFFEE) && i.getPurchase(SKU_ICED_COFFEE).getPurchaseState() == SUPPORT_STATE_PURCHASED) {
            return true;
        }

        if (i.hasPurchase(SKU_EARL_GREY) && i.getPurchase(SKU_EARL_GREY).getPurchaseState() == SUPPORT_STATE_PURCHASED) {
            return true;
        }

        return false;
    }


    /**
     * Check is a user is supporting us.
     *
     * @param context  a {@link android.content.Context}
     * @param listener a {@link fr.tvbarthel.apps.billing.utils.SupportUtils.OnCheckSupportListener} used to deliver the response.
     */
    public static void checkSupport(Context context, final OnCheckSupportListener listener) {
        final SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        final int donateState = sharedPreferences.getInt(SupportUtils.SUPPORT_SHARED_KEY,
                SupportUtils.SUPPORT_UNSET);
        switch (donateState) {
            case SupportUtils.SUPPORT_UNSET:
                //retrieve info
                final IabHelper helper = new IabHelper(context,
                        context.getResources().getString(R.string.support_key));
                helper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                    @Override
                    public void onIabSetupFinished(IabResult result) {
                        if (result.isSuccess()) {
                            helper.queryInventoryAsync(new IabHelper.QueryInventoryFinishedListener() {
                                @Override
                                public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                                    if (result.isSuccess()) {
                                        if (SupportUtils.hasPurchased(inv)) {
                                            if(listener != null) {
                                                listener.onCheckSupport(true);
                                            }

                                            //save it
                                            final SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putInt(SupportUtils.SUPPORT_SHARED_KEY, SupportUtils.SUPPORT_DONATE);
                                            editor.commit();

                                            //release resources
                                            helper.dispose();
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
                break;
            case SupportUtils.SUPPORT_DONATE:
                //user already support us
                if(listener != null) {
                    listener.onCheckSupport(true);
                }
                break;
            case SupportUtils.SUPPORT_NOT_YET:
                if(listener != null) {
                    listener.onCheckSupport(false);
                }
                break;
            default:
                if(listener != null) {
                    listener.onCheckSupport(false);
                }
        }
    }

    public static interface OnCheckSupportListener {
        /**
         * Called after we know if the user is supporting us or not.
         *
         * @param supporting true if the user is supporting us, false otherwise.
         */
        void onCheckSupport(boolean supporting);
    }

}
