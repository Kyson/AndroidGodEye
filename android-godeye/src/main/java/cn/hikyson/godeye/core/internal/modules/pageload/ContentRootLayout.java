package cn.hikyson.godeye.core.internal.modules.pageload;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.ContentFrameLayout;
import android.util.AttributeSet;

/**
 * 每个页面的根布局
 * Created by kysonchao on 2017/5/25.
 */
public class ContentRootLayout extends ContentFrameLayout {
//    private PageLoadSupport mPageableContext;

    public ContentRootLayout(@NonNull Context context) {
        super(context);
//        if (getContext() instanceof PageLoadSupport) {
//            mPageableContext = (PageLoadSupport) getContext();
//        }
    }

    public ContentRootLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        if (getContext() instanceof PageLoadSupport) {
//            mPageableContext = (PageLoadSupport) getContext();
//        }
    }

    /**
     * 第一次绘制
     */
    private boolean mFirstDraw = true;
    /**
     * 绘制的时候需要打标记
     */
    private boolean mNeedDrawMark = false;

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mFirstDraw) {
//            PageLoadSafe.markDrawStart(mPageableContext);
            super.dispatchDraw(canvas);
//            PageLoadSafe.markDrawEnd(mPageableContext);
            mFirstDraw = false;
            return;
        }
        if (mNeedDrawMark) {
//            PageLoadSafe.markRedrawStart(mPageableContext);
            super.dispatchDraw(canvas);
//            PageLoadSafe.markRedrawEnd(mPageableContext);
            mNeedDrawMark = false;
            return;
        }
        super.dispatchDraw(canvas);
    }

    /**
     * 需要标记
     */
//    @Override
//    public void needDrawMark() {
//        mNeedDrawMark = true;
//    }
}
