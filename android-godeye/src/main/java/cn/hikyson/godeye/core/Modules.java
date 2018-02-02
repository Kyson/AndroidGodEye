//package cn.hikyson.godeye.core;
//
//import android.support.annotation.StringDef;
//import android.support.v4.util.ArrayMap;
//
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.util.List;
//
//import cn.hikyson.godeye.core.internal.ProduceableSubject;
//import cn.hikyson.godeye.core.internal.modules.battery.Battery;
//import cn.hikyson.godeye.core.internal.modules.cpu.Cpu;
//import cn.hikyson.godeye.core.internal.modules.crash.Crash;
//import cn.hikyson.godeye.core.internal.modules.fps.Fps;
//import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakDetector;
//import cn.hikyson.godeye.core.internal.modules.memory.Heap;
//import cn.hikyson.godeye.core.internal.modules.memory.Pss;
//import cn.hikyson.godeye.core.internal.modules.memory.Ram;
//import cn.hikyson.godeye.core.internal.modules.network.Network;
//import cn.hikyson.godeye.core.internal.modules.pageload.Pageload;
//import cn.hikyson.godeye.core.internal.modules.sm.Sm;
//import cn.hikyson.godeye.core.internal.modules.startup.Startup;
//import cn.hikyson.godeye.core.internal.modules.thread.ThreadDump;
//import cn.hikyson.godeye.core.internal.modules.thread.deadlock.DeadLock;
//import cn.hikyson.godeye.core.internal.modules.traffic.Traffic;
//
///**
// * Created by kysonchao on 2018/1/30.
// */
//public class Modules {
//    @Retention(RetentionPolicy.SOURCE)
//    @StringDef({ModuleName.CPU, ModuleName.BATTERY, ModuleName.FPS, ModuleName.LEAK,
//            ModuleName.HEAP, ModuleName.PSS, ModuleName.TRAFFIC, ModuleName.CRASH,
//            ModuleName.THREAD, ModuleName.RAM, ModuleName.NETWORK, ModuleName.SM,
//            ModuleName.STARTUP, ModuleName.DEADLOCK, ModuleName.PAGELOAD
//    })
//    public @interface ModuleName {
//        public static final String CPU = "CPU";
//        public static final String BATTERY = "BATTERY";
//        public static final String FPS = "FPS";
//        public static final String LEAK = "LEAK";
//        public static final String HEAP = "HEAP";
//        public static final String PSS = "PSS";
//        public static final String RAM = "RAM";
//        public static final String NETWORK = "NETWORK";
//        public static final String SM = "SM";
//        public static final String STARTUP = "STARTUP";
//        public static final String TRAFFIC = "TRAFFIC";
//        public static final String CRASH = "CRASH";
//        public static final String THREAD = "THREAD";
//        public static final String DEADLOCK = "DEADLOCK";
//        public static final String PAGELOAD = "PAGELOAD";
//    }
//
//    static ArrayMap<String, ProduceableSubject> sModules;
//
//    static {
//        sModules = new ArrayMap<>();
//        sModules.put(ModuleName.CPU, new Cpu());
//        sModules.put(ModuleName.BATTERY, new Battery());
//        sModules.put(ModuleName.FPS, new Fps());
//        sModules.put(ModuleName.LEAK, LeakDetector.instance());
//        sModules.put(ModuleName.HEAP, new Heap());
//        sModules.put(ModuleName.PSS, new Pss());
//        sModules.put(ModuleName.RAM, new Ram());
//        sModules.put(ModuleName.NETWORK, new Network());
//        sModules.put(ModuleName.SM, Sm.instance());
//        sModules.put(ModuleName.STARTUP, new Startup());
//        sModules.put(ModuleName.TRAFFIC, new Traffic());
//        sModules.put(ModuleName.CRASH, new Crash());
//        sModules.put(ModuleName.THREAD, new ThreadDump());
//        sModules.put(ModuleName.DEADLOCK, new DeadLock());
//        sModules.put(ModuleName.PAGELOAD, new Pageload());
//    }
//
//}
