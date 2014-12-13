#version 130

varying vec4 theColor;

attribute vec4 inVertex;
attribute vec4 inColor;

void main(){
    theColor = inColor;
    gl_Position = gl_ModelViewProjectionMatrix * inVertex;
}
