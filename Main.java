import java.util.ArrayList;
import java.util.Scanner;

class proceso {
    // Atributos
    public String id, nombre;
    public Integer size, teje, prioridad, tllega;
    
    // Constructor
    public proceso(){
    }
    // Dar valor al id
    public void setId(String id) {
        this.id = id;
    }
    // Dar valor al nombre
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    // Dar valor a la prioridad
    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }
    // Dar valor al tamaño
    public void setSize(Integer size) {
        this.size = size;
    }
    // Dar valor al tiempo de ejecucion
    public void setTeje(Integer teje) {
        this.teje = teje;
    }  
    // Dar valor al tiempo de llegada
    public void setTllega(Integer tllega) {
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

class gantt {
    public String id;
    public int tInicia, tTermina, tllega;

    public gantt (proceso data, int tInicia, int tTermina, int tllega){
        this.id = data.id;
        this.tllega = tllega;
        this.tInicia = tInicia;
        if (tTermina > 4) {
            this.tTermina = this.tInicia+4;
        }else{
            this.tTermina = this.tInicia+tTermina;
        }
    }

    public String toString(){
        return this.id+" tini: "+this.tInicia+" tfin: "+this.tTermina+" tllega: "+this.tllega;
    }
}

class colaProcesos {

    private nodo iniL, finL, iniE, finE;
    private int tiempoTotal, memoriaTotal, memoriaUsada, quantum;
    private ArrayList<proceso> planeado = new ArrayList<proceso>();
    public ArrayList<gantt> diagrama = new ArrayList<gantt>();
    private ArrayList<String> ids = new ArrayList<String>();
    // Constructor
    public colaProcesos(int memoriaTotal, int quantum){
        this.finL = null;
        this.iniL = null;
        this.memoriaTotal = memoriaTotal;
        this.quantum = quantum;
    }
    // Metodo para insertar un nuevo proceso
    public void inserta(proceso data){
        if (ids.contains(data.id)) {
            System.out.println("Este id ya existe");
            return;
        }
        this.ids.add(data.id);
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
                mostrarColas();
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
            System.out.println("Subio el proceso "+iniL.getData().id+" y restan "+
            (memoriaTotal-memoriaUsada)+" unidades de memoria");
            iniL = iniL.getSig();
            mostrarColas();
        }
    }
    // Metodo que se encarga de mandar a ejecutar cada proceso
    private void procesa(){
        // Ciclo que mantiene la espera en caso de que no 
        // halla procesos listos para ejecucion pero si planeados
        while (planeado.isEmpty() != true) {
            // Si aun no llega ningun proceso se cuenta el tiempo
            while (iniE == null) {
                this.tiempoTotal++;
                expandePost();
            }
            // En cuanto se encuantre un proceso empieza a ejecutarse
            while (iniE != null) {
                boolean restado = false;
                mostrarColas();
                // Añade los procesos a una lista para poder realizar los calculos de tiempos
                diagrama.add(new gantt(iniE.getData(), tiempoTotal,
                iniE.getData().teje, iniE.getData().tllega));
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
        // Calculamos los tiempos de espera, respuesta y ejecucion
        calcula();
    }

    // Metodo para calular los tiempos de espera, respuesta y ejecucion
    private void calcula(){
        int indice = 0;
        float sumEsp = 0, sumResp = 0, sumEje = 0;
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
        System.out.println("Tiempo de respuesta: "+sumEje/ids.size());
    }

    // Metodo que muestra el estado de cada cola de datos, el tiempo
    // de total y el uso de memoria
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
        if (colaVolteada.isEmpty() != true) {
            System.out.println(colaVolteada.get(0));
        }else{
            System.out.println("");
        }
        System.out.println("Tiempo total: "+tiempoTotal);
        System.out.println("Memoria restante: "+(memoriaTotal-memoriaUsada)+"\n\n");
    }
}

// Clase principal que inicializa todo
public class Main {
    public static void main(String []args) {
        colaProcesos uno = new colaProcesos(1000, 4);
        Scanner input = new Scanner(System.in);
        System.out.print("Ingresa el numero de procesos a trabajar: ");
        int numeroP = input.nextInt();
        for (int i = 0; i < numeroP; i++) {
            proceso nuevoP = new proceso();
            System.out.print("Ingresa el id del proceso: ");
            nuevoP.setId(input.next());
            System.out.print("Ingresa el nombre del proceso: ");
            nuevoP.setNombre(input.next());
            System.out.print("Ingresa tamaño del proceso: ");
            nuevoP.setSize(input.nextInt());
            System.out.print("Ingresa el tiempo de ejecucion del proceso: ");
            nuevoP.setTeje(input.nextInt());
            System.out.print("Ingresa la prioridad del proceso: ");
            nuevoP.setPrioridad(input.nextInt());
            System.out.print("Ingresa el tiempo de llegada del proceso: ");
            nuevoP.setTllega(input.nextInt());
            uno.inserta(nuevoP);
        }
        System.out.println("\n");
        input.close();
        uno.rr();
    }
}