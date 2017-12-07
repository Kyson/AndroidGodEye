/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.hikyson.godeye.core.internal.modules.leakdetector.canary.android;

import android.content.Context;
import android.os.Debug;

import com.squareup.leakcanary.HeapDumper;

import java.io.File;

import cn.hikyson.godeye.core.internal.modules.leakdetector.canary.android.output.OutputLeakService;

public final class AndroidHeapDumper implements HeapDumper {

    final Context context;
    private final LeakDirectoryProvider leakDirectoryProvider;
//  private final Handler mainHandler;

    public AndroidHeapDumper(Context context, LeakDirectoryProvider leakDirectoryProvider) {
        this.leakDirectoryProvider = leakDirectoryProvider;
        this.context = context.getApplicationContext();
//    mainHandler = new Handler(Looper.getMainLooper());
    }


    @SuppressWarnings("ReferenceEquality") // Explicitly checking for named null.
    @Override
    public File dumpHeap(String referenceKey, String referenceName) {
        OutputLeakService.sendOutputBroadcastStart(context, referenceKey);
        File heapDumpFile = leakDirectoryProvider.newHeapDumpFile();

        if (heapDumpFile == RETRY_LATER) {
            CanaryLog.d("创建新的dump文件失败，RETRY_LATER");
            OutputLeakService.sendOutputBroadcastRetry(context, referenceKey);
            return RETRY_LATER;
        }
        CanaryLog.d("创建了新的dump文件：" + heapDumpFile.getAbsolutePath());

//    FutureResult<Toast> waitingForToast = new FutureResult<>();
//    showToast(waitingForToast);

//    if (!waitingForToast.wait(5, SECONDS)) {
//      CanaryLog.d("Did not dump heap, too much time waiting for Toast.");
//      return RETRY_LATER;
//    }

//    Toast toast = waitingForToast.get();
        try {
            CanaryLog.d("开始写入dump信息");
            Debug.dumpHprofData(heapDumpFile.getAbsolutePath());
            CanaryLog.d("写入dump信息完成");
//      cancelToast(toast);
            return heapDumpFile;
        } catch (Exception e) {
            CanaryLog.d(e, "Could not dump heap");
            // Abort heap dump
            OutputLeakService.sendOutputBroadcastRetry(context, referenceKey);
            return RETRY_LATER;
        }
    }

//  private void showToast(final FutureResult<Toast> waitingForToast) {
//    mainHandler.post(new Runnable() {
//      @Override public void run() {
//        final Toast toast = new Toast(context);
//        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//        toast.setDuration(Toast.LENGTH_LONG);
//        LayoutInflater inflater = LayoutInflater.from(context);
//        toast.setView(inflater.inflate(R.layout.leak_canary_heap_dump_toast, null));
//        toast.show();
//        // Waiting for Idle to make sure Toast gets rendered.
//        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
//          @Override public boolean queueIdle() {
//            waitingForToast.set(toast);
//            return false;
//          }
//        });
//      }
//    });
//  }

//  private void cancelToast(final Toast toast) {
//    mainHandler.post(new Runnable() {
//      @Override public void run() {
//        toast.cancel();
//      }
//    });
//  }
}
