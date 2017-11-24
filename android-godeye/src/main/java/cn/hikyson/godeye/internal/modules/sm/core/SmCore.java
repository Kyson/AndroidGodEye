package cn.hikyson.godeye.internal.modules.sm.core;

import android.content.Context;
import android.os.Looper;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import cn.hikyson.godeye.internal.modules.cpu.CpuInfo;
import cn.hikyson.godeye.internal.modules.memory.MemoryUtil;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public final class SmCore {

    private Context mContext;
    private LooperMonitor mMonitor;
    private StackSampler stackSampler;
    private CpuSampler cpuSampler;
    private SmConfig mSmConfig;

    private List<BlockInterceptor> mInterceptorChain = new LinkedList<>();


    public SmCore(final Context context, SmConfig smConfig) {
        this.mContext = context;
        this.mSmConfig = smConfig;
        this.stackSampler = new StackSampler(
                Looper.getMainLooper().getThread(), this.mSmConfig.dumpInterval);
        this.cpuSampler = new CpuSampler(this.mSmConfig.dumpInterval);
        this.mMonitor = new LooperMonitor(new LooperMonitor.BlockListener() {

            @Override
            public void onEventStart(long startTime) {
                startDump();
            }

            @Override
            public void onEventEnd(long endTime) {
                stopDump();
            }

            @Override
            public void onBlockEvent(final long blockTimeMillis, final long threadBlockTimeMillis, final boolean longBlock, final long eventStartTimeMilliis, final long eventEndTimeMillis, long longBlockThresholdMillis, long shortBlockThresholdMillis) {
                HandlerThreadFactory.getObtainDumpThreadHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (!longBlock) {//短卡顿
                            if (!mInterceptorChain.isEmpty()) {
                                for (BlockInterceptor interceptor : mInterceptorChain) {
                                    interceptor.onShortBlock(context, blockTimeMillis);
                                }
                            }
                            return;
                        }
                        //如果是长卡顿，那么需要记录很多信息
                        final boolean cpuBusy = cpuSampler.isCpuBusy(eventStartTimeMilliis, eventEndTimeMillis);
                        //这里短卡顿基本是dump不到数据的，因为dump延时一般都会比短卡顿时间久
                        final List<CpuInfo> cpuInfos = cpuSampler.getCpuRateInfo(eventStartTimeMilliis, eventEndTimeMillis);
                        final Map<Long, List<StackTraceElement>> threadStackEntries = stackSampler.getThreadStackEntries(eventStartTimeMilliis, eventEndTimeMillis);
                        Observable.fromCallable(new Callable<MemoryInfo>() {
                            @Override
                            public MemoryInfo call() throws Exception {
                                return new MemoryInfo(MemoryUtil.getAppHeapInfo(), MemoryUtil.getAppPssInfo(mContext), MemoryUtil.getRamInfo(mContext));
                            }
                        }).subscribe(new Consumer<MemoryInfo>() {
                            @Override
                            public void accept(MemoryInfo memoryInfo) throws Exception {
                                LongBlockInfo blockBaseinfo = LongBlockInfo.create(eventStartTimeMilliis, eventEndTimeMillis, threadBlockTimeMillis,
                                        blockTimeMillis, cpuBusy, cpuInfos, threadStackEntries, memoryInfo);
                                if (!mInterceptorChain.isEmpty()) {
                                    for (BlockInterceptor interceptor : mInterceptorChain) {
                                        interceptor.onLongBlock(context, blockBaseinfo);
                                    }
                                }
                            }
                        });
                    }
                });
            }
        }, this.mSmConfig.longBlockThreshold, this.mSmConfig.shortBlockThreshold);
    }

    private void startDump() {
        if (null != stackSampler) {
            stackSampler.start();
        }
        if (null != cpuSampler) {
            cpuSampler.start();
        }
    }

    private void stopDump() {
        if (null != stackSampler) {
            stackSampler.stop();
        }
        if (null != cpuSampler) {
            cpuSampler.stop();
        }
    }

    public void addBlockInterceptor(BlockInterceptor blockInterceptor) {
        mInterceptorChain.add(blockInterceptor);
    }

    public void install() {
        Looper.getMainLooper().setMessageLogging(mMonitor);
    }

    public void uninstall() {
        Looper.getMainLooper().setMessageLogging(null);
        stopDump();
    }

    /**
     * 获取dump信息的延时时间
     * 认为1秒是长卡顿，那么延时0.8s开始dump信息
     * 换句话说，短卡顿是dump不到信息的
     *
     * @return
     */
    public long getSampleDelay() {
        return (long) (this.mSmConfig.longBlockThreshold * 0.8f);
    }

    public Context getContext() {
        return mContext;
    }

    public SmConfig getSmConfig() {
        return mSmConfig;
    }

}
