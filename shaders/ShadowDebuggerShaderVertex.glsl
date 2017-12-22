#version 400 core

in vec3 position;
in vec2 textureCoordinates;

out vec2 passedTextureCoordinates;

void main()
{
    passedTextureCoordinates = textureCoordinates;
    gl_Position = vec4(position, 1.0);
}

