package cn.hikyson.godeye.core.internal.modules.viewcanary.levenshtein;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class ViewIdWithSize implements Serializable {
    public int id;
    public double sizeInScreenPercent;

    public ViewIdWithSize(int id, double sizeInScreenPercent) {
        this.id = id;
        this.sizeInScreenPercent = sizeInScreenPercent;
    }

    @Override
    public String toString() {
        return "ViewIdWithSize{" +
                "id=" + id +
                ", sizeInScreenPercent=" + sizeInScreenPercent +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ViewIdWithSize that = (ViewIdWithSize) o;

        if (id != that.id) return false;
        return Double.compare(that.sizeInScreenPercent, sizeInScreenPercent) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        temp = Double.doubleToLongBits(sizeInScreenPercent);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
