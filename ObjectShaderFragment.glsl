#version 400 core

in vec4 shadowCoordinates;
in vec3 surfaceNormal, lightVector, cameraVector;
in vec2 passedTextureCoordinates;

out vec4 outColor;

uniform vec3 lightColor;
uniform sampler2D textureSampler;
uniform sampler2D shadowMap;

float ShadowCalculation(vec3 lightVector, vec3 unitNormal) {
    float closestDepth = texture(shadowMap, shadowCoordinates.xy).r;

    float currentDepth = shadowCoordinates.z;

    float bias = max(0.05 * (1.0 - dot(unitNormal, lightVector)), 0.005);

    float shadow = 0.0;
    vec2 texelSize = 1.0 / textureSize(shadowMap, 0);
    for(int x = -1; x <= 1; ++x)
    {
        for(int y = -1; y <= 1; ++y)
        {
            float pcfDepth = texture(shadowMap, shadowCoordinates.xy + vec2(x, y) * texelSize).r;
            shadow += currentDepth > pcfDepth  ? 1.0 : 0.0;
        }
    }
    shadow /= 9.0;

    if(shadowCoordinates.z > 1.0)
        shadow = 0.0;

    return shadow;
}


void main(void) {

    vec3 unitNormal = normalize(surfaceNormal); //Normal of the surface
    vec3 unitLight = normalize(lightVector); // Vector from light to object
    vec3 unitCamera = normalize(cameraVector); //Vector from camera to object
    vec3 unitHalfway = normalize(unitLight + unitCamera); //Halfway vector for Blinn Phong model
    vec3 reflectDir = reflect(-unitLight, unitNormal);

    //Shadow
    float shadow = ShadowCalculation(unitLight, unitNormal);

    //Ambient Lightning
    float ambientStrength = 0.4;
    vec3 ambientColor = ambientStrength * lightColor;

    //Diffuse Lightning
    float brightness = max(dot(unitNormal, unitLight), 0.0);
    vec3 diffuseColor = brightness * lightColor * (1.0 - shadow);

    //Specular Lightning
    float specularness = pow(max(dot(unitHalfway, unitNormal), 0.0), 32);
    vec3 specularColor = 0.3f * specularness * lightColor * (1.0 - shadow);

    outColor = vec4(ambientColor + diffuseColor, 1.0) * texture(textureSampler, passedTextureCoordinates) + vec4(specularColor, 1.0);
}
