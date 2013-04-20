#version 150 compatibility

in vec4 theColor;



void main(){
    gl_FragColor = theColor * vec4(1, 1, 1, 0.8);
}