#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_cn_hikyson_godeye_core_internal_modules_thread_deadlock_DeadLockDetecter_stringFromJNI(
        JNIEnv *env, jclass thiz) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}