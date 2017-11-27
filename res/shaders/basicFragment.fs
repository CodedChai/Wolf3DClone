#version 330

in vec2 texCoord0;
out vec4 fragColor;

uniform vec3 baseColor;
uniform sampler2D sampler;

void main()
{
	vec4 textureColor = texture(sampler, texCoord0.xy);
	vec4 color = vec4(baseColor, 1);

	fragColor = textureColor * color;
}