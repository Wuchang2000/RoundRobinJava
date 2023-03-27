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
    // Metodos
    public proceso getData(){
        return data;
    }

    public void redux(){
        this.data.teje -= 1;
    }

    public nodo getSig(){
        return sig;
    }

    public void setSig(nodo sig){
        this.sig = sig;
    }
}

class colaProcesos {

    private nodo iniL, finL, iniE, finE;
    private int contador, memoriaTotal, memoriaUsada, quantum;

    public colaProcesos(int memoriaTotal, int quantum){
        this.finL = null;
        this.iniL = null;
        this.memoriaTotal = memoriaTotal;
        this.quantum = quantum;
    }

    public void insertarL(proceso data){
        nodo nuevo = new nodo(data);
        if(iniL == null){
            iniL = nuevo;
            finL = nuevo;
        }else{
            finL.setSig(nuevo);
            finL = nuevo;
        }
    }
    
    private void insertarE(proceso data){
        nodo nuevo = new nodo(data);
        if(iniE == null){
            iniE = nuevo;
            finE = nuevo;
        }else{
            finE.setSig(nuevo);
            finE = nuevo;
        }
    }

    public void rr(){
        expande();
        procesa();
    }

    private void expande(){
        while (iniL != null) {
            if (memoriaUsada+iniL.getData().size > memoriaTotal) {
                break;
            }
            insertarE(iniL.getData());
            memoriaUsada += iniL.getData().size;
            iniL = iniL.getSig();
        }
    }

    private void cls(){
        System.out.print("\033[H\033[2J");  
        System.out.flush();
    }

    private void procesa(){
        while (iniE != null) {
            cls();
            mostrarColas();
            System.out.println("Press Any Key To Continue...");
            new java.util.Scanner(System.in).nextLine();
            cls();
            // System.out.println(iniE.getData().toString());
            for (int i = 0; i < quantum; i++) {
                if (iniE.getData().teje != 0) {
                    iniE.redux();
                    this.contador++;
                    // System.out.println(iniE.getData().id+" en ejecucion "+iniE.getData().teje+" msg");
                }else{
                    memoriaUsada -= iniE.getData().size;
                    expande();
                    break;
                }
            }
            if (iniE.getData().teje != 0) {
                insertarE(iniE.getData());
            }else{
                memoriaUsada -= iniE.getData().size;
                expande();
            }
            iniE = iniE.getSig();
        }
    }

    private void mostrarColas(){
        ArrayList<String> colaVolteada = new ArrayList<String>();
        nodo temp = iniL;
        while (temp != null) {
            colaVolteada.add(temp.getData().id);
            temp = temp.getSig();
        }
        System.out.println("Cola de procesos listos");
        for (int i = colaVolteada.size()-1; i >= 0; i--) {
            System.out.print(colaVolteada.get(i)+" ");
        }
        System.out.println("");
        colaVolteada.clear();
        temp = iniE;
        while (temp != null) {
            colaVolteada.add(temp.getData().id);
            temp = temp.getSig();
        }
        System.out.println("Cola de procesos listos para ejecucion");
        for (int i = colaVolteada.size()-1; i >= 0; i--) {
            System.out.print(colaVolteada.get(i)+" ");
        }
        System.out.println("");
    }
}

public class Main {
    public static void main(String []args) {
        colaProcesos uno = new colaProcesos(1000, 4);
        uno.insertarL(new proceso("1", "powershell",300,14,0,0));
        uno.insertarL(new proceso("2", "cmd",400,4,0,0));
        uno.insertarL(new proceso("3", "Chrome",100,20,0,0));
        uno.insertarL(new proceso("4", "vscode",500,20,0,0));
        uno.rr();
    }
}