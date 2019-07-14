package cn.hikyson.godeye.core.internal.modules.pageload;

import java.util.Map;

public class PageInfo<T> {
    public String pageClassName;
    public int pageHashCode;
    public Map<Object, Object> extraInfo;

    public PageInfo(T page, Map<Object, Object> extraInfo) {
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
        return pageClassName.equals(pageInfo.pageClassName);
    }

    @Override
    public int hashCode() {
        int result = pageClassName.hashCode();
        result = 31 * result + pageHashCode;
        return result;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "pageClassName='" + pageClassName + '\'' +
                ", pageHashCode=" + pageHashCode +
                ", extraInfo=" + extraInfo +
                '}';
    }
}
