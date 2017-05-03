package com.pushpam.materialdesignblooddonor.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pushpam.materialdesignblooddonor.R;

import java.util.ArrayList;


public class ExtendBaseAdapterForFillingDataInTabItemListView extends BaseAdapter {


    int image[]={};
    Context context;
    ArrayList<Holder> arrayList;
    Holder holderc;

    ExtendBaseAdapterForFillingDataInTabItemListView(Context context, ArrayList<Holder> arrayList){
        System.out.println("yha aaya");
        System.out.println("yha aaya "+arrayList.get(0).name);
        this.arrayList=arrayList;
        this.context=context;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        MyViewHolder holder=null;
        if(row==null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             row = layoutInflater.inflate(R.layout.tabs_custom_list_view, parent, false);
                holder=new MyViewHolder(row);
                row.setTag(holder);

        }
        else {

            holder= (MyViewHolder) row.getTag();

        }

            holderc = arrayList.get(position);
            holder.txtName.setText(holderc.name);
            holder.txtBloodGroup.setText(holderc.bloodgroup);
            holder.txtCity.setText(holderc.city);
            holder.txtPhone.setText(holderc.phone);

       // Glide.with(context).load(R.drawable.lovesexthree).into(holder.imageView);
        return row;
    }
}

class MyViewHolder{
    ImageView imageView;
    TextView txtName;
    TextView txtPhone;
    TextView txtBloodGroup;
    TextView txtCity;
    MyViewHolder(View row)
    {

        imageView= (ImageView) row.findViewById(R.id.circularimageviewid);
        txtName= (TextView) row.findViewById(R.id.textView4);
        txtPhone= (TextView) row.findViewById(R.id.phoneid);
        txtBloodGroup= (TextView) row.findViewById(R.id.bloodgroupId);
        txtCity= (TextView) row.findViewById(R.id.cityid);
    }
}