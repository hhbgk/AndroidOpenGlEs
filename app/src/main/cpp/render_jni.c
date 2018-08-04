#include <jni.h>
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include "render.h"
#include "log.h"

static AVCodecContext  *pCodecCtx;
static AVCodec         *pCodec;
static AVFrame *pFrame;
static AVPacket packet;

JNIEXPORT void JNICALL
Java_com_hhbgk_example_opengles_CodecWrapper_render(JNIEnv *env, jclass jclazz) {
    gl_render_frame();
}

JNIEXPORT void JNICALL
Java_com_hhbgk_example_opengles_CodecWrapper_init(JNIEnv *env, jclass type) {
    LOGE("%s", __func__);
//av_log_set_callback(custom_log);
    av_register_all();
    pCodec = avcodec_find_decoder(AV_CODEC_ID_H264);
    if (pCodec == NULL) {
        LOGE("Couldn't find Codec.\n");
        return;
    }
    pCodecCtx = avcodec_alloc_context3(pCodec);
    if (avcodec_open2(pCodecCtx, pCodec, NULL) < 0) {
        LOGE("Couldn't open codec.\n");
        return;
    }
    pFrame = av_frame_alloc();
    pCodecCtx->flags |= CODEC_FLAG_LOW_DELAY;
    pCodecCtx->debug |= FF_DEBUG_MMCO;
    pCodecCtx->pix_fmt = AV_PIX_FMT_YUV420P;

    pCodecCtx->flags2 |= CODEC_FLAG2_CHUNKS;
    if (pCodec->capabilities & AV_CODEC_CAP_TRUNCATED)
        pCodecCtx->flags |= AV_CODEC_FLAG_TRUNCATED; // we do not send complete frames

    av_init_packet(&packet);
    gl_initialize();
    LOGE("%s: success", __func__);
}
JNIEXPORT void JNICALL
Java_com_hhbgk_example_opengles_CodecWrapper_changed(JNIEnv *env, jclass type, jint width, jint height) {
    LOGE("screen w=%d, h=%d", width, height);
    gl_set_screen_resolution(width, height);
}
static int decode(AVCodecContext *codecCtx, AVFrame **f, AVPacket *pkt) {
    AVFrame* frame = (*f);
    int ret = 0;
    while (1)
    {
        while(1)
        {
            ret = avcodec_receive_frame(codecCtx, frame);
            if (ret == AVERROR(EAGAIN)) {//数据太少不能解出一帧，重新读取
                break;
            } else if (ret == AVERROR(EINVAL)) {//不支持数据，错误，退出
                LOGE("error -1")
                return -1;
            }
            (*f) = frame;
            av_packet_unref(pkt);
            return 0;
        }
        if ((ret = avcodec_send_packet(codecCtx, pkt)) != 0)
        {
            LOGE("avcodec_send_packet error=%d", ret);
            return ret;
        }
    }
}
JNIEXPORT jboolean JNICALL
Java_com_hhbgk_example_opengles_CodecWrapper_decode(JNIEnv *env, jclass jclazz, jbyteArray jin_packet) {

    unsigned char *in_array = (unsigned char *) (*env)->GetByteArrayElements(env, jin_packet, 0);
    if (!in_array)
        return JNI_FALSE;
    jsize length = (jsize) (*env)->GetArrayLength(env, jin_packet);

    packet.size = (int) length;
    packet.data = in_array;

    int result = decode(pCodecCtx, &pFrame, &packet);
    //LOGE("result=%d, w=%d, h=%d", result, pFrame->width, pFrame->height);
    if (result != 0){
        goto fail;
    }
    gl_set_frame(pFrame);
    (*env)->ReleaseByteArrayElements(env, jin_packet, (jbyte *) in_array, 0);
    return JNI_TRUE;
fail:
    if (in_array) (*env)->ReleaseByteArrayElements(env, jin_packet, (jbyte *) in_array, 0);
    return JNI_FALSE;
}
JNIEXPORT void JNICALL
Java_com_hhbgk_example_opengles_CodecWrapper_destroy(JNIEnv *env, jclass type) {
    LOGE("%s", __func__);
    if (pFrame) av_frame_free(&pFrame);
    if (pCodecCtx) {
        avcodec_close(pCodecCtx);
        av_free(pCodecCtx);
    }

    gl_uninitialize();
}

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    return JNI_VERSION_1_6;
}

