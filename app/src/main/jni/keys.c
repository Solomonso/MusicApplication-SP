#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_example_musicapplication_1sp_activities_SpotifyLoginActivity_getKey(JNIEnv *env,jobject instance) {
    return (*env)->NewStringUTF(env,"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJib2R5Ijoic3R1ZmYiLCJpYXQiOjE2NDQ1ODI3MTd9.KL4UQfRoJNzC3n-ui0EP8398oKhlDV-SiJPCgH_7FUM");
}

JNIEXPORT jstring JNICALL
Java_com_example_musicapplication_1sp_repositories_SongListService_00024Companion_getURL(JNIEnv *env, jobject instance) {
    return (*env)->NewStringUTF(env, "https://musicapi.duckdns.org/api/");
}

JNIEXPORT jstring JNICALL
Java_com_example_musicapplication_1sp_repositories_ServiceInterceptor_getTokenKey(JNIEnv *env,jobject instance) {
    return (*env)->NewStringUTF(env,"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJib2R5Ijoic3R1ZmYiLCJpYXQiOjE2NDQ1NzY0OTZ9.lxG2IoubFbjv7pPNpq0-8U5gHNSlmeUfIvSE_1uBjIc");
}

JNIEXPORT jstring JNICALL
Java_com_example_musicapplication_1sp_activities_SonglistActivity_getKey(JNIEnv *env, jobject instance) {
    return (*env)->NewStringUTF(env,"jwt eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJib2R5Ijoic3R1ZmYiLCJpYXQiOjE2NDQ1NzY0OTZ9.lxG2IoubFbjv7pPNpq0-8U5gHNSlmeUfIvSE_1uBjIc");
}

JNIEXPORT jstring JNICALL
Java_com_example_musicapplication_1sp_activities_YoutubeActivity_getTokenKey(JNIEnv *env, jobject instance) {
    return (*env)->NewStringUTF(env,"AIzaSyDGDhdiqacmjroaO7-Bar_fgP6G2YVEHsA");
}
JNIEXPORT jstring JNICALL
Java_com_example_musicapplication_1sp_activities_YoutubePlaylistItemActivity_getTokenKey(JNIEnv *env, jobject instance) {
    return (*env)->NewStringUTF(env,"AIzaSyDGDhdiqacmjroaO7-Bar_fgP6G2YVEHsA");
}

JNIEXPORT jstring JNICALL

Java_com_example_musicapplication_1sp_activities_PlaylistActivity_getTokenKey(JNIEnv *env,jobject thiz) {
    return (*env)->NewStringUTF(env,"AIzaSyDGDhdiqacmjroaO7-Bar_fgP6G2YVEHsA");
}

JNIEXPORT jstring JNICALL
Java_com_example_musicapplication_1sp_repositories_StoredUserSongsService_getKey(JNIEnv *env,
                                                                                 jobject thiz) {
    return (*env)->NewStringUTF(env,"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJib2R5Ijoic3R1ZmYiLCJpYXQiOjE2NDQ1NzY0OTZ9.lxG2IoubFbjv7pPNpq0-8U5gHNSlmeUfIvSE_1uBjIc");
}