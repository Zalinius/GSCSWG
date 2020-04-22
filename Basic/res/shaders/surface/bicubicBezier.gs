#version 430

layout(triangles) in; //Input type from TES
layout(triangle_strip, max_vertices = 3) out; //output type (and number of vertices) to be rendered

in int gl_PrimitiveIDIn[];
in int gl_InvocationID[]; 

//Built-in inputs from TES
in gl_PerVertex {
  vec4 gl_Position;
  float gl_PointSize;
  float gl_ClipDistance[];
} input_from_TES[];
in vec3 normal[];
in vec3 fragmentPosition[];

out gl_PerVertex
{
  vec4 gl_Position;
  float gl_PointSize;
  float gl_ClipDistance[];
}; //output from GS
out vec3 out_normal;
out vec3 out_fragmentPosition;

void main(){ // 3 lines from four points, for a triangle mesh
	gl_Position = input_from_TES[0].gl_Position;
	out_normal = normal[0];
	out_fragmentPosition = fragmentPosition[0];
	EmitVertex();
	
	gl_Position = input_from_TES[1].gl_Position;
	out_normal = normal[1];
	out_fragmentPosition = fragmentPosition[1];
	EmitVertex();

	gl_Position = input_from_TES[2].gl_Position;
	out_normal = normal[2];
	out_fragmentPosition = fragmentPosition[2];
	EmitVertex();
	
	EndPrimitive();
}