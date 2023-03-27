import java.util.ArrayList;

class proceso {
    // Atributos
    public String id, nombre;
    public Integer size, teje, prioridad, tllega;

    // Constructor
    public proceso(String id, String nombre, Integer size,
                        Integer teje, Integer prioridad, Integer tllega){
        this.id = id;
        this.nombre = nombre;
        this.prioridad = prioridad;
        this.size = size;
        this.teje = teje;
        this.tllega = tllega;
    }

    // Metodo para imprimir con formato los procesos
    public String toString(){
        return "ID: "+this.id+" Nombre: "+this.nombre+" Teje: "+this.teje;
    }
}

class nodo {
    // Atributos
    private proceso data;
    private nodo sig;
    //Construtor
    public nodo(proceso data){
        this.data = data;
    }
    // Metodos para obtener la informacion del proceso
    public proceso getData(){
        return data;
    }
    // Metodos para reducir el tiempo de ejecucion
    public void redux(){
        this.data.teje -= 1;
    }
    // Metodo para obtener el nodo siguiente 
    public nodo getSig(){
        return sig;
    }
    // Metodo para colocar el nodo siguiente
    public void setSig(nodo sig){
        this.sig = sig;
    }
}

class colaProcesos {

    private nodo iniL, finL, iniE, finE;
    private int tiempoTotal, memoriaTotal, memoriaUsada, quantum;
    private ArrayList<proceso> planeado = new ArrayList<proceso>();
    // Constructor
    public colaProcesos(int memoriaTotal, int quantum){
        this.finL = null;
        this.iniL = null;
        this.memoriaTotal = memoriaTotal;
        this.quantum = quantum;
    }
    // Metodo para insertar un nuevo proceso
    public void inserta(proceso data){
        // Valida si el tiempo de llegada es 0
        if (data.tllega != 0) {
            // Si es 0 se inserta en una cola de planificados
            insertaP(data);
            return;
        }
        // Inserta el proceso en la cola de procesos listos
        insertaL(data);
    }
    // Metodo para insertar un nuevo proceso a la cola de procesos listos
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
    // Metodo para insertar proceso en el arreglo de planeados
    private void insertaP(proceso data){
        planeado.add(data);
    }
    // Metodo para insertar un nuevo proceso a la cola de procesos listos para ejecucion
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
    // Metodo que añadde los procesos con tiempos de llegada diferentes
    private void expandePost(){
        for (int i = 0; i < planeado.size(); i++) {
            // Si coincide su tiempo de llegada con el tiempo total
            if (planeado.get(i).tllega == tiempoTotal) {
                insertaL(planeado.get(i));
                expande();
                planeado.remove(i);
            }
        }
    }
    // Metodo que inicia Round Robin
    public void rr(){
        expande();
        procesa();
    }
    // Metodo que añade el proceso que quepa segun la memoria
    private void expande(){
        while (iniL != null) {
            // Si se excede la memoria no entra el proceso
            if (memoriaUsada+iniL.getData().size > memoriaTotal) {
                break;
            }
            insertaE(iniL.getData());
            memoriaUsada += iniL.getData().size;
            iniL = iniL.getSig();
        }
    }
    // Metodo que limpia la pantalla
    private void cls(){
        System.out.print("\033[H\033[2J");  
        System.out.flush();
    }
    // Metodo que se encarga de mandar a ejecutar cada proceso
    private void procesa(){
        // Si aun no llega ningun proceso se cuenta el tiempo
        while (iniE == null) {
            this.tiempoTotal++;
            expandePost();
        }
        // En cuanto se encuantra un proceso empieza a ejecutarse
        while (iniE != null) {
            boolean restado = false;
            mostrarColas();
            // Este ciclo realiza contabiliza el quantum
            for (int i = 0; i < quantum; i++) {
                // Si el tiempo de ejecucion no a llegado a 0 se reduce su tiempo en
                // una unidad y se aumenta el tiempo total, se revisa si ya es tiempo
                // de llegada de un proceso
                if (iniE.getData().teje != 0) {
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
                insertaE(iniE.getData());
            // En caso de ya no tener tiempo de ejecucion
            }else{
                // Se revisa si ya se libero la memoria del proceso
                if (restado == false) {
                    memoriaUsada -= iniE.getData().size;
                }
                // Se revisa si ya es tiempo de llegada de un proceso
                expandePost();
                expande();
            }
            // Se avanza al siguiente proceso
            iniE = iniE.getSig();
        }
    }
    // Metodo que muestra el estado de cada cola de datos, el tiempo
    // de total y el uso de memoria
    private void mostrarColas(){
        cls();
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
            System.out.print(colaVolteada.get(i)+" ");
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
            System.out.print(colaVolteada.get(i)+" ");
        }
        System.out.println("");
        System.out.println("Proceso en ejecucion");
        System.out.println(colaVolteada.get(0));
        System.out.println("Tiempo total: "+tiempoTotal);
        System.out.println("Memoria usada: "+memoriaUsada);
        System.out.println("Presiona cualquier tecla para continuar...");
        new java.util.Scanner(System.in).nextLine();
        cls();
    }
}
// Clase principal que inicializa todo
public class Main {
    public static void main(String []args) {
        colaProcesos uno = new colaProcesos(1000, 4);
        uno.inserta(new proceso("1", "powershell",0,12,0,2));
        uno.inserta(new proceso("2", "cmd",0,10,0,4));
        uno.inserta(new proceso("3", "Chrome",0,7,0,6));
        uno.inserta(new proceso("4", "vscode",0,4,0,8));
        uno.inserta(new proceso("5", "lolipop",0,2,0,10));
        // uno.inserta(new proceso("1", "powershell",0,6,0,1));
        // uno.inserta(new proceso("2", "cmd",0,18,0,4));
        // uno.inserta(new proceso("3", "Chrome",0,12,0,6));
        // uno.inserta(new proceso("4", "vscode",0,17,0,7));
        uno.rr();
    }
}