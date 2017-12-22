#version 400 core

in vec3 position, normal;
in vec2 textureCoordinates;

out vec4 shadowCoordinates;
out vec3 surfaceNormal, lightVector, cameraVector;
out vec2 passedTextureCoordinates;

uniform mat4 transformationMatrix, projectionMatrix, viewMatrix, depthBias;
uniform vec3 cameraPosition;
uniform vec4 lightPosition;
uniform int textureRepeat;

void main(void) {
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);

    gl_Position = projectionMatrix * viewMatrix * worldPosition;

    surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;

    if(lightPosition.w == 0.0) { //It is a directional light
        lightVector = lightPosition.xyz;
    } else { //It is a point light
        lightVector = lightPosition.xyz - worldPosition.xyz;
    }

    cameraVector = cameraPosition - worldPosition.xyz;

    passedTextureCoordinates = textureCoordinates * textureRepeat;

    shadowCoordinates = depthBias * worldPosition;
}