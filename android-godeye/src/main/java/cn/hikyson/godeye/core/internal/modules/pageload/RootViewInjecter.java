//package cn.hikyson.godeye.core.internal.modules.pageload;
//
//import android.content.Context;
//import android.support.v4.view.LayoutInflaterCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.app.AppCompatDelegate;
//import android.support.v7.widget.ContentFrameLayout;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//
//import cn.hikyson.godeye.core.utils.L;
//
///**
// * Created by kysonchao on 2018/1/25.
// */
//public class RootViewInjecter {
//    public static void inject(final AppCompatActivity activity) {
//        LayoutInflaterCompat.setFactory2(LayoutInflater.from(activity), new LayoutInflater.Factory2() {
//
//            @Override
//            public View onCreateView(String name, Context context, AttributeSet attrs) {
//                return onCreateView(null, name, context, attrs);
//            }
//
//            @Override
//            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
//                AppCompatDelegate delegate = activity.getDelegate();
//                View view;
//                if (!TextUtils.isEmpty(name) && name.equals(ContentFrameLayout.class.getName())) {
//                    L.d(activity.getClass().getSimpleName() + ", name:" + name + " inject success");
//                    view = new ContentRootLayout(context, attrs);
//                } else {
//                    L.d(activity.getClass().getSimpleName() + ", name:" + name + " delegate createView");
//                    view = delegate.createView(parent, name, context, attrs);
//                }
//                return view;
//            }
//        });
//    }
//
////    public static void inject2(final Activity activity) {
////        LayoutInflaterCompat.setFactory2(LayoutInflater.from(activity), new LayoutInflater.Factory2() {
////            @Override
////            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
////                View view;
////                if (!TextUtils.isEmpty(name) && name.equals(ContentFrameLayout.class.getName())) {
////                    L.d(activity.getClass().getSimpleName() + ", name:" + name + " inject success");
////                    view = new ContentRootLayout(context, attrs);
////                } else {
////                    L.d(activity.getClass().getSimpleName() + ", name:" + name + " activity createView");
////                    view = activity.onCreateView(parent, name, context, attrs);
////                }
////                return view;
////            }
////
////            @Override
////            public View onCreateView(String name, Context context, AttributeSet attrs) {
////                return onCreateView(null, name, context, attrs);
////            }
////        });
////    }
//
//}
