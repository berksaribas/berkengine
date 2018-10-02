#version 400 core

in vec4 shadowCoordinates;
in vec3 surfaceNormal, lightVector, cameraVector;
in vec2 passedTextureCoordinates;

out vec4 outColor;

uniform vec3 lightColor;
uniform sampler2D textureSampler;
uniform sampler2D depthMap;
uniform sampler2D colorMap;

float texture2DCompare(sampler2D depths, vec2 uv, float compare, float bias){
    float depth = texture2D(depths, uv).r;
    return compare - bias > depth  ? 1.0 : 0.0;
}

float shadowSampler(sampler2D depths, vec2 size, vec2 uv, float compare, float bias){
    vec2 texelSize = vec2(1.0)/size;
    vec2 f = fract(uv*size+0.5);
    vec2 centroidUV = floor(uv*size+0.5)/size;

    float lb = texture2DCompare(depths, centroidUV+texelSize*vec2(0.0, 0.0), compare, bias);
    float lt = texture2DCompare(depths, centroidUV+texelSize*vec2(0.0, 1.0), compare, bias);
    float rb = texture2DCompare(depths, centroidUV+texelSize*vec2(1.0, 0.0), compare, bias);
    float rt = texture2DCompare(depths, centroidUV+texelSize*vec2(1.0, 1.0), compare, bias);

    float a = mix(lb, lt, f.y);
    float b = mix(rb, rt, f.y);
    float c = mix(a, b, f.x);

    return c;
}

float VSM(sampler2D depths, vec2 uv, float compare){
    vec2 moments = texture2D(depths, uv).xy;

    if (compare <= moments.x)
        return 1.0;

    float variance = moments.y - (moments.x*moments.x);
    variance = max(variance, 0.0002);

    float d = compare - moments.x;
    float p_max = variance / (variance + d*d);

    return p_max;
}

float ShadowCalculation(vec3 lightVector, vec3 unitNormal) {

    vec4 point = shadowCoordinates / shadowCoordinates.w;

    float shadow = 0.0;

//    vec2 texelSize = 1.0 / textureSize(depthMap, 0);
//
//    for(int x = -1; x <= 1; ++x)
//    {
//        for(int y = -1; y <= 1; ++y)
//        {
//            vec2 off = vec2(x,y) * texelSize;
//            shadow += shadowSampler(depthMap, 1 / texelSize, point.xy + off, point.z, 0);
//        }
//    }
//    shadow /= 9.0;

//    if(shadowCoordinates.z > 1.0)
//        return 0;

    shadow = VSM(colorMap, point.xy, point.z);

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
    vec3 diffuseColor = brightness * lightColor * shadow;

    //Specular Lightning
    float specularness = pow(max(dot(unitHalfway, unitNormal), 0.0), 32);
    vec3 specularColor = 0.4f * specularness * lightColor * shadow;

    vec3 totalColor = (ambientColor + diffuseColor) * texture(textureSampler, passedTextureCoordinates).rgb + specularColor;

    outColor = vec4(totalColor, texture(textureSampler, passedTextureCoordinates).a);
}
