#version 430

layout(triangles) in; //Input type from TES
layout(line_strip, max_vertices = 4) out; //output type (and number of vertices) to be rendered\

void main(){
	gl_Position = gl_in[0].gl_Position;
	EmitVertex();
	gl_Position = gl_in[1].gl_Position;
	EmitVertex();
	gl_Position = gl_in[2].gl_Position;
	EmitVertex();
	gl_Position = gl_in[0].gl_Position;
	EmitVertex();
	EndPrimitive();
}