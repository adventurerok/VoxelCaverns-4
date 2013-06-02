#version 150 compatibility

out vec4 theColor;
out vec3 texCoord;

in vec4 inVertex;
in vec4 inColor;
in vec3 inTex;

uniform float modelIndex;
uniform vec4 colorMod;

void main(){
    theColor = inColor * colorMod;
    gl_Position = gl_ModelViewProjectionMatrix * inVertex;
    texCoord = vec3(inTex.xy, modelIndex);
}


