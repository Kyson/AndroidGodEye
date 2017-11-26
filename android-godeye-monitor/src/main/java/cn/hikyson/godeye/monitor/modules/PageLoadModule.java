package cn.hikyson.godeye.monitor.modules;

import android.net.Uri;

import com.ctrip.ibu.performance.debugmonitor.StotageQueue;
import com.ctrip.ibu.performance.internal.pageload.PageLoadItem2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kysonchao on 2017/9/4.
 */
public class PageLoadModule implements Module {

    @Override
    public byte[] process(String path, Uri uri) throws Throwable {
        List<PageLoadItem2> pageLoadItem2s = StotageQueue.popPageLoadItem2s();
        if (pageLoadItem2s == null || pageLoadItem2s.isEmpty()) {
            return new ResultWrapper<List<PageLoadItem2>>("no pageload data found").toBytes();
        }
        List<PageloadOutput> pageloadOutputs = new ArrayList<>();
        for (PageLoadItem2 pageLoadItem2 : pageLoadItem2s) {
            pageloadOutputs.add(new PageloadOutput(pageLoadItem2));
        }
        return new ResultWrapper<>(pageloadOutputs).toBytes();
    }

    private static class PageloadOutput {
        public String pageName;
        public long drawStartTime;
        public long drawEndTime;
        public String requestStartName;
        public long requestStartTime;
        public String requestEndName;
        public long requestEndTime;
        public long redrawStartTime;
        public long redrawEndTime;
        public String pageloadDetail;

        private static final long INVALID_TIME = 0;

        public PageloadOutput(PageLoadItem2 pageLoadItem2) {
            if (pageLoadItem2.pageInfo != null && pageLoadItem2.pageInfo.length == 2) {
                this.pageName = pageLoadItem2.pageInfo[1] + "[" + pageLoadItem2.pageInfo[0] + "]";
            } else {
                this.pageName = "";
            }
            this.drawStartTime = pageLoadItem2.drawStartTime - pageLoadItem2.pageStartTime;
            this.drawEndTime = pageLoadItem2.drawEndTime - pageLoadItem2.pageStartTime;
            PageLoadItem2.PageLoadProcessor.RequestTimeData requestTimeData = PageLoadItem2.PageLoadProcessor.getRequestData(pageLoadItem2);
            if (requestTimeData == null) {
                this.requestStartTime = INVALID_TIME;
                this.requestEndTime = INVALID_TIME;
            } else {
                if (requestTimeData.requestEqueueTimeMills <= 0) {
                    this.requestStartTime = INVALID_TIME;
                    this.requestStartName = "";
                } else {
                    this.requestStartTime = requestTimeData.requestEqueueTimeMills - pageLoadItem2.pageStartTime;
                    this.requestStartName = requestTimeData.requestEqueueName;
                }
                if (requestTimeData.requestEndTimeMills <= 0) {
                    this.requestEndTime = INVALID_TIME;
                    this.requestEndName = "";
                } else {
                    this.requestEndTime = requestTimeData.requestEndTimeMills - pageLoadItem2.pageStartTime;
                    this.requestEndName = requestTimeData.requestEndName;
                }
            }
            final long[] maxRedrawDuration = getMaxRedrawDuration(pageLoadItem2);
            if (maxRedrawDuration == null || this.requestEndTime <= 0 || maxRedrawDuration[0] < this.requestEndTime) {
                this.redrawStartTime = INVALID_TIME;
                this.redrawEndTime = INVALID_TIME;
            } else {
                this.redrawStartTime = maxRedrawDuration[0] - pageLoadItem2.pageStartTime;
                this.redrawEndTime = maxRedrawDuration[1] - pageLoadItem2.pageStartTime;
            }
            this.pageloadDetail = String.valueOf(pageLoadItem2);
        }

        private static long[] getMaxRedrawDuration(PageLoadItem2 pageLoadItem2) {
            List<long[]> redrawDatas = pageLoadItem2.redrawLoadList;
            if (redrawDatas == null || redrawDatas.isEmpty()) {
                return null;
            }
            long[] redrawDuration = new long[2];
            long maxTime = 0L;
            for (long[] item : redrawDatas) {
                if (!PageLoadItem2.PageLoadProcessor.isValidRedrawData(item)) {
                    continue;
                }
                if (item[1] > maxTime) {
                    maxTime = item[1];
                    redrawDuration[0] = item[0];
                    redrawDuration[1] = item[1];
                }
            }
            if (redrawDuration[0] <= 0 || redrawDuration[1] <= 0 || redrawDuration[1] <= redrawDuration[0]) {
                return null;
            }
            return redrawDuration;
        }
    }
}
