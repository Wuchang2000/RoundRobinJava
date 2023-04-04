/** 
 * Esta clase contiene la informacion de proceso
 * @author Equipo 5
 * @version 04/04/2023
 * */

public class proceso {
    // Atributos
    public String id, nombre;
    public Integer size, teje, prioridad, tllega;
    
    /**
     * Crea un nuevo proceso
     */
    public proceso(){
    }

    /**
     * Metodo que asigna un valor al id
     * @param id El parametro es el identificador del proceso
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Metodo que asigna un valor al nombre
     * @param nombre El parametro es el nombre del proceso
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Metodo que asigna un valor a la prioridad
     * @param nombre El parametro es el valor de la prioridad
     */
    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    /**
     * Metodo que asigna un valor al tamaño
     * @param size El parametro es el valor del tamaño del proceso
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * Metodo que asigna un valor del tiempo de ejecucion
     * @param teje El parametro es el valor del tiempo de ejecucion
     */
    public void setTeje(Integer teje) {
        this.teje = teje;
    }

    /**
     * Metodo asigna el valor del tiempo de llegada
     * @param tllega El parametro es el valor del tiempo de llegada 
     */
    public void setTllega(Integer tllega) {
        this.tllega = tllega;
    }

    /**
     * Metodo para imprimir con formato los procesos
     */
    public String toString(){
        return "ID: "+this.id+" Nombre: "+this.nombre+" Teje: "+this.teje;
    }
}