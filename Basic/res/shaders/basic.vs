#version 430
in vec3 vertex;
 
out gl_PerVertex { vec4 gl_Position; };

uniform mat4 mm;
uniform mat4 vm;
uniform mat4 pm;
  
void main()
{
	gl_Position = pm * vm * mm * vec4(vertex, 1.0f);
}