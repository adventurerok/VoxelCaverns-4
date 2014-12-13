#version 130

varying vec4 theColor;
varying vec3 texCoord;

attribute vec4 inVertex;
attribute vec4 inColor;
attribute vec3 inTex;

uniform vec3 light;

void main(){
    theColor = inColor * vec4(light, 1.0);
    gl_Position = gl_ModelViewProjectionMatrix * inVertex;
    texCoord = inTex;
}


