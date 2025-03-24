#include <jni.h>
#include <cstdint>
#include <android/log.h>

// Define the log tag
#define TAG "CameraAndroid"


extern "C"
JNIEXPORT jlong JNICALL
Java_io_flutter_plugins_camera_CameraPlugin_getNativeAddress(JNIEnv *env, jclass clazz,
                                                                        jobject byteBuffer) {
    // Get the address of the native buffer associated with the ByteBuffer
    void* address = env->GetDirectBufferAddress(byteBuffer);
    if (address == nullptr) {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Failed to get native address");
        return 0;
    }
    return (jlong)address;
}