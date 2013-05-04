uniform mat4 MVP;
uniform mat4 M;

attribute vec3 vertexCoordinates;
attribute vec2 texCoordinates;
attribute vec3 normalCoordinates;

varying vec3 worldspacePosition;
varying vec2 textureCoord;
varying vec3 worldspaceNormal;

void main() {
	gl_Position = (MVP * vec4(vertexCoordinates, 1));
	worldspacePosition = (M * vec4(vertexCoordinates, 1)).xyz;
	textureCoord = texCoordinates;
	worldspaceNormal = (M * vec4(normalCoordinates, 0)).xyz;
}