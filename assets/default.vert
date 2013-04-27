uniform mat4 MVP;

attribute vec3 worldspacePosition;

void main() {
	gl_Position = MVP * vec4(worldspacePosition, 1);
}