#version 130

varying vec4 theColor;

void main(){
    gl_FragColor = theColor * vec4(1, 1, 1, 0.8);
}