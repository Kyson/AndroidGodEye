package cn.hikyson.godeye.internal.modules.sm.core;

import cn.hikyson.godeye.internal.modules.memory.HeapInfo;
import cn.hikyson.godeye.internal.modules.memory.PssInfo;
import cn.hikyson.godeye.internal.modules.memory.RamInfo;

/**
 * Created by kysonchao on 2017/11/22.
 */
public class MemoryInfo {
    public HeapInfo heapInfo;
    public PssInfo pssInfo;
    public RamInfo ramInfo;

    public MemoryInfo(HeapInfo heapInfo, PssInfo pssInfo, RamInfo ramInfo) {
        this.heapInfo = heapInfo;
        this.pssInfo = pssInfo;
        this.ramInfo = ramInfo;
    }

    @Override
    public String toString() {
        return "MemoryInfo{" +
                "heapInfo=" + heapInfo +
                ", pssInfo=" + pssInfo +
                ", ramInfo=" + ramInfo +
                '}';
    }
}
