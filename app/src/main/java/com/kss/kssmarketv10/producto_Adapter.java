package com.kss.kssmarketv10;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by KSS on 30/3/2017.
 */

public class producto_Adapter extends ArrayAdapter<itemrow_image> {

    ArrayList<itemrow_image> itemrow_images, tempproducto_row, suggestions;

    public producto_Adapter(Context context, ArrayList<itemrow_image> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.itemrow_images = objects;
        this.tempproducto_row = new ArrayList<itemrow_image>(objects);
        this.suggestions = new ArrayList<itemrow_image>(objects);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        itemrow_image itemrow_image = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.producto_row, parent, false);
        }
        TextView txtproducto_row = (TextView) convertView.findViewById(R.id.tvCustomer);

        ImageView ivproducto_rowImage = (ImageView) convertView.findViewById(R.id.ivCustomerImage);
        if (txtproducto_row != null)
            txtproducto_row.setText(itemrow_image.getItemTitulo() );
        if (ivproducto_rowImage != null && itemrow_image.getItemImage() != null)
            ivproducto_rowImage.setImageBitmap(itemrow_image.getItemImage());
// Now assign alternate color for rows
        if (position % 2 == 0)
            convertView.setBackgroundColor(getContext().getColor(R.color.odd));
        else
            convertView.setBackgroundColor(getContext().getColor(R.color.even));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }

    Filter myFilter = new Filter() {
        @Override


        public CharSequence convertResultToString(Object resultValue) {
            itemrow_image itemrow_image = (itemrow_image) resultValue;
            return itemrow_image.getItemTitulo();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (itemrow_image producto : tempproducto_row) {
                    if (producto.getItemTitulo().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(producto);
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
