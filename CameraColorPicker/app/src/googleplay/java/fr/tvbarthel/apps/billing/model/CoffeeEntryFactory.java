package fr.tvbarthel.apps.billing.model;

import com.android.vending.billing.util.SkuDetails;

public class CoffeeEntryFactory {

    /**
     * Espresso values
     */
    private static final int ESPRESSO_CAFFEINE_RATE = 75;
    private static final int ESPRESSO_ENERGY_RATE = 50;
    private static final int ESPRESSO_CANDYNESS_RATE = 5;

    /**
     * Earl Grey values
     */
    private static final int EARL_GREY_CAFFEINE_RATE = 0;
    private static final int EARL_GREY_ENERGY_RATE = 70;
    private static final int EARL_GREY_CANDYNESS_RATE = 35;

    /**
     * Cappuccino values
     */
    private static final int CAPPUCCINO_CAFFEINE_RATE = 50;
    private static final int CAPPUCCINO_ENERGY_RATE = 40;
    private static final int CAPPUCCINO_CANDYNESS_RATE = 45;

    /**
     * Iced Coffee values
     */
    private static final int ICED_COFFEE_CAFFEINE_RATE = 55;
    private static final int ICED_COFFEE_ENERGY_RATE = 45;
    private static final int ICED_COFFEE_CANDYNESS_RATE = 80;


    /**
     * create espresso entry
     *
     * @param espressoDetails
     * @return
     */
    public static CoffeeEntry createEspressoEntry(SkuDetails espressoDetails) {
        return new CoffeeEntry(
                espressoDetails,
                ESPRESSO_CAFFEINE_RATE,
                ESPRESSO_ENERGY_RATE,
                ESPRESSO_CANDYNESS_RATE);
    }

    /**
     * create earl grey entry
     *
     * @param earlGreyDetails
     * @return
     */
    public static CoffeeEntry createEarlGreyEntry(SkuDetails earlGreyDetails) {
        return new CoffeeEntry(
                earlGreyDetails,
                EARL_GREY_CAFFEINE_RATE,
                EARL_GREY_ENERGY_RATE,
                EARL_GREY_CANDYNESS_RATE);
    }

    /**
     * create cappuccino entry
     *
     * @param cappuccinoDetails
     * @return
     */
    public static CoffeeEntry createCappuccinoEntry(SkuDetails cappuccinoDetails) {
        return new CoffeeEntry(
                cappuccinoDetails,
                CAPPUCCINO_CAFFEINE_RATE,
                CAPPUCCINO_ENERGY_RATE,
                CAPPUCCINO_CANDYNESS_RATE);
    }

    /**
     * create iced coffee entry
     *
     * @param icedCoffeeDetails
     * @return
     */
    public static CoffeeEntry createIcedCoffeeEntry(SkuDetails icedCoffeeDetails) {
        return new CoffeeEntry(
                icedCoffeeDetails,
                ICED_COFFEE_CAFFEINE_RATE,
                ICED_COFFEE_ENERGY_RATE,
                ICED_COFFEE_CANDYNESS_RATE);
    }
}
