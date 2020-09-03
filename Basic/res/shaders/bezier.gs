#version 430

layout(lines) in; //Input type from TES
layout(line_strip, max_vertices = 2) out; //output type (and number of vertices) to be rendered

void main(){ // Only 2 points, because isolines
	gl_Position = gl_in[0].gl_Position;
	EmitVertex();
	gl_Position = gl_in[1].gl_Position;
	EmitVertex();
	EndPrimitive();
}