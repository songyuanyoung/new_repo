package veoride.com.veoridedemo.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import veoride.com.veoridedemo.helper.Const;
import veoride.com.veoridedemo.R;
import veoride.com.veoridedemo.adapter.SlideAdatper;
import veoride.com.veoridedemo.activity.MapActivity;

import static android.content.Context.MODE_PRIVATE;


public class OnboradingFragment extends Fragment {
    private ViewPager slideViewPager;
    private SlideAdatper slideAdatper;
    private LinearLayout dotLayout;
    private TextView[] dots;
    private int currentPage;
    private Button prevButton;
    private Button nextButton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding, container, false);

        dotLayout = (LinearLayout) view.findViewById(R.id.dotLayout);
        slideViewPager = (ViewPager) view.findViewById(R.id.pager);
        prevButton = (Button) view.findViewById(R.id.backBtn);
        nextButton = (Button) view.findViewById(R.id.nextBtn);


        slideAdatper = new SlideAdatper(getContext());
        slideViewPager.setAdapter(slideAdatper);

        addDotsIndicator(0);
        slideViewPager.addOnPageChangeListener(viewPagerListener);


        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideViewPager.setCurrentItem(currentPage - 1);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideViewPager.setCurrentItem(currentPage + 1);
                if (currentPage == dots.length - 1) {

                    SharedPreferences.Editor editor = getContext().getSharedPreferences(Const.SHARED_PREFERENCE, MODE_PRIVATE).edit();
                    editor.putBoolean(Const.FIRST_LOAD_INDICATOR, false);
                    editor.apply();
                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        return view;
    }

    private void addDotsIndicator(int position) {
        dots = new TextView[3];
        dotLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.GRAY);

            dotLayout.addView(dots[i]);

        }

        if (dots.length > 0) {
            dots[position].setTextColor(Color.BLACK);
        }
    }

    ViewPager.OnPageChangeListener viewPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);

            currentPage = position;

            if (position == 0) {
                nextButton.setEnabled(true);
                prevButton.setEnabled(false);
                prevButton.setVisibility(View.INVISIBLE);

                nextButton.setText("Next");
                prevButton.setTag("");
            } else if (position == dots.length - 1) {
                nextButton.setEnabled(true);
                prevButton.setEnabled(true);
                prevButton.setVisibility(View.VISIBLE);

                nextButton.setText("Finish");
                prevButton.setTag("Back");
            } else {
                nextButton.setEnabled(true);
                prevButton.setEnabled(true);


                nextButton.setText("Next");
                prevButton.setTag("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
