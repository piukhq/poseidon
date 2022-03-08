#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_bink_localhero_utils_Keys_testApiKey(
        JNIEnv *env,
        jobject) {
    std::string key = "TestApiKey123";
    return env->NewStringUTF(key.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_bink_localhero_utils_Keys_spreedlyKey(
        JNIEnv *env,
        jobject /* this */) {
    std::string key = "1Lf7DiKgkcx5Anw7QxWdDxaKtTa";
    return env->NewStringUTF(key.c_str());
}

