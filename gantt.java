/** 
 * Esta clase realiza el diagrama de Gantt
 * @author Equipo 5
 * @version 04/04/2023
 * */

public class gantt {
    public String id;
    public int tInicia, tTermina, tllega;

    /**
     * Inicializa la cola de procesos listos y define el tamaño de RAM y el quatum
     * @param data El parametro sera un objeto proceso
     * @param tInicia El parametro sera el tiempo en el que sube el proceso
     * @param tTermina El parametro sera el tiempo en el que baja el proceso
     * @param tllega El parametro sera el tiempo en el que llega el proceso
     * @param quantum El parametro sera el tiempo en msg que se ejecuta cada proceso
     */
    public gantt (proceso data, int tInicia, int tTermina, int tllega, int quantum){
        this.id = data.id;
        this.tllega = tllega;
        this.tInicia = tInicia;
        if (tTermina > quantum) {
            this.tTermina = this.tInicia+quantum;
        }else{
            this.tTermina = this.tInicia+tTermina;
        }
    }
}