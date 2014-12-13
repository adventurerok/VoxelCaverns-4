#version 130

varying vec4 theColor;
varying vec3 texCoord;

uniform sampler3D theTex;


void main(){
    gl_FragColor = texture(theTex, texCoord) * theColor;
}