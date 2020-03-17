#version 430

in vec3 interpolate_color;
out vec4 color;

void main()
{
	color = vec4(interpolate_color,1.0f);
}