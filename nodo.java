/** 
 * Esta clase contiene la informacion de cada nodo
 * @author Equipo 5
 * @version 04/04/2023
 * */

public class nodo {
    // Atributos
    private proceso data;
    private nodo sig;
    
    /**
     * Crea un nuevo nodo
     * @param data El parametro sera un objeto proceso
     */
    public nodo(proceso data){
        this.data = data;
    }

    /**
     * Metodos para obtener la informacion del proceso
     */
    public proceso getData(){
        return data;
    }

    /**
     * Metodos para reducir el tiempo de ejecucion
     */
    public void redux(){
        this.data.teje -= 1;
    }
    /**
     * Metodo para obtener el nodo siguiente
     */
    public nodo getSig(){
        return sig;
    }

    /**
     * Metodo para colocar el nodo siguiente
     * @param sig El parametro es un objeto nodo al cual sera el siguiente
     */
    public void setSig(nodo sig){
        this.sig = sig;
    }
}