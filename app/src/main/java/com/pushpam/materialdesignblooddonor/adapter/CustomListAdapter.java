package com.pushpam.materialdesignblooddonor.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pushpam.materialdesignblooddonor.R;
import com.pushpam.materialdesignblooddonor.model.UserData;

import java.util.List;


public class CustomListAdapter extends BaseAdapter {
    Activity activity;
    LayoutInflater inflater;
    List<UserData> list;

    public CustomListAdapter(Activity activity, List<UserData> list) {
        this.activity = activity;
        this.list = list;
    }

    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int location) {
        return list.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView bloodGroup = (TextView) convertView.findViewById(R.id.bloodgroup);
        TextView city = (TextView) convertView.findViewById(R.id.city);
        TextView age=(TextView)convertView.findViewById(R.id.age);
        TextView emailUser=(TextView)convertView.findViewById(R.id.emailUser);
        TextView phone = (TextView) convertView.findViewById(R.id.phone);

        UserData m = list.get(position);
        name.setText(m.getName());
        bloodGroup.setText(m.getBloodGroup());
        city.setText(m.getCity());
        phone.setText(m.getPhone());
        age.setText(m.getAge());
        emailUser.setText(m.getEmailUser());

        return convertView;
    }
}
