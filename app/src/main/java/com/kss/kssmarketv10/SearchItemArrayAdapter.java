package com.kss.kssmarketv10;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KSS on 30/3/2017.
 */

public class SearchItemArrayAdapter extends ArrayAdapter<itemrow_image> {
    private static final String tag = "SearchItemArrayAdapter";
    private itemrow_image itemrowImage;
    private TextView itemText;
    private ImageView itemImage;
    // private List<itemrow_image> rowList = new ArrayList<itemrow_image>();

    List<itemrow_image> rowList, tempCustomer, suggestions;

    /**
     * @param context
     * @param textViewResourceId
     * @param objects
     */
    public SearchItemArrayAdapter(Context context, int textViewResourceId, List<itemrow_image> objects) {
        super(context, textViewResourceId, objects);
        rowList = objects;
        tempCustomer = new ArrayList<itemrow_image>(objects);
        suggestions = new ArrayList<itemrow_image>(objects);
        Log.d(tag, "Search List -> journalEntryList := " + rowList.toString());
    }

    @Override
    public int getCount() {
        return this.rowList.size();
    }

    @Override
    public itemrow_image getItem(int position) {
        itemrow_image journalEntry = this.rowList.get(position);
        Log.d(tag, "*-> Retrieving JournalEntry @ position: " + String.valueOf(position) + " : " + journalEntry.toString());
        return journalEntry;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (row == null)
            row = inflater.inflate(R.layout.searchitem_image, parent, false);

        this.itemrowImage = this.rowList.get(position);
        String searchItem = this.itemrowImage.getItemTitulo();
        itemText = (TextView) row.findViewById(R.id.tvItem_src);
        itemText.setText(searchItem);
        itemImage = (ImageView) row.findViewById(R.id.ivImagen_src);
        if (this.itemrowImage.getItemImage() == null)
            itemImage.setVisibility(View.GONE);
        else
            itemImage.setImageBitmap(this.itemrowImage.getItemImage());
        return row;
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }

    Filter myFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            itemrow_image itemrowImage = (itemrow_image) resultValue;
            return itemrowImage.getItemTitulo();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (itemrow_image item : tempCustomer) {
                    if (item.getItemTitulo().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(item);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<itemrow_image> c = (ArrayList<itemrow_image>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (itemrow_image cust : c) {
                    add(cust);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
