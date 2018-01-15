#version 400 core

in vec3 position;

uniform mat4 transformationMatrix, lightSpaceMatrix;

void main(void) {
    gl_Position = lightSpaceMatrix * transformationMatrix * vec4(position, 1.0);
}