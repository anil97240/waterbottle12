package com.example.waterbottle.admin_agent_side;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterbottle.R;
import com.example.waterbottle.admin_agent_side.product_model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SlidingImage_Adapter extends PagerAdapter {

    // private ArrayList<Integer> IMAGES;
    private LayoutInflater inflater;
    private Context context;
    List<sliding_image> totalList;

    public SlidingImage_Adapter(Context context,List<sliding_image> totalList) {
        this.context = context;
        this.totalList=totalList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return totalList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);
        final Button Buttonadd = (Button) imageLayout.findViewById(R.id.btnadd2);
        final Button buttonmin = (Button) imageLayout.findViewById(R.id.btnmin2);

        final EditText edtbottle= (EditText) imageLayout
                .findViewById(R.id.edtadd2);

        edtbottle.setText(""+0);
//        tvtotal.setText(""+0);
        final sliding_image sliding_image= totalList.get(position);


        Buttonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a= Integer.parseInt(edtbottle.getText().toString());
                int b=1;
                a=b+a;

                edtbottle.setText(""+a);

                edtbottle.setText("" + a);
                String ed=edtbottle.getText().toString();
                sliding_image slid =new sliding_image();
                slid.setQry(ed);

                Toast.makeText(context, ""+slid.getQry(), Toast.LENGTH_SHORT).show();
            }
        });


        buttonmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a= Integer.parseInt(edtbottle.getText().toString());
                if(a==0)
                {
                    Toast.makeText(v.getContext(), "No Bottle In Cart", Toast.LENGTH_SHORT).show();
                }
                else {
                    int b = 1;
                    a = a-b;
                    edtbottle.setText("" + a);
                    String ed=edtbottle.getText().toString();
                    sliding_image slid =new sliding_image();
                    slid.setQry(ed);
                    Toast.makeText(context, ""+slid.getQry(), Toast.LENGTH_SHORT).show();

                }
            }
        });

        //imageView.setImageResource(Integer.parseInt(sliding_image.getImage()));

        Picasso.with(context)
                .load(sliding_image.getImage())
                .into(imageView);
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}

