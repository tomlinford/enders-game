precision mediump float;

struct Material {
    float Ns;   		// specular component
    float d;    		// dissolve component (pass in as alpha value)
    vec3 Ka;    		// ambient color
    vec3 Kd;    		// diffuse color
    vec3 Ks;    		// specular color
    vec3 Ke;    		// emissivity
    sampler2D texture; 	// texture
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

varying vec3 worldspacePosition;
varying vec2 textureCoord;
varying vec3 worldspaceNormal;

void main() {
	vec3 lightSource = spot.position;
    vec3 lightColor = spot.color;
    
    vec3 materialAmb = mat.Ka;
    vec3 materialDiff = mat.Kd;
    vec3 materialSpec = mat.Ks;
    float shininess = mat.Ns;
    lightSource = vec3(8, .75, -.75);
    lightColor = vec3(1.0, 1.0, 1.0);
    float dist = distance(lightSource, worldspacePosition) +
                 distance(worldspacePosition, worldspaceCameraPosition);

    vec3 L = normalize(lightSource - worldspacePosition);
    vec3 N = normalize(worldspaceNormal);
    vec3 R = normalize(reflect(L, N));
    vec3 V = normalize(worldspaceCameraPosition - worldspacePosition);
    
    vec3 direction = vec3(3, 0, 0) - lightSource;
    float radius = 3.1415 / 24.;
    radius = spot.radius;
    bool inSpotlight = dot(-normalize(direction), L) > cos(radius);
    
    vec3 amb = materialAmb * lightColor / 10.0 * mat.Ka;
    vec3 diff = vec3(0);
    vec3 spec = vec3(0);
    if (inSpotlight) {
    	diff = clamp(dot(N, L), 0., 1.) * materialDiff * lightColor * mat.Kd / sqrt(dist + 1.0);
    	spec = pow(clamp(-dot(V, R), 0., 1.), shininess) * materialSpec * lightColor * mat.Ks / sqrt(dist + 1.0);
    }
    
    gl_FragColor = vec4(amb + diff + spec + mat.Ke, 1);
    //if (!inSpotlight)
    	//gl_FragColor = vec4(1, 0, 0, 1);
}