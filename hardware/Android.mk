LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES := realarmhardware.c
LOCAL_SHARED_LIBRARIES := liblog libutils libcutils
LOCAL_C_INCLUDES += $(JNI_H_INCLUDE)
LOCAL_LDLIBS:=-L$(SYSROOT)/usr/lib -llog
LOCAL_PRELINK_MODULE := false
LOCAL_MODULE_PATH := $(TARGET_OUT_SHARED_LIBRARIES)
LOCAL_MODULE := libRealarmHardwareJni

include $(BUILD_SHARED_LIBRARY)

