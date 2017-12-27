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
    private boolean mIsFollow;

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
        mMainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void log(final String msg) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mLogTv.append(msg + "\n");
                if (mIsFollow) {
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

    public void clear() {
        mLogTv.setText("");
    }

    public void follow(boolean follow) {
        mIsFollow = follow;
        if (mIsFollow) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    fullScroll(ScrollView.FOCUS_DOWN);
                }
            }, 250);
        }
    }
}
