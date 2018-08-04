//Shader.frag文件内容
static const char* FRAG_SHADER =
    "varying lowp vec2 tc;\n"
	"uniform sampler2D SamplerY;\n"
	"uniform sampler2D SamplerU;\n"
	"uniform sampler2D SamplerV;\n"
	"void main(void)\n"
	"{\n"
		"mediump vec3 yuv;\n"
		"lowp vec3 rgb;\n"
		"yuv.x = texture2D(SamplerY, tc).r;\n"
		"yuv.y = texture2D(SamplerU, tc).r - 0.5;\n"
		"yuv.z = texture2D(SamplerV, tc).r - 0.5;\n"
		"rgb = mat3( 1,   1,   1,\n"
                    "0,       -0.39465,  2.03211,\n"
                    "1.13983,   -0.58060,  0) * yuv;\n"
		"gl_FragColor = vec4(rgb, 1);\n"
	"}\n";
