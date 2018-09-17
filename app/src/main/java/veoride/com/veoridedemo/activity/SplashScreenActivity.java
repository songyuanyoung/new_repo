package veoride.com.veoridedemo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import veoride.com.veoridedemo.helper.Const;
import veoride.com.veoridedemo.fragment.OnboradingFragment;
import veoride.com.veoridedemo.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivity extends AppCompatActivity {
    private ImageView imageView;
    private LinearLayout layout;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);


        button = (Button) findViewById(R.id.btn) ;
        layout = (LinearLayout) findViewById(R.id.layout);
        imageView = (ImageView) findViewById(R.id.logo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(layout);
                imageView.setVisibility(imageView.isShown() ? View.GONE : View.VISIBLE);
            }
        });
        Animation startRotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.android_rotate_animation);
        imageView.startAnimation(startRotateAnimation);
        startRotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                SharedPreferences prefs = getSharedPreferences(Const.SHARED_PREFERENCE, MODE_PRIVATE);
                boolean isFirstLoad = prefs.getBoolean(Const.FIRST_LOAD_INDICATOR, true);
                if (isFirstLoad) {
                    getSupportFragmentManager().beginTransaction().add(R.id.content, new OnboradingFragment()).commit();
                } else {
                    startActivity(new Intent(SplashScreenActivity.this, MapActivity.class));
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }



}
