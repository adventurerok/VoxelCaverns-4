#version 150 compatibility

out vec4 theColor;
out vec3 texCoord;

in vec4 inVertex;
in vec4 inColor;
in vec3 inTex;

uniform vec3 light;

void main(){
    theColor = inColor * vec4(light, 1.0);
    gl_Position = gl_ModelViewProjectionMatrix * inVertex;
    texCoord = inTex;
}


