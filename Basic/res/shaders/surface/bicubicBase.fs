#version 430

in vec3 out_normal;
in vec3 out_fragmentPosition; //in model space

out vec4 color; //The pixel color


uniform vec3 camera_position;

//Material parameteres
uniform float ambient_coefficient;
uniform float diffuse_coefficient;
uniform float specular_coefficient;
uniform float shinyness;

//Light parameters
uniform vec3 light_position;
uniform vec3 light_color;

vec4 computeLitColorForSingleSource(vec3 normal, vec3 fragment_position, vec3 view_position);

void main()
{
	color = vec4(abs(out_normal), 1.0f);
	color = computeLitColorForSingleSource(out_normal, out_fragmentPosition, camera_position);
} 

vec4 computeLitColorForSingleSource(vec3 normal, vec3 fragment_position, vec3 view_position){
	vec4 result;
	vec3 light_color = vec3(1,1,1);
	vec3 object_color = vec3(1,1,1);
	vec3 light_position = vec3(0,0,0);
	
	//Ambient 
	float ambient_strength = 0.19225f;
	vec3 ambient = ambient_strength * light_color;

	//Diffuse
	vec3 light_direction = normalize(light_position - fragment_position);
	float diffuse_strength = 0.50754f * max(dot(normalize(normal), light_direction), 0.0f);
	vec3 diffuse = diffuse_strength * light_color;

	//Specular
	vec3 view_direction = normalize(view_position - fragment_position);
	vec3 reflect_light_direction = reflect(-light_direction, normalize(normal));
	float specular_strength =  0.508273f * pow(max(dot(reflect_light_direction, view_direction), 0.0f),128 * 0.4f);
	vec3 specular = specular_strength * light_color;

	vec3 color = (specular + diffuse + ambient) * object_color;

	result = vec4(color, 1.0f);
	
	return result;
}