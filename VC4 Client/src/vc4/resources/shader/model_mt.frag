#version 130

varying vec4 theColor;
varying vec4 texCoord;

uniform sampler2DArray theTex;


void main(){
    gl_FragColor = texture(theTex, texCoord.xyz) * vec4(texture(theTex, texCoord.xyw).rgb, 1) * theColor;
}