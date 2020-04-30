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
vec3 light_position[4];
vec3 light_color[4];

vec4 computeLitColorForSingleSource(vec3 normal, vec3 fragment_position, vec3 view_position, vec3 light_position, vec3 light_color);

void main()
{
	light_position[0] = vec3(0,0,0);
	light_color[0] = vec3(0.5f,0.5f,0.5f);
	light_position[1] = vec3(8,5,10);
	light_color[1] = vec3(0,0,0.5f);
	light_position[2] = vec3(-4,-7,3);
	light_color[2] = vec3(0.5f,0,0);
	light_position[3] = vec3(-10,5,5);
	light_color[3] = vec3(0,0.5f,0);
	
	color = vec4(0,0,0,1);
	for(int i = 0; i != 4; ++i){
		color += computeLitColorForSingleSource(out_normal, out_fragmentPosition, camera_position, light_position[i], light_color[i]);
	}
	
} 

vec4 computeLitColorForSingleSource(vec3 normal, vec3 fragment_position, vec3 view_position, vec3 light_position, vec3 light_color){
	vec4 result;
	vec3 object_color = vec3(1,1,1);
	
	//Ambient 
	float ambient_strength = 0.3;
	vec3 ambient = ambient_strength * light_color;

	//Diffuse
	vec3 light_direction = normalize(light_position - fragment_position);
	float diffuse_strength = 0.5 * max(dot(normalize(normal), light_direction), 0.0f);
	vec3 diffuse = diffuse_strength * light_color;

	//Specular
	vec3 view_direction = normalize(view_position - fragment_position);
	vec3 reflect_light_direction = reflect(-light_direction, normalize(normal));
	float specular_strength =  0.5 * pow(max(dot(reflect_light_direction, view_direction), 0.0f),128 * 0.4f);
	vec3 specular = specular_strength * light_color;

	vec3 color = (specular + diffuse + ambient) * object_color;

	result = vec4(color, 1.0f);
	
	return result;
}