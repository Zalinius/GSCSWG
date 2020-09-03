#version 430
layout(location = 0) in vec3 vertex;

uniform mat4 mm;
uniform mat4 vm;
uniform mat4 pm;
  
void main()
{
	gl_Position = pm * vm * mm * vec4(vertex, 1.0f);
}