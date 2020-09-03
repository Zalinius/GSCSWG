#version 430

layout(location = 0) in vec3 vertex;
layout(location = 1) in vec3 color_in;
 
out vec3 interpolate_color;

uniform mat4 mm;
uniform mat4 vm;
uniform mat4 pm;
  
void main()
{
	gl_Position = pm * vm * mm * vec4(vertex, 1.0f);
	interpolate_color = color_in;
}