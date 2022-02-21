#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_bink_localhero_utils_Keys_testApiKey(
        JNIEnv *env,
        jobject) {
    std::string key = "TestApiKey";
    return env->NewStringUTF(key.c_str());
}

