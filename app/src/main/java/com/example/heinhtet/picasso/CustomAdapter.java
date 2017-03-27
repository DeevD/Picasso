package com.example.heinhtet.picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heinhtet on 2/9/17.
 */

public class CustomAdapter extends ArrayAdapter<Products> implements Filterable {
    List<Products> arrayList;

    Context mContext ;
    List<Products> org;


    public CustomAdapter(Context context, int rescourse,List<Products> objects) {
        super(context, 0, objects);
        mContext = context;
        arrayList = objects;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<Products> results = new ArrayList<Products>();
                if (org == null)
                    org = arrayList;
                if (constraint != null) {
                    if (org != null && org.size() > 0) {
                        for (final Products products : org) {
                            if (products.getmName().toLowerCase()
                                    .contains(constraint))
                                results.add(products);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                arrayList = (ArrayList<Products>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Products getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_items,parent,false);

        }
        Products products = arrayList.get(position);
        TextView name = (TextView)convertView.findViewById(R.id.name);
        TextView price = (TextView)convertView.findViewById(R.id.price);
        ImageView image = (ImageView)convertView.findViewById(R.id.image);


        name.setText(products.getmName());
        price.setText(products.getmPrice());
        Picasso.with(mContext).load(products.getmImage()).into(image);


        return convertView;
    }
}
