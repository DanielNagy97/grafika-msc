attribute vec4 vPosition;
attribute vec2 a_TexCoordinate;

uniform mat4 viewMatrix;
uniform mat4 worldMatrix;
uniform mat4 projectionMatrix;

varying vec2 v_TexCoordinate;

void main() {
    gl_Position = projectionMatrix * viewMatrix * worldMatrix * vPosition;
    v_TexCoordinate = a_TexCoordinate;
}