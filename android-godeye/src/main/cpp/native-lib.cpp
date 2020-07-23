#include <jni.h>
#include <string>
#include "xh_log.h"


extern "C"
JNIEXPORT jstring JNICALL
Java_cn_hikyson_godeye_core_internal_modules_thread_deadlock_DeadLockDetecter_stringFromJNI(
        JNIEnv *env, jclass thiz) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    XH_LOG_INFO("JNI_OnLoad");
    int result = vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6);
    if (result != JNI_OK) {
        XH_LOG_INFO("Failed: %i", result);
        return JNI_VERSION_1_6;
    }
    XH_LOG_INFO("Success: %i", result);
    return JNI_VERSION_1_6;
}