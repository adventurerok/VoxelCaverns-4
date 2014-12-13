#version 130

varying vec4 theColor;
varying vec3 texCoord;

attribute vec4 inVertex;
attribute vec4 inColor;
attribute vec3 inTex;

void main(){
    theColor = inColor;
    gl_Position = gl_ModelViewProjectionMatrix * inVertex;
    texCoord = inTex;
}


