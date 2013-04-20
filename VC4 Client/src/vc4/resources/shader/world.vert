#version 150 compatibility

out vec4 theColor;
out vec3 texCoord;

in vec4 inVertex;
in vec4 inColor;
in vec3 inTex;




void main(){
    theColor = inColor;
    gl_Position = gl_ModelViewProjectionMatrix * inVertex;
    texCoord = inTex;
}


