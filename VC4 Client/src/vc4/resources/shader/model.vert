#version 130

varying vec4 theColor;
varying vec3 texCoord;

attribute vec4 inVertex;
attribute vec4 inColor;
attribute vec3 inTex;

uniform float modelIndex;
uniform vec4 colorMod;

void main(){
    theColor = inColor * colorMod;
    gl_Position = gl_ModelViewProjectionMatrix * inVertex;
    texCoord = vec3(inTex.xy, modelIndex);
}


