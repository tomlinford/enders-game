precision mediump float;

struct Material {
    float Ns;   // specular component
    float d;    // dissolve component (pass in as alpha value)
    vec3 Ka;    // ambient color
    vec3 Kd;    // diffuse color
    vec3 Ks;    // specular color
    vec3 Ke;    // emissivity
};

struct AmbientLight {
    vec3 color;
};

struct SpotLight {
    vec3 color;
    vec3 position;
    vec3 direction;
    float radius;
};

uniform vec3 worldspaceCameraPosition;
uniform Material mat;
uniform AmbientLight amb;
uniform SpotLight spot;
uniform sampler2D texture;

varying vec3 worldspacePosition;
varying vec2 textureCoord;
varying vec3 worldspaceNormal;

void main() {
    vec3 lightSource = spot.position;
    vec3 lightColor = spot.color;
    float dist = distance(lightSource, worldspacePosition) +
                 distance(worldspacePosition, worldspaceCameraPosition);
    
    vec3 materialAmb = mat.Ka;
    vec3 materialDiff = mat.Kd;
    vec3 materialSpec = mat.Ks;
    float shininess = mat.Ns;
    lightSource = vec3(5, 0, 5);
    lightColor = vec3(1, 1, 3);

    vec3 L = normalize(lightSource - worldspacePosition);
    vec3 N = normalize(worldspaceNormal);
    vec3 R = normalize(reflect(L, N));
    vec3 V = normalize(worldspaceCameraPosition - worldspacePosition);
    
    vec3 amb = materialAmb * lightColor / 10.0;
    vec3 diff = clamp(dot(N, L), 0., 1.) * materialDiff * lightColor;// / sqrt(dist + 1.0);
    vec3 spec = pow(clamp(-dot(V, R), 0., 1.), shininess) * materialSpec * lightColor;// / sqrt(dist + 1);
    
    gl_FragColor = vec4(amb + diff + spec, 1);
    //if (worldspaceNormal.x < .01)
    	//gl_FragColor = vec4(1, 0, 0, 1);
}