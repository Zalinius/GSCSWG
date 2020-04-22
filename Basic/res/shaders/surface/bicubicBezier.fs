#version 430
out vec4 color;
in vec3 out_normal;
void main()
{
	color = vec4(abs(out_normal), 1.0f); //normal
}