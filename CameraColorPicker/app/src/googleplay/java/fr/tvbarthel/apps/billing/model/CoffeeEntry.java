package fr.tvbarthel.apps.billing.model;

import com.android.vending.billing.util.SkuDetails;

public class CoffeeEntry {

    /**
     * details from play store
     */
    private SkuDetails mSkuDetails;

    /**
     * percentage of caffeine
     */
    private int mCoffeeRate;

    /**
     * percentage of energy
     */
    private int mEnergyRate;

    /**
     * percentage of "candyness"
     */
    private int mCandyRate;

    /**
     * create a coffee entry
     *
     * @param skuDetails details from play store
     * @param coffeeRate caffeine rate
     * @param energyRate energy rate
     * @param candyRate  candyness rate
     */
    public CoffeeEntry(SkuDetails skuDetails, int coffeeRate, int energyRate, int candyRate) {
        mSkuDetails = skuDetails;
        mCoffeeRate = coffeeRate;
        mEnergyRate = energyRate;
        mCandyRate = candyRate;
    }

    /**
     * GETTER
     */
    public SkuDetails getSkuDetails() {
        return mSkuDetails;
    }

    public int getCaffeineRate() {
        return mCoffeeRate;
    }

    public int getEnergyRate() {
        return mEnergyRate;
    }

    public int getCandyRate() {
        return mCandyRate;
    }
}
