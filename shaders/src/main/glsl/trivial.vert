#version 120
attribute vec2 coord2d;  
varying vec2 surfacePosition;
                
void main(void) {
    gl_Position = vec4(coord2d, 0.0, 1.0); 
    surfacePosition = coord2d;
}

