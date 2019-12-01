package cn.hikyson.godeye.core.internal.modules.viewcanary;

import android.graphics.Rect;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Keep
public class ViewIssueInfo implements Serializable {
    public String activityName;
    public long timestamp;
    public int maxDepth;
    public int screenWidth;
    public int screenHeight;
    // all view infos
    public List<ViewInfo> views = new ArrayList<>();
    // overdraw areas
    public List<OverDrawArea> overDrawAreas = new ArrayList<>();

    @Keep
    public static class ViewInfo implements Serializable {
        public String className;
        public String id;
        public Rect rect;
        public int depth;
        public String text;
        public int textOverDrawTimes;
        public float textSize;
        public boolean hasBackground;
        public boolean isViewGroup;
    }

    @Keep
    public static class OverDrawArea implements Serializable {
        public Rect rect;
        public int overDrawTimes;
    }
}
