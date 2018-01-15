#version 400 core

out vec4 frag_color;

void main(void) {
    float moment1 = gl_FragCoord.z;
    float moment2 = moment1*moment1;

    float dx = dFdx(moment1);
    float dy = dFdy(moment1);
    moment2 += 0.25*(dx*dx+dy*dy);

    frag_color = vec4(moment1, moment2, 0.0, 0.0);
}
