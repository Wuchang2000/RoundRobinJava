import java.util.ArrayList;

/** 
 * Esta clase realiza el algoritmo de planeacion de procesos Round Robin
 * @author Equipo 5
 * @version 04/04/2023
 * */

public class RoundRobin {
    
    private nodo iniL, finL, iniE, finE;
    private int tiempoTotal, memoriaTotal, memoriaUsada, quantum;
    private ArrayList<proceso> planeado = new ArrayList<proceso>();
    public ArrayList<gantt> diagrama = new ArrayList<gantt>();
    private ArrayList<String> ids = new ArrayList<String>();
    
    /**
     * Inicializa la cola de procesos listos y define el tamaño de RAM y el quantum
     * @param memoriaTotal El parametro sera la memoria RAM simulada
     * @param quantum El parametro sera el tiempo en msg que se ejecuta cada proceso
     */
    public RoundRobin(int memoriaTotal, int quantum){
        this.finL = null;
        this.iniL = null;
        this.memoriaTotal = memoriaTotal;
        this.quantum = quantum;
    }

    /**
     * Metodo que busca si el id a registrar ya existe en los procesos actuales 
     * @param id Identificador que se intenta registar
     */
    public boolean searchIds(String id){
        return this.ids.contains(id);
    }

    /**
     * Metodo que inserta el nuevo proceso 
     * @param data El parametro sera un objeto proceso
     */
    public void inserta(proceso data){
        this.ids.add(data.id);
        // Valida si el tiempo de llegada es 0
        if (data.tllega != 0) {
            // Si no es 0 se inserta en una cola de planificados
            insertaP(data);
            return;
        }
        // Inserta el proceso en la cola de procesos listos
        insertaL(data);
    }

    /**
     * Metodo para insertar un nuevo proceso a la cola de procesos listos 
     * @param data El parametro sera un objeto proceso
     */
    private void insertaL(proceso data){
        nodo nuevo = new nodo(data);
        // Si esta vacia
        if(iniL == null){
            iniL = nuevo;
            finL = nuevo;
        // Si tiene procesos en espera
        }else{
            finL.setSig(nuevo);
            finL = nuevo;
        }
    }

    /**
     * Metodo para insertar proceso en el arreglo de planeados 
     * @param data El parametro sera un objeto proceso
     */
    private void insertaP(proceso data){
        planeado.add(data);
    }

    /**
     * Metodo para insertar un nuevo proceso a la cola de procesos listos para ejecucion 
     * @param data El parametro sera un objeto proceso
     */
    private void insertaE(proceso data){
        nodo nuevo = new nodo(data);
        // Si esta vacia
        if(iniE == null){
            iniE = nuevo;
            finE = nuevo;
        // Si tiene procesos en espera
        }else{
            finE.setSig(nuevo);
            finE = nuevo;
        }
    }

    /**
     * Metodo que añade los procesos con tiempos de llegada diferentes 
     */
    private void expandePost(){
        for (int i = 0; i < planeado.size(); i++) {
            // Si coincide su tiempo de llegada con el tiempo total
            if (planeado.get(i).tllega == tiempoTotal) {
                insertaL(planeado.get(i));
                mostrarColas();
                expande();
                planeado.remove(i);
            }
        }
    }
    /**
     * Se inicializa el algoritmo de planeacion
     */
    public void rr(){
        expande();
        procesa();
    }

    /**
     * Metodo que añade el proceso que quepa segun la memoria
     */
    private void expande(){
        while (iniL != null) {
            // Si se excede la memoria no entra el proceso
            if (memoriaUsada+iniL.getData().size > memoriaTotal) {
                break;
            }
            insertaE(iniL.getData());
            memoriaUsada += iniL.getData().size;
            System.out.println("Subio el proceso "+iniL.getData().id+" y restan "+
            (memoriaTotal-memoriaUsada)+" unidades de memoria");
            iniL = iniL.getSig();
            mostrarColas();
        }
    }

    /**
     * Metodo que se encarga de mandar a ejecutar cada proceso
     */
    private void procesa(){
        // Ciclo que mantiene la espera en caso de que no 
        // halla procesos listos para ejecucion pero si planeados
        while (planeado.isEmpty() != true || iniE != null) {
            // Si aun no llega ningun proceso se cuenta el tiempo
            while (iniE == null) {
                this.tiempoTotal++;
                expandePost();
            }
            // En cuanto se encuantre un proceso empieza a ejecutarse
            while (iniE != null) {
                boolean restado = false;
                mostrarColas();
                expande();
                // Añade los procesos a una lista para poder realizar los calculos de tiempos
                diagrama.add(new gantt(iniE.getData(), tiempoTotal,
                iniE.getData().teje, iniE.getData().tllega, quantum));
                // Este ciclo realiza contabiliza el quantum
                for (int i = 0; i < quantum; i++) {
                    // Si el tiempo de ejecucion no a llegado a 0 se reduce su tiempo en
                    // una unidad y se aumenta el tiempo total, se revisa si ya es tiempo
                    // de llegada de un proceso
                    if (iniE.getData().teje != 0) {
                        System.out.println(iniE.getData().id+" en ejecución "+iniE.getData().teje+"msg");
                        iniE.redux();
                        this.tiempoTotal++;
                        expandePost();
                    // Si el tiempo de ejecucion es 0 se quita el peso del proceso
                    // se avisa con la bandera que ya no es necesario restarlo y
                    // se revisa si ya es tiempo de llegada de un proceso
                    }else{
                        memoriaUsada -= iniE.getData().size;
                        restado = true;
                        expandePost();
                        expande();
                        break;
                    }
                }
                // En caso de todavia tener tiempo de ejecucion se vuelve a colocar
                // en la cola de procesos listos para ejecucion
                if (iniE.getData().teje != 0) {
                    insertaL(iniE.getData());
                // En caso de ya no tener tiempo de ejecucion
                }else{
                    // Se revisa si ya es tiempo de llegada de un proceso
                    expandePost();
                    expande();
                }
                // Se revisa si ya se libero la memoria del proceso
                if (restado == false) {
                    memoriaUsada -= iniE.getData().size;
                }
                // Se avanza al siguiente proceso
                iniE = iniE.getSig();
            }
        }
        // Calculamos los tiempos de espera, respuesta y ejecucion
        calcula();
    }

    /**
     * Metodo para calular los tiempos de espera, respuesta y ejecucion
     */
    private void calcula(){
        int indice = 0;
        float sumEsp = 0, sumResp = 0, sumEje = 0;
        System.out.println("\n\n\n─────────────────────────────────────────Calculo de tiempos─────────────────────────────────────────────");
        // Calculamos el tiempo de espera
        // Recorremos el conjunto de identificadores
        for (int i = 0; i < ids.size(); i++) {
            int sumSube = 0;
            // Encontramos la ultima vez que sube
            for (int j = 0; j < diagrama.size(); j++) {
                if (ids.get(i) == diagrama.get(j).id) {
                    indice = j;
                    sumSube += diagrama.get(indice).tTermina-diagrama.get(indice).tInicia;
                }
            }
            // Ultima rafaga
            sumSube -= diagrama.get(indice).tTermina-diagrama.get(indice).tInicia;
            // Sumamos TultimoSube-Tllega-TyaEjecutado
            sumEsp += diagrama.get(indice).tInicia-diagrama.get(indice).tllega-sumSube;
        }
        System.out.println("Tiempo de espera: "+sumEsp/ids.size());
        // Calculamos el tiempo de respuesta
        // Recorremos el conjunto de identificadores
        for (int i = 0; i < ids.size(); i++) {
            // Encontramos la ultima vez que sube
            for (int j = 0; j < diagrama.size(); j++) {
                if (ids.get(i) == diagrama.get(j).id) {
                    indice = j;
                    break;
                }
            }
            // Primera rafaga
            // Sumamos TPrimeraVezSube-Tllega
            sumResp += diagrama.get(indice).tInicia-diagrama.get(indice).tllega;
        }
        System.out.println("Tiempo de respuesta: "+sumResp/ids.size());
        // Calculamos el tiempo de ejecucion
        // Recorremos el conjunto de identificadores
        for (int i = 0; i < ids.size(); i++) {
            // Encontramos la ultima vez que sube
            for (int j = 0; j < diagrama.size(); j++) {
                if (ids.get(i) == diagrama.get(j).id) {
                    indice = j;
                }
            }
            // Ultima rafaga
            // Sumamos TUltimoSubeFinal-Tllega
            sumEje += diagrama.get(indice).tTermina-diagrama.get(indice).tllega;
        }
        System.out.println("Tiempo de ejecucion: "+sumEje/ids.size());
    }

    /**
     * Metodo que muestra el estado de cada cola de datos, el tiempo de total y el uso de memoria
     */
    private void mostrarColas(){
        ArrayList<String> colaVolteada = new ArrayList<String>();
        nodo temp = iniL;
        // Se extraen los procesos de la cola de procesos listos
        while (temp != null) {
            colaVolteada.add(temp.getData().id);
            temp = temp.getSig();
        }
        System.out.println("Cola de procesos listos");
        // Si imprime el arreglo al reves
        for (int i = colaVolteada.size()-1; i >= 0; i--) {
            //formatoColas(colaVolteada.get(i));
            System.out.print(colaVolteada.get(i)+" | ");
        }
        System.out.println("");
        colaVolteada.clear();
        temp = iniE;
        // Se extraen los procesos de la cola de procesos listos para ejecucion
        while (temp != null) {
            colaVolteada.add(temp.getData().id);
            temp = temp.getSig();
        }
        System.out.println("Cola de procesos listos para ejecucion");
        // Si imprime el arreglo al reves
        for (int i = colaVolteada.size()-1; i > 0; i--) {
            //formatoColas(colaVolteada.get(i));
            System.out.print(colaVolteada.get(i)+" | ");
        }
        System.out.println("");
        System.out.println("Proceso en ejecucion");
        if (colaVolteada.isEmpty() != true) {
            System.out.println(colaVolteada.get(0));
        }else{
            System.out.println("");
        }
        System.out.println("Tiempo total: "+tiempoTotal);
        System.out.println("Memoria restante: "+(memoriaTotal-memoriaUsada));
        System.out.println("\n\n\n─────────────────────────────────────────Información actualizada─────────────────────────────────────────────");
    }
}