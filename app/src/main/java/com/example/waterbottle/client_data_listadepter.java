package com.example.waterbottle;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import static android.support.constraint.Constraints.TAG;

public class client_data_listadepter extends ArrayAdapter<cliet_data> {
    List<cliet_data> delivryList;
    Context context;
    int resource;

    public client_data_listadepter(Context context, int resource, List<cliet_data> delivryList) {
        super(context, resource, delivryList);
        this.context = context;
        this.resource = resource;
        this.delivryList = delivryList;
    }
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource, null, false);

        TextView tvdate = view.findViewById(R.id.tvdate);
        TextView tvcollection = view.findViewById(R.id.tvcollection);
        TextView pennding = view.findViewById(R.id.tvpendding);

        cliet_data delorder = delivryList.get(position);
try {
    tvdate.setText(delorder.getCollected_Date());
    tvcollection.setText(delorder.getAmount_collected());
    pennding.setText(delorder.getPadding_amount());
}
catch (Exception e)
{
    Log.e(TAG, "getView:"+e );
}
        return view;
    }

}

