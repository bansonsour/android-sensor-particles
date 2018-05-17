uniform mat4 u_Model;
uniform mat4 u_View;
uniform mat4 u_Proj;

attribute vec4 a_Position;

void main()
{
    gl_Position = u_Proj * u_View * u_Model * a_Position;
    gl_PointSize = 10.0;
}