#version 130

varying vec4 theColor;
varying vec3 texCoord;

attribute vec4 inVertex;
attribute vec4 inColor;
attribute vec3 inTex;
attribute vec4 inData6;

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


