#version 400 core

in vec3 position;

out vec3 passedTextureCoordinates;

uniform mat4 projectionMatrix, viewMatrix;

void main()
{
    passedTextureCoordinates = position;
    gl_Position = projectionMatrix * viewMatrix * vec4(position, 1.0);
}