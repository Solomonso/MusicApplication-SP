LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := keys
LOCAL_SRC_FILES := keys.c

-D_FORTIFY_SOURCE=2

LOCAL_CFLAGS := -fstack-protector-all

LOCAL_LDFLAGS := -fstack-protector-all

LOCAL_CXXFLAGS := -fstack-protector-all


include $(BUILD_SHARED_LIBRARY)