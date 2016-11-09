precision mediump float;       		// Set the default precision to medium. We don't need as high of a
													// precision in the fragment shader.				


varying vec4 v_Color;          	// This is the color from the vertex shader interpolated across the
		  									 //triangle per fragment.

varying vec3 v_Position;
varying vec3 v_Normal;
varying vec3 v_LightVec;              //Vector from fragment to light
void main() {                    	// The entry point for our fragment shader.
    float diffuse = max(dot(normalize(v_LightVec), v_Normal), 0.1);
    float distance = length(v_LightVec);
    diffuse = diffuse / (1.0 + (0.25 * distance * distance));
    gl_FragColor = v_Color * diffuse;     	// Pass the color directly through the pipeline.


}