#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_le_stock_stockdemo_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello StockChart";
    return env->NewStringUTF(hello.c_str());
}
