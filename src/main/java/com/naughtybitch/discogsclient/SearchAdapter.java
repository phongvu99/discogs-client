package com.naughtybitch.discogsclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchAdapter extends BaseAdapter {

    protected Context mContext;
    protected LayoutInflater inflater;
    private List<SearchQuery> searchQueries = null;
    private ArrayList<SearchQuery> arrayList;

    protected SearchAdapter(Context context, List<SearchQuery> searchQueries) {
        mContext = context; //
        this.searchQueries = searchQueries;
        inflater = LayoutInflater.from(context); //
        this.arrayList = new ArrayList<SearchQuery>();
        this.arrayList.addAll(searchQueries);

    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return searchQueries.size();
    }

    @Override
    public Object getItem(int position) {
        return searchQueries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_items, parent);
            // Locate the TextViews in list_items.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Set the results into TextView
        holder.name.setText(searchQueries.get(position).getQuery());
        return view;

    }

    // Filter class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        searchQueries.clear();
        if (charText.length() == 0) {
            searchQueries.addAll(arrayList);
        } else {
            for (SearchQuery sq : arrayList) {
                if (sq.getQuery().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    searchQueries.add(sq);
                }
            }
        }
        notifyDataSetChanged();
    }

}
