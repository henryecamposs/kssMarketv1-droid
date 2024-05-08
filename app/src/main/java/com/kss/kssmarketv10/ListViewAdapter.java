package com.kss.kssmarketv10;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by KSS on 29/3/2017.
 */

public class ListViewAdapter extends BaseAdapter {
    // Declare Variables
    Context context;
    String[] titulos;
    String[] titulos2;
    Bitmap[] imagenes;
    LayoutInflater inflater;

    public ListViewAdapter(Context context, String[] titulos, String[] titulos2, Bitmap[] imagenes) {
        this.context = context;
        this.titulos = titulos;
        this.titulos2 = titulos2;
        this.imagenes = imagenes;
    }

    @Override
    public int getCount() {
        return titulos.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables
        TextView txtTitle;
        ImageView imgImg;
        TextView txtTitle2;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.listrow_image, parent, false);

        // Locate the TextViews in listview_item.xml
        txtTitle = (TextView) itemView.findViewById(R.id.list_row_title);
        txtTitle2 = (TextView) itemView.findViewById(R.id.list_row_title2);
        imgImg = (ImageView) itemView.findViewById(R.id.list_row_image);

        // Capture position and set to the TextViews
        txtTitle.setText(titulos[position]);
        txtTitle2.setText(titulos2[position]);
        imgImg.setImageBitmap(imagenes[position]);
        return itemView;
    }

}
