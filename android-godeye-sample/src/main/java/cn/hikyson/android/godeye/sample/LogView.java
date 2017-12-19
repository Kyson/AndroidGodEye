package cn.hikyson.android.godeye.sample;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 用于展示打印日志的View
 * Created by kysonchao on 2017/12/9.
 */
public class LogView extends ScrollView implements Loggable {
    private TextView mLogTv;
    private Handler mMainHandler;
    private CheckBox mFollowTv;

    public LogView(Context context) {
        this(context, null);
    }

    public LogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_log_layout, this);
        mLogTv = this.findViewById(R.id.view_log_layout_log_tv);
        mFollowTv = this.findViewById(R.id.view_log_layout_follow);
        TextView clearTv = this.findViewById(R.id.view_log_layout_clear);
        mMainHandler = new Handler(Looper.getMainLooper());
        clearTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogTv.setText("");
            }
        });
    }

    @Override
    public void log(final String msg) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mLogTv.append(msg + "\n");
                if (mFollowTv.isChecked()) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    }, 250);
                }
            }
        });
    }
}
