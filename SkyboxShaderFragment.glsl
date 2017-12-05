#version 400 core

out vec4 outColor;

in vec3 passedTextureCoordinates;

uniform samplerCube skybox;

void main()
{
    outColor = texture(skybox, passedTextureCoordinates);
}