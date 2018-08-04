#include "render.h"
#include "Shader.vert"
#include "Shader.frag"
#include "log.h"

#include <jni.h>
#include <android/log.h>

#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

static GLuint g_texYId;
static GLuint g_texUId;
static GLuint g_texVId;
static GLuint simpleProgram;

//static char *              g_buffer = NULL;
static int                 g_width = 0;
static int                 g_height = 0;

static void checkGlError(const char* op)
{
    GLint error;
    for (error = glGetError(); error; error = glGetError())
    {
        LOGE("error::after %s() glError (0x%x)\n", op, error);
    }
}

static GLuint bindTexture(GLuint texture, const char *buffer, GLuint w , GLuint h)
{
//	GLuint texture;
//	glGenTextures ( 1, &texture );
    //checkGlError("glGenTextures");
    glBindTexture ( GL_TEXTURE_2D, texture );
    //checkGlError("glBindTexture");
    glTexImage2D ( GL_TEXTURE_2D, 0, GL_LUMINANCE, w, h, 0, GL_LUMINANCE, GL_UNSIGNED_BYTE, buffer);
    //checkGlError("glTexImage2D");
    glTexParameteri ( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR );
    //checkGlError("glTexParameteri");
    glTexParameteri ( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR );
    //checkGlError("glTexParameteri");
    glTexParameteri ( GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE );
    //checkGlError("glTexParameteri");
    glTexParameteri ( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE );
    //checkGlError("glTexParameteri");
    //glBindTexture(GL_TEXTURE_2D, 0);
/*
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, yTextureId);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_LUMINANCE, _video.Frame422->linesize[0], _video.Frame422->height,0, GL_LUMINANCE, GL_UNSIGNED_BYTE, _video.Frame422->data[0]);

    glActiveTexture(GL_TEXTURE1);
    glBindTexture(GL_TEXTURE_2D, uTextureId);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_LUMINANCE,  _video.Frame422->linesize[1], _video.Frame422->height/2,0, GL_LUMINANCE, GL_UNSIGNED_BYTE, _video.Frame422->data[1]);

    glActiveTexture(GL_TEXTURE2);
    glBindTexture(GL_TEXTURE_2D, vTextureId);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_LUMINANCE,  _video.Frame422->linesize[2], _video.Frame422->height/2,0, GL_LUMINANCE, GL_UNSIGNED_BYTE, _video.Frame422->data[2]);
*/
    return texture;
}

static void renderFrame()
{
#if 1
    // Galaxy Nexus 4.2.2
    static GLfloat squareVertices[] = {
        -1.0f, -1.0f,
        1.0f, -1.0f,
        -1.0f,  1.0f,
        1.0f,  1.0f,
    };

    static GLfloat coordVertices[] = {
        0.0f, 1.0f,
        1.0f, 1.0f,
        0.0f,  0.0f,
        1.0f,  0.0f,
    };
#else
    // HUAWEIG510-0010 4.1.1
    static GLfloat squareVertices[] = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f,  1.0f,
            1.0f,  1.0f,
    };

    static GLfloat coordVertices[] = {
            -1.0f, 1.0f,
            1.0f, 1.0f,
            -1.0f,  -1.0f,
            1.0f,  -1.0f,
    };
#endif

    glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
    checkGlError("glClearColor");
    glClear(GL_COLOR_BUFFER_BIT);
    checkGlError("glClear");
    //PRINTF("setsampler %d %d %d", g_texYId, g_texUId, g_texVId);
    GLint tex_y = glGetUniformLocation(simpleProgram, "SamplerY");
    GLint tex_u = glGetUniformLocation(simpleProgram, "SamplerU");
    GLint tex_v = glGetUniformLocation(simpleProgram, "SamplerV");

/*
    glBindAttribLocation(simpleProgram, ATTRIB_VERTEX, "vPosition");
    checkGlError("glBindAttribLocation");
    glBindAttribLocation(simpleProgram, ATTRIB_TEXTURE, "a_texCoord");
    checkGlError("glBindAttribLocation");
    */

    int ATTRIB_VERTEX, ATTRIB_TEXTURE;

    ATTRIB_VERTEX=glGetAttribLocation(simpleProgram,"vPosition");
    checkGlError("glBindAttribLocation");
    ATTRIB_TEXTURE=glGetAttribLocation(simpleProgram,"a_texCoord");
    checkGlError("glBindAttribLocation");


    glVertexAttribPointer(ATTRIB_VERTEX, 2, GL_FLOAT, 0, 0, squareVertices);
    checkGlError("glVertexAttribPointer");
    glEnableVertexAttribArray(ATTRIB_VERTEX);
    checkGlError("glEnableVertexAttribArray");

    glVertexAttribPointer(ATTRIB_TEXTURE, 2, GL_FLOAT, 0, 0, coordVertices);
    checkGlError("glVertexAttribPointer");
    glEnableVertexAttribArray(ATTRIB_TEXTURE);
    checkGlError("glEnableVertexAttribArray");

    glActiveTexture(GL_TEXTURE0);
    checkGlError("glActiveTexture");
    glBindTexture(GL_TEXTURE_2D, g_texYId);
    checkGlError("glBindTexture");
    glUniform1i(tex_y, 0);
    checkGlError("glUniform1i");

    glActiveTexture(GL_TEXTURE1);
    checkGlError("glActiveTexture");
    glBindTexture(GL_TEXTURE_2D, g_texUId);
    checkGlError("glBindTexture");
    glUniform1i(tex_u, 1);
    checkGlError("glUniform1i");

    glActiveTexture(GL_TEXTURE2);
    checkGlError("glActiveTexture");
    glBindTexture(GL_TEXTURE_2D, g_texVId);
    checkGlError("glBindTexture");
    glUniform1i(tex_v, 2);
    checkGlError("glUniform1i");

    //glEnable(GL_TEXTURE_2D);
    //checkGlError("glEnable");
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    checkGlError("glDrawArrays");
}

static GLuint buildShader(const char* source, GLenum shaderType)
{
    GLuint shaderHandle = glCreateShader(shaderType);

    if (shaderHandle)
    {
        glShaderSource(shaderHandle, 1, &source, 0);
        glCompileShader(shaderHandle);

        GLint compiled = 0;
        glGetShaderiv(shaderHandle, GL_COMPILE_STATUS, &compiled);
        if (!compiled)
        {
            GLint infoLen = 0;
            glGetShaderiv(shaderHandle, GL_INFO_LOG_LENGTH, &infoLen);
            if (infoLen)
            {
                char* buf = (char*) malloc(infoLen);
                if (buf)
                {
                    glGetShaderInfoLog(shaderHandle, infoLen, NULL, buf);
                    LOGE("error::Could not compile shader %d:\n%s\n", shaderType, buf);
                    free(buf);
                }
                glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }

    }

    return shaderHandle;
}

static GLuint buildProgram(const char* vertexShaderSource, const char* fragmentShaderSource)
{
    GLuint vertexShader = buildShader(vertexShaderSource, GL_VERTEX_SHADER);
    GLuint fragmentShader = buildShader(fragmentShaderSource, GL_FRAGMENT_SHADER);
    GLuint programHandle = glCreateProgram();

    if (programHandle)
    {
        glAttachShader(programHandle, vertexShader);
        checkGlError("glAttachShader");
        glAttachShader(programHandle, fragmentShader);
        checkGlError("glAttachShader");
        glLinkProgram(programHandle);

        GLint linkStatus = GL_FALSE;
        glGetProgramiv(programHandle, GL_LINK_STATUS, &linkStatus);
        if (linkStatus != GL_TRUE) {
            GLint bufLength = 0;
            glGetProgramiv(programHandle, GL_INFO_LOG_LENGTH, &bufLength);
            if (bufLength) {
                char* buf = (char*) malloc(bufLength);
                if (buf) {
                    glGetProgramInfoLog(programHandle, bufLength, NULL, buf);
                    LOGE("error::Could not link program:\n%s\n", buf);
                    free(buf);
                }
            }
            glDeleteProgram(programHandle);
            programHandle = 0;
        }

    }

    return programHandle;
}

static unsigned char * readYUV(const char *path)
{
    FILE *fp;
    unsigned char * buffer;
    long size = 1920 * 1080 * 3 / 2;

    if((fp=fopen(path,"rb"))==NULL)
    {
        LOGE("cant open the file");
        exit(0);
    }

    buffer = (unsigned char *)malloc(size);
    memset(buffer,'\0',size);
    long len = (long) fread(buffer, 1, size, fp);
    LOGE("read data size:%ld", len);
    fclose(fp);
    return buffer;
}

void gl_initialize()
{
    simpleProgram = buildProgram(VERTEX_SHADER, FRAG_SHADER);
    if (simpleProgram == 0)
        LOGE("%s:buildProgram failed ", __func__);
    glUseProgram(simpleProgram);
    glGenTextures(1, &g_texYId);
    glGenTextures(1, &g_texUId);
    glGenTextures(1, &g_texVId);
}

void gl_uninitialize()
{

    g_width = 0;
    g_height = 0;
}
static AVFrame *gFrame = NULL;
void gl_set_frame(AVFrame *pFrame)
{
    gFrame = pFrame;
}
//设置图像数据
void gl_set_screen_resolution(int width, int height)
{
    if (g_width != width || g_height != height)
    {
        g_width = width;
        g_height = height;
    }
}
//画屏
void gl_render_frame()
{
    if (0 == g_width || 0 == g_height)
        return;

#if 0
    int width = 1920;
    int height = 1080;
	static unsigned char *buffer = NULL;

    if (NULL == buffer)
    {
        char filename[128] = {0};
        strcpy(filename, "/mnt/sdcard/video.yuv");
	    buffer = readYUV(filename);
    }
#else
    int width = g_width;
    int height = g_height;
#endif
    glViewport(0, 0, width, height);

#if 0
    bindTexture(g_texYId, (const char *) buffer, (GLuint) width, (GLuint) height);
    bindTexture(g_texUId, (const char *) (buffer + width * height), (GLuint) (width / 2), (GLuint) (height / 2));
    bindTexture(g_texVId, (const char *) (buffer + width * height * 5 / 4), (GLuint) (width / 2), (GLuint) (height / 2));
#else
    bindTexture(g_texYId, (const char *) gFrame->data[0], (GLuint) gFrame->width, (GLuint) gFrame->height);
    bindTexture(g_texUId, (const char *) gFrame->data[1], (GLuint) (gFrame->width / 2), (GLuint) (gFrame->height / 2));
    bindTexture(g_texVId, (const char *) gFrame->data[2], (GLuint) (gFrame->width / 2), (GLuint) (gFrame->height / 2));
#endif
    renderFrame();
}
//如果设置图像数据和画屏是两个线程的话，记住要加锁。