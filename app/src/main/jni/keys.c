#include <jni.h>

JNIEXPORT jstring JNICALL
com_example_musicapplication_sp_activities_SpotifyLoginActivity_getNativeKey(JNIEnv *env, jobject instance) {

 return (*env)->  NewStringUTF(env, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJib2R5Ijoic3R1ZmYiLCJpYXQiOjE2NDQ1ODI3MTd9.KL4UQfRoJNzC3n-ui0EP8398oKhlDV-SiJPCgH_7FUM");
}

JNIEXPORT jstring JNICALL
Java_com_example_musicapplication_1sp_activities_SpotifyLoginActivity_getKey(JNIEnv *env,jobject instance) {
 return (*env)->  NewStringUTF(env, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJib2R5Ijoic3R1ZmYiLCJpYXQiOjE2NDQ1ODI3MTd9.KL4UQfRoJNzC3n-ui0EP8398oKhlDV-SiJPCgH_7FUM");

}