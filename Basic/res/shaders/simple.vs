#version 430
layout(location = 0) in vec3 vertex;

out gl_PerVertex
{
  vec4 gl_Position;
  float gl_PointSize;
  float gl_ClipDistance[];
} vertex_out;

void main()
{
	vertex_out.gl_Position = vec4(vertex, 1.0f);
}