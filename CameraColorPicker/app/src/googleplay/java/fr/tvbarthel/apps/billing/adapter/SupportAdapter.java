package fr.tvbarthel.apps.billing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.vending.billing.util.SkuDetails;

import java.util.List;

import fr.tvbarthel.apps.billing.model.CoffeeEntry;
import fr.tvbarthel.apps.cameracolorpicker.R;


public class SupportAdapter extends ArrayAdapter<CoffeeEntry> {

    /**
     * create an adapter for support purchase list
     *
     * @param context
     * @param objects
     */
    public SupportAdapter(Context context, List<CoffeeEntry> objects) {
        super(context, R.layout.support_entry, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        final CoffeeEntry currentEntry = getItem(position);
        final SkuDetails currentSku = currentEntry.getSkuDetails();
        if (rowView == null) {
            final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.support_entry, parent, false);

            //configure view holder
            CoffeeEntryHolder viewHolder = new CoffeeEntryHolder();
            viewHolder.title =
                    ((TextView) rowView.findViewById(R.id.support_entry_title));
            viewHolder.description =
                    ((TextView) rowView.findViewById(R.id.support_entry_description));
            viewHolder.price =
                    ((TextView) rowView.findViewById(R.id.support_entry_price));
            viewHolder.currency =
                    ((TextView) rowView.findViewById(R.id.support_entry_currency));
            viewHolder.coffeeBar =
                    ((ProgressBar) rowView.findViewById(R.id.support_detail_coffee_bar));
            viewHolder.coffeeValue =
                    ((TextView) rowView.findViewById(R.id.support_detail_coffee_value));
            viewHolder.energyBar =
                    ((ProgressBar) rowView.findViewById(R.id.support_detail_energy_bar));
            viewHolder.energyValue =
                    ((TextView) rowView.findViewById(R.id.support_detail_energy_value));
            viewHolder.candyBar =
                    ((ProgressBar) rowView.findViewById(R.id.support_detail_candy_bar));
            viewHolder.candyValue =
                    ((TextView) rowView.findViewById(R.id.support_detail_candy_value));
            rowView.setTag(viewHolder);
        }

        CoffeeEntryHolder holder = (CoffeeEntryHolder) rowView.getTag();

        final String title = currentSku.getTitle();
        holder.title.setText(title.substring(0, title.indexOf("(")));
        holder.description.setText(currentSku.getDescription());
        final String price = currentSku.getPrice();
        final String amount = price.substring(0, price.length() - 1);
        final String currency = price.substring(price.length() - 1, price.length());
        holder.price.setText(amount);
        holder.currency.setText(currency);

        //set bar value
        holder.coffeeBar.setProgress(currentEntry.getCaffeineRate());
        holder.coffeeValue.setText(getContext().getString(R.string.support_bar_text,
                currentEntry.getCaffeineRate()));
        holder.energyBar.setProgress(currentEntry.getEnergyRate());
        holder.energyValue.setText(getContext().getString(R.string.support_bar_text,
                currentEntry.getEnergyRate()));
        holder.candyBar.setProgress(currentEntry.getCandyRate());
        holder.candyValue.setText(getContext().getString(R.string.support_bar_text,
                currentEntry.getCandyRate()));

        return rowView;
    }

    /**
     * View Holder pattern
     */
    static class CoffeeEntryHolder {
        public TextView title;
        public TextView description;
        public TextView price;
        public TextView currency;
        public TextView coffeeValue;
        public TextView energyValue;
        public TextView candyValue;
        public ProgressBar coffeeBar;
        public ProgressBar energyBar;
        public ProgressBar candyBar;
    }
}
