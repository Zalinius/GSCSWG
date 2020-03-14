#version 430
in vec3 vertex;
 
out gl_PerVertex { vec4 gl_Position; };

  
void main()
{
	gl_Position = vec4(vertex, 1.0f);
}