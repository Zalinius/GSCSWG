#version 430
layout(location = 0) in vec3 vertex;

out gl_PerVertex
{
  vec4 gl_Position;
  float gl_PointSize;
  float gl_ClipDistance[];
} vertex_out;

uniform mat4 mm;
uniform mat4 vm;
uniform mat4 pm;

void main()
{
	vertex_out.gl_Position = pm*vm*mm*vec4(vertex, 1.0f);
}