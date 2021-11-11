precision mediump float;

varying vec2 v_TexCoordinate;

uniform vec4 vColor;
uniform sampler2D u_Texture;

void main() {
    gl_FragColor = (vColor * texture2D(u_Texture, v_TexCoordinate));
}
