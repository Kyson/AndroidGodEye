package cn.hikyson.godeye.core.internal.modules.viewcanary;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Rect;

public class ViewIssueInfo {
    public String activityName;
    public long timestamp;
    public int maxDepth;
    public int screenWidth;
    public int screenHeight;
    public List<ViewInfo> views = new ArrayList<>();
    public List<OverDrawArea> overDrawAreas = new ArrayList<>();

    public static class ViewInfo {
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

    public static class OverDrawArea {
        public Rect rect;
        public int overDrawTimes;
    }
}
