
#ifndef APPEXAMPLE_RENDER_H
#define APPEXAMPLE_RENDER_H

#include <libavutil/frame.h>
#include "include/libavutil/frame.h"

void gl_initialize();
void gl_uninitialize();
void gl_render_frame();
void gl_set_screen_resolution(int width, int height);
void gl_set_frame(AVFrame *pFrame);
#endif //APPEXAMPLE_RENDER_H
