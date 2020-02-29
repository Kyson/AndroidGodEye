package cn.hikyson.godeye.sample;

import android.app.Fragment;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.hikyson.android.godeye.sample.R;


public class LeakFragment extends Fragment {
    private TextView mTv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        new Thread(() -> {
            try {
                Thread.sleep(100000);
                mTv.setText("Leak");
            } catch (InterruptedException e) {
            }
        }).start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leak, container, false);
        mTv = view.findViewById(R.id.fragment_leak_test);
        return view;
    }
}
