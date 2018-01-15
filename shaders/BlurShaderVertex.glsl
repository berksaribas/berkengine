#version 400

in vec2 position;
in vec2 textureCoordinates;

out vec2 passedTextureCoordinates;

void main() {
    passedTextureCoordinates = textureCoordinates;
    gl_Position = vec4(position, 1.0, 1.0);
}
