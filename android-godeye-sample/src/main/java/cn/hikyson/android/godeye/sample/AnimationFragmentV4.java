package cn.hikyson.android.godeye.sample;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AnimationFragmentV4 extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animation_v4, container, false);
        View animView = view.findViewById(R.id.fragment_animation_v4_view);
        view.findViewById(R.id.translationX).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator translate = ObjectAnimator.ofFloat(animView, "translationX", 0f, 300f);
                translate.setDuration(5000);
                translate.start();
            }
        });
        view.findViewById(R.id.scaleX).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator translate = ObjectAnimator.ofFloat(animView, "scaleX", 1.0f, 5.0f, 1.0f);
                translate.setDuration(5000);
                translate.start();
            }
        });
        view.findViewById(R.id.changeSize).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WindowManager mWindowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics metrics = new DisplayMetrics();
                mWindowManager.getDefaultDisplay().getMetrics(metrics);
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;
                if (animView.getWidth() < (width / 3)) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                    animView.setLayoutParams(params);
                } else {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width / 10, height / 10);
                    animView.setLayoutParams(params);
                }
            }
        });
        view.findViewById(R.id.show_hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animView.getVisibility() == View.VISIBLE) {
                    animView.setVisibility(View.GONE);
                } else {
                    animView.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
