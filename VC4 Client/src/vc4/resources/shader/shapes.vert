#version 150 compatibility

out vec4 theColor;

in vec4 inVertex;
in vec4 inColor;




void main(){
    theColor = inColor;
    gl_Position = gl_ModelViewProjectionMatrix * inVertex;
}


