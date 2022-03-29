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
Java_com_bink_localhero_utils_Keys_binkTestAuthToken(
        JNIEnv *env,
        jobject) {
    std::string key = "e66cd653a8a1a4ee49ef7b4f2f44517e01e4e513c0c0ad4cc0818696847f98be";
    return env->NewStringUTF(key.c_str());
}

