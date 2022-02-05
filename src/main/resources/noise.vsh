#version 460

in vec4 vertexPosition;
in vec4 vertexColor;

uniform mat4 modelViewProjectionMatrix;

out vec3 color;

void main() {
    gl_Position = modelViewProjectionMatrix * vertexPosition;
    color = vertexColor.xyz;
}