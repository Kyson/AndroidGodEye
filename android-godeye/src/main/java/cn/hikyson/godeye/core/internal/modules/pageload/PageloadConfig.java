package cn.hikyson.godeye.core.internal.modules.pageload;


import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.io.Serializable;

@Keep
public class PageloadConfig implements Serializable {
    public String pageInfoProvider;

    public PageloadConfig() {
        this.pageInfoProvider = DefaultPageInfoProvider.class.getName();
    }

    public PageloadConfig(String pageInfoProvider) {
        this.pageInfoProvider = pageInfoProvider;
    }

    @NonNull
    public String pageInfoProvider() {
        return pageInfoProvider;
    }

    @Override
    public String toString() {
        return "PageloadConfig{" +
                "pageInfoProvider=" + pageInfoProvider +
                '}';
    }
}