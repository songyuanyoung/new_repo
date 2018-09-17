package veoride.com.veoridedemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import veoride.com.veoridedemo.R;

public class SlideAdatper extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;

    public SlideAdatper(Context context) {
        this.context = context;
    }

    public String[] slide_descs = {
            "Eat Here",
            "Relax Here",
            "Meet People Here"
    };

    public int[] icons = {
            R.drawable.icon_eat,
            R.drawable.icon_relax,
            R.drawable.icon_stury
    };


    @Override
    public int getCount() {
        return slide_descs.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide_layout, container, false);

        ImageView icon = (ImageView) view.findViewById(R.id.circleIcon);
        TextView des = (TextView) view.findViewById(R.id.desc);

        icon.setImageResource(icons[position]);
        des.setText(slide_descs[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
