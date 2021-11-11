#version 330

layout (location=0) in vec4 vPosition;
layout (location=1) in vec2 a_TexCoordinate;

out vec2 v_TexCoordinate;

uniform mat4 viewMatrix;
uniform mat4 worldMatrix;
uniform mat4 projectionMatrix;

void main()
{
	gl_Position = projectionMatrix * viewMatrix * worldMatrix * vPosition;
    v_TexCoordinate = a_TexCoordinate;
}