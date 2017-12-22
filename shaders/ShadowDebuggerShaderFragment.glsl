#version 400 core

out vec4 outColor;

in vec2 passedTextureCoordinates;

uniform sampler2D depthMap;

void main()
{
    float depthValue = texture(depthMap, passedTextureCoordinates).r;
    outColor = vec4(vec3(depthValue), 1.0);
}

