package Alura_Challenge.demo.servises;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
