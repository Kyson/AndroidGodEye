package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Activity;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.util.Map;

public class PageInfo<T> implements Serializable {
    public PageType pageType;
    public String pageClassName;
    public int pageHashCode;
    public Map<String, String> extraInfo;

    public PageInfo(T page, Map<String, String> extraInfo) {
        if (page instanceof Activity) {
            this.pageType = PageType.ACTIVITY;
        } else if (page instanceof Fragment || page instanceof android.app.Fragment) {
            this.pageType = PageType.FRAGMENT;
        } else {
            this.pageType = PageType.UNKNOWN;
        }
        this.pageClassName = page.getClass().getName();
        this.pageHashCode = page.hashCode();
        this.extraInfo = extraInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PageInfo<?> pageInfo = (PageInfo<?>) o;

        if (pageHashCode != pageInfo.pageHashCode) return false;
        if (pageType != pageInfo.pageType) return false;
        return pageClassName != null ? pageClassName.equals(pageInfo.pageClassName) : pageInfo.pageClassName == null;
    }

    @Override
    public int hashCode() {
        int result = pageType != null ? pageType.hashCode() : 0;
        result = 31 * result + (pageClassName != null ? pageClassName.hashCode() : 0);
        result = 31 * result + pageHashCode;
        return result;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "pageType=" + pageType +
                ", pageClassName='" + pageClassName + '\'' +
                ", pageHashCode=" + pageHashCode +
                ", extraInfo=" + extraInfo +
                '}';
    }
}
