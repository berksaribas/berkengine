#version 400 core

out vec4 outColor;

in vec2 passedTextureCoordinates;

uniform sampler2D ui;

void main()
{
    outColor = texture(ui, passedTextureCoordinates);
}

