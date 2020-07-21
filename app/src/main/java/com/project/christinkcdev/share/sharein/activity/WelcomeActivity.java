package com.project.christinkcdev.share.sharein.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.project.christinkcdev.share.sharein.R;

public class WelcomeActivity extends AppCompatActivity {

    CoordinatorLayout myView;
    ImageView logo;
    LinearLayout details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        myView = findViewById(R.id.mainLayout);
        logo = findViewById(R.id.layout_welcome_page_1_splash_image);
        details = findViewById(R.id.layout_welcome_page_1_details);

        myView.post(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int cx = myView.getWidth() / 2;
                int cy = myView.getHeight() / 2;

                float endRadius = (float) Math.hypot(cx, cy);

                Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0f, endRadius);

                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(Color.WHITE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.yellow));
                        logo.setVisibility(View.VISIBLE);
                        details.setVisibility(View.VISIBLE);
                        logo.setAnimation(AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.enter_from_top));
                        Animation animation1 = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.enter_from_bottom);
                        animation1.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        details.setAnimation(animation1);

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                anim.start();

            }
        });

    }
}