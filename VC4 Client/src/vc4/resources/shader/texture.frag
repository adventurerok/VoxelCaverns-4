#version 150 compatibility

in vec4 theColor;
in vec3 texCoord;

uniform sampler2DArray theTex;


void main(){
    gl_FragColor = texture(theTex, texCoord) * theColor;
}