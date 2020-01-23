package cn.hikyson.godeye.core.internal.modules.viewcanary.levenshtein;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.Objects;

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
        return id == that.id &&
                Double.compare(that.sizeInScreenPercent, sizeInScreenPercent) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, sizeInScreenPercent);
    }
}
