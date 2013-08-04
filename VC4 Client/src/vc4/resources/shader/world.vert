#version 150 compatibility

out vec4 theColor;
out vec3 texCoord;

in vec4 inVertex;
in vec4 inColor;
in vec3 inTex;
in vec4 inCustom1;

uniform vec3 skyLight;


void main(){
    theColor = inColor * vec4(skyLight.xyz, 1.0);
    gl_Position = gl_ModelViewProjectionMatrix * inVertex;
    texCoord = inTex;
}


