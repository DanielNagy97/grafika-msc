#version 330
precision mediump float;

in vec2 v_TexCoordinate;
out vec4 gl_FragColor;

uniform vec4 vColor;
uniform sampler2D u_Texture;

void main()
{
    gl_FragColor = (vColor * texture2D(u_Texture, v_TexCoordinate));
}