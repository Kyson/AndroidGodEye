package cn.hikyson.godeye.core.internal.modules.viewcanary.levenshtein;

public interface ViewWithSizeInsDelInterface {
    double deletionCost(ViewIdWithSize c);

    double insertionCost(ViewIdWithSize c);
}
