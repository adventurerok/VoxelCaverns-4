#version 150 compatibility

out vec4 theColor;
out vec3 texCoord;

in vec4 inVertex;
in vec4 inColor;
in vec3 inTex;
in vec4 inData6;

uniform vec3 skyLight;

vec4 getLightColor();
vec3 flickerLight();

void main(){
    theColor = inColor * getLightColor();
    gl_Position = gl_ModelViewProjectionMatrix * inVertex;
    texCoord = inTex;
}

vec4 getLightColor(){
	vec3 inLight = flickerLight();
	return vec4(max(inLight.r, skyLight.r * inData6.a), max(inLight.g, skyLight.g * inData6.a), max(inLight.b, skyLight.b * inData6.a), 1.0);

}

vec3 flickerLight(){
	return inData6.rgb;
}


