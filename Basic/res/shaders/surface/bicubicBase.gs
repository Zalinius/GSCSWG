#version 460 core

layout(triangles) in; //Input type from TES
layout(triangle_strip, max_vertices = 3) out; //output type (and number of vertices) to be rendered

in vec3 normal[];
in vec3 fragmentPosition[];

out vec3 out_normal;
out vec3 out_fragmentPosition;

void main(){ // 3 lines from four points, for a triangle mesh
	gl_Position = gl_in[0].gl_Position;
	out_normal = normal[0];
	out_fragmentPosition = fragmentPosition[0];
	EmitVertex();
	
	gl_Position = gl_in[1].gl_Position;
	out_normal = normal[1];
	out_fragmentPosition = fragmentPosition[1];
	EmitVertex();

	gl_Position = gl_in[2].gl_Position;
	out_normal = normal[2];
	out_fragmentPosition = fragmentPosition[2];
	EmitVertex();
	
	EndPrimitive();
}