package cn.hikyson.godeye.core.internal.modules.leakdetector.debug;

import android.support.annotation.Keep;

import com.squareup.leakcanary.AnalysisResult;

import java.io.Serializable;

@Keep
public class AnalysisResultWrapper implements Serializable {
    public String className;
    public boolean excludedLeak;
    public long retainedHeapSize;

    public AnalysisResultWrapper(AnalysisResult analysisResult) {
        this.className = analysisResult.className;
        this.excludedLeak = analysisResult.excludedLeak;
        this.retainedHeapSize = analysisResult.retainedHeapSize;
    }

}
