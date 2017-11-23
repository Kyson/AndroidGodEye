package cn.hikyson.godeye.internal.modules.memory;

/**
 * Created by kysonchao on 2017/11/22.
 */

public class PssInfo {
    public int totalPssKb;
    public int dalvikPssKb;
    public int nativePssKb;
    public int otherPssKb;

    @Override
    public String toString() {
        return "PssInfo{" +
                "totalPss=" + totalPssKb +
                ", dalvikPss=" + dalvikPssKb +
                ", nativePss=" + nativePssKb +
                ", otherPss=" + otherPssKb +
                '}';
    }
}
