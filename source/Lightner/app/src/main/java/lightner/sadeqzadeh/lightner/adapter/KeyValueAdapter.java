package lightner.sadeqzadeh.lightner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;

public class KeyValueAdapter extends ArrayAdapter<String> {
    MainActivity mainActivity;
    String[] keys;
    String[] values;


    public KeyValueAdapter(MainActivity mainActivity, String[] keys, String[] values) {
        super(mainActivity,android.R.layout.simple_expandable_list_item_1,values);
        this.mainActivity = mainActivity;
        this.keys = keys;
        this.values  = values;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);
        v.setText(values[position]);
        return v;
    }

    @Override
    public String getItem(int position) {
        return keys[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = mainActivity.getLayoutInflater();
            v = inflater.inflate(R.layout.view_spinner_item, null);
        }
        TextView lbl = (TextView) v.findViewById(R.id.text1);
        lbl.setText(values[position]);
        return v;
    }

}
