#version 430

layout(triangles) in; //Input type from TES
layout(line_strip, max_vertices = 4) out; //output type (and number of vertices) to be rendered

in int gl_PrimitiveIDIn[];
in int gl_InvocationID[]; 

//Built-in inputs from TES
in gl_PerVertex {
  vec4 gl_Position;
  float gl_PointSize;
  float gl_ClipDistance[];
} input_from_TES[];


out gl_PerVertex
{
  vec4 gl_Position;
  float gl_PointSize;
  float gl_ClipDistance[];
}; //output from GS

void main(){ // 3 lines from four points, for a triangle mesh
	gl_Position = input_from_TES[0].gl_Position;
	EmitVertex();
	gl_Position = input_from_TES[1].gl_Position;
	EmitVertex();
	gl_Position = input_from_TES[2].gl_Position;
	EmitVertex();
	gl_Position = input_from_TES[0].gl_Position;
	EmitVertex();
	EndPrimitive();
}