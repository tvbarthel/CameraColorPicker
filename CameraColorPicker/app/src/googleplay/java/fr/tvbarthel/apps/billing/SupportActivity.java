package fr.tvbarthel.apps.billing;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.util.IabHelper;
import com.android.vending.billing.util.IabResult;
import com.android.vending.billing.util.Inventory;
import com.android.vending.billing.util.Purchase;
import com.android.vending.billing.util.SkuDetails;

import java.util.ArrayList;
import java.util.List;

import fr.tvbarthel.apps.billing.adapter.SupportAdapter;
import fr.tvbarthel.apps.billing.model.CoffeeEntry;
import fr.tvbarthel.apps.billing.model.CoffeeEntryFactory;
import fr.tvbarthel.apps.billing.utils.SupportUtils;
import fr.tvbarthel.apps.cameracolorpicker.R;


public class SupportActivity extends AppCompatActivity {

    /**
     * dev purpose
     */
    private static final String TAG = SupportActivity.class.getName();

    /**
     * helper for in app billing
     */
    private IabHelper mIabHelper;

    /**
     * used to manage Toast
     */
    private Toast mToast;

    /**
     * Listener for purchase finished callback
     */
    private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener;

    /**
     * Listener for purchase list query
     */
    private IabHelper.QueryInventoryFinishedListener mQueryInventoryListener;

    /**
     * Listener for purchase consumption when already owned
     */
    private IabHelper.OnConsumeFinishedListener mConsumeListener;

    /**
     * list view for coffee entry
     */
    private ListView mCoffeeListView;

    /**
     * adapter for coffee entry
     */
    private SupportAdapter mCoffeeAdapter;

    /**
     * loader displayed during data recovering
     */
    private ProgressBar mLoader;

    /**
     * TextView used to display info when errors occur
     */
    private TextView mErrorPlaceholder;

    /**
     * Remember the selected item since purchase == null when already owned
     */
    private int mSelectedPurchase;

    /**
     * Purchase list
     */
    private ArrayList<Purchase> mPurchaseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get ui components
        mCoffeeListView = (ListView) findViewById(R.id.support_purchase_list);
        mLoader = (ProgressBar) findViewById(R.id.support_progressbar);
        mErrorPlaceholder = (TextView) findViewById(R.id.support_error_placeholder);

        //init coffee adapter
        mCoffeeAdapter = new SupportAdapter(getBaseContext(), new ArrayList<CoffeeEntry>());

        //set adapter
        mCoffeeListView.setAdapter(mCoffeeAdapter);
        mCoffeeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedPurchase = position;
                mIabHelper.launchPurchaseFlow(SupportActivity.this,
                        mCoffeeAdapter.getItem(position).getSkuDetails().getSku(),
                        SupportUtils.REQUEST_CODE_SUPPORT_DEV,
                        mPurchaseFinishedListener);
            }
        });

        String base64EncodedPublicKey = getResources().getString(R.string.support_key);
        mIabHelper = new IabHelper(this, base64EncodedPublicKey);

        //dev purpose
        mIabHelper.enableDebugLogging(true);

        //Iab not started
        mSelectedPurchase = -1;
        mPurchaseList = new ArrayList<Purchase>();

        //init listener for purchase callback
        initPurchaseListener();

        //init listener for inventory callback
        initInventoryListener();

        //init listener for purchase consumption
        initConsumeListener();

        //start helper
        startIabHelper();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mIabHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mIabHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // very important:
        if (mIabHelper != null) {
            mIabHelper.dispose();
            mIabHelper = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * use to don't have toast queue
     *
     * @param message toast content
     */
    private void makeToast(String message) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        mToast.show();
    }

    /**
     * request asynchronously the coffee list
     */
    private void requestCoffeeList() {
        //retrieve purchase list
        List<String> inventory = new ArrayList<String>(4);
        inventory.add(SupportUtils.SKU_ESPRESSO);
        inventory.add(SupportUtils.SKU_CAPPUCCINO);
        inventory.add(SupportUtils.SKU_ICED_COFFEE);
        inventory.add(SupportUtils.SKU_EARL_GREY);
        mIabHelper.queryInventoryAsync(true, inventory, mQueryInventoryListener);
    }

    /**
     * init purchase listener for async purchase request
     */
    private void initPurchaseListener() {
        mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
            @Override
            public void onIabPurchaseFinished(IabResult result, Purchase info) {
                // if we were disposed of in the meantime, quit.
                if (mIabHelper == null) return;

                //if success, thanks the user
                if (result.isSuccess()) {
                    purchaseSuccess(info);
                } else if (result.getResponse() == IabHelper.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED) {
                    //if purchase already owned consume it
                    if (mPurchaseList.get(mSelectedPurchase) != null) {
                        //workaround to get the purchase since info == null when already owned
                        mIabHelper.consumeAsync(mPurchaseList.get(mSelectedPurchase), mConsumeListener);
                    }
                }
            }
        };
    }

    /**
     * init callback for purchase consumption when purchase is already owned
     */
    private void initConsumeListener() {
        mConsumeListener = new IabHelper.OnConsumeFinishedListener() {
            @Override
            public void onConsumeFinished(Purchase purchase, IabResult result) {
                //if consume or not owned (due to cache from API v3, not owned purchase can be
                // asked for consumption) buy product again
                if (result.isSuccess() ||
                        result.getResponse() == IabHelper.BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED) {
                    mIabHelper.launchPurchaseFlow(SupportActivity.this,
                            purchase.getSku(),
                            SupportUtils.REQUEST_CODE_SUPPORT_DEV,
                            mPurchaseFinishedListener);
                }
            }
        };
    }

    /**
     * init inventory callback for async purchase list request
     */
    private void initInventoryListener() {
        mQueryInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
            @Override
            public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                if (result.isFailure()) {
                    makeToast("Fail to load coffee list, check your internet connection.");
                    mLoader.setVisibility(View.GONE);
                    mErrorPlaceholder.setVisibility(View.VISIBLE);
                    return;
                }
                if (inv != null) {
                    //clear the list
                    mCoffeeAdapter.clear();
                    mPurchaseList.clear();

                    //get espresso details
                    if (inv.hasDetails(SupportUtils.SKU_ESPRESSO)) {
                        SkuDetails espressoDetails = inv.getSkuDetails(SupportUtils.SKU_ESPRESSO);

                        //add espresso to the coffee list
                        mCoffeeAdapter.add(CoffeeEntryFactory.createEspressoEntry(espressoDetails));
                        mPurchaseList.add(inv.getPurchase(SupportUtils.SKU_ESPRESSO));
                    }

                    //get earl grey details
                    if (inv.hasDetails(SupportUtils.SKU_EARL_GREY)) {
                        SkuDetails earlGreyDetails = inv.getSkuDetails(SupportUtils.SKU_EARL_GREY);

                        //add earl grey to the coffee list
                        mCoffeeAdapter.add(CoffeeEntryFactory.createEarlGreyEntry(earlGreyDetails));
                        mPurchaseList.add(inv.getPurchase(SupportUtils.SKU_EARL_GREY));
                    }

                    //get cappuccino details
                    if (inv.hasDetails(SupportUtils.SKU_CAPPUCCINO)) {
                        SkuDetails cappuccinoDetails = inv.getSkuDetails(SupportUtils.SKU_CAPPUCCINO);

                        //add cappuccino to the coffee list
                        mCoffeeAdapter.add(CoffeeEntryFactory.createCappuccinoEntry(cappuccinoDetails));
                        mPurchaseList.add(inv.getPurchase(SupportUtils.SKU_CAPPUCCINO));
                    }

                    //get iced coffee details
                    if (inv.hasDetails(SupportUtils.SKU_ICED_COFFEE)) {
                        SkuDetails icedCoffeeDetails = inv.getSkuDetails(SupportUtils.SKU_ICED_COFFEE);

                        //add iced coffee to the coffee list
                        mCoffeeAdapter.add(CoffeeEntryFactory.createIcedCoffeeEntry(icedCoffeeDetails));
                        mPurchaseList.add(inv.getPurchase(SupportUtils.SKU_ICED_COFFEE));
                    }
                } else {
                    //inv empty
                }

                //hide loader
                mLoader.setVisibility(View.GONE);

                //show list
                mCoffeeListView.setVisibility(View.VISIBLE);

                //update list view
                mCoffeeAdapter.notifyDataSetChanged();
            }
        };
    }

    /**
     * setup IabHelper asynchronously
     */
    private void startIabHelper() {
        mIabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    makeToast("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mIabHelper == null) return;

                //retrieve coffee list
                requestCoffeeList();
            }
        });
    }

    /**
     * dialog to thanks user for his support
     */
    private void purchaseSuccess(Purchase info) {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setMessage(String.format(
                getResources().getString(R.string.support_thanks), info.getSku()));
        build.setPositiveButton(R.string.support_thanks_positive_btn, null);
        build.create().show();

        //save support act in shared preferences
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SupportUtils.SUPPORT_SHARED_KEY, SupportUtils.SUPPORT_DONATE);
        editor.commit();
    }
}
