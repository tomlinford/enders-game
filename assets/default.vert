/* Defined in model space */
attribute vec3 vertexCoordinates;
attribute vec3 normalCoordinates;
attribute vec3 texCoordinates;

/* Transformation matrices */
uniform mat4 MVP;
uniform mat4 M;
uniform mat4 V;
uniform mat4 P;

void main() {
	gl_Position = MVP * vec4(vertexCoordinates, 1);
}