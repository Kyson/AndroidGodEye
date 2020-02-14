package cn.hikyson.godeye.core.helper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TestViewCanaryFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        return linearLayout;
    }

    public void layoutChange() {
        View view0 = new View(getContext());
        ((ViewGroup) getView()).addView(view0);
        View view1 = new View(getContext());
        ((ViewGroup) getView()).addView(view1);

        view0.measure(50, 50);
        view0.layout(0, 0, 50, 50);
        view1.measure(500, 500);
        view1.layout(0, 0, 500, 500);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
