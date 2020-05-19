package com.adam.annuairechiali.Activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adam.annuairechiali.R;

public class AdvancedExampleCountryAdapter extends ArrayAdapter<AdvancedExampleCountryPOJO> {
    private List<AdvancedExampleCountryPOJO> items;
    private Context context;

    public AdvancedExampleCountryAdapter(@NonNull Context context, int resource, @NonNull List<AdvancedExampleCountryPOJO> objects) {
        super(context, resource, objects);
        this.items = objects;
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        Log.d("TETE", "getView: 0");
        if (null == convertView) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert vi != null;
            convertView = vi.inflate(R.layout.advanced_example_country_list_item, parent, false);
            holder = new ViewHolder();
            holder.text = convertView.findViewById(R.id.custom_cell_text);
            holder.icon = convertView.findViewById(R.id.custom_cell_image);
            Log.d("TETE", "getView: 1");
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            Log.d("TETE", "getView: 2");
        }

        if (null != holder) {
            holder.text.setText(items.get(position).getTitle());
            holder.icon.setImageResource(items.get(position).getIcon());
            Log.d("TETE", "getView: 3");
        }
        return convertView;
    }

    private class ViewHolder {
        TextView text;
        ImageView icon;
    }

}
