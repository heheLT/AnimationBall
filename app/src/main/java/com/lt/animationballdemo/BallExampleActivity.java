package com.lt.animationballdemo;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class BallExampleActivity extends AppCompatActivity {

    private View animalView;
    private View endView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_ball_example);
        animalView = findViewById(R.id.animalView);
        endView = findViewById(R.id.endView);
    }


    public void doAnimal(View view) {
        paoWuXian(new int[]{0, 0}, new int[]{(int) endView.getX(), (int) endView.getY()}, animalView, endView);
    }


    /**
     * 二阶赛贝尔曲线,由三个点确定一条曲线
     */
    public void paoWuXian(final int[] startLocation, final int[] endLocation, final View animalView, final View endView) {
        final float[] middle = new float[2];

        middle[0] = endLocation[0];//中间点的x,这边也可以取其他值
        middle[1] = startLocation[1];//中间点的y,这边也可以取其他值

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(1000);
        valueAnimator.setObjectValues(new PointF(endLocation[0], endLocation[1]));
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {
            @Override
            public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                PointF point = new PointF();
                //这边利用的是二阶贝塞尔曲线公式计算x,y
                point.x = (1 - fraction) * (1 - fraction) * startLocation[0] + 2 * fraction * (1 - fraction) * middle[0] + fraction * fraction * endLocation[0];
                point.y = (1 - fraction) * (1 - fraction) * startLocation[1] + 2 * fraction * (1 - fraction) * middle[1] + fraction * fraction * endLocation[1];
                return point;
            }
        });

        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                animalView.setX(point.x);
                animalView.setY(point.y);
            }
        });

        valueAnimator.addListener(new ValueAnimator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //抛物线动画结束后,进行缩放动画
                ValueAnimator valueAnimator = new ValueAnimator();
                valueAnimator.setDuration(400);
                valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                valueAnimator.setObjectValues(1.0f, 0.8f);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) animation.getAnimatedValue();
                        if (value > 0.9) {
                            endView.setScaleX(value);
                            endView.setScaleY(value);
                        } else {
                            endView.setScaleX(1.8f - value);
                            endView.setScaleY(1.8f - value);
                        }
                    }
                });
                valueAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
