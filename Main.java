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
    private nodo ant;
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

    public nodo getAnt(){
        return ant;
    }

    public void setSig(nodo sig){
        this.sig = sig;
    }

    public void setAnt(nodo ant){
        this.ant = ant;
    }
}

class colaProcesos {

    private nodo ini, fin;
    public int contador;

    public colaProcesos(){
        this.fin = null;
        this.ini = null;
    }

    public void insertar(proceso data){
        nodo nuevo = new nodo(data);
        if(ini == null){
            ini = nuevo;
            // ini.setSig(null);
            // ini.setAnt(null);
            fin = nuevo;
        }else{
            fin.setSig(nuevo);
            // nuevo.setSig(null);
            // nuevo.setAnt(fin);
            fin = nuevo;
        }
    }

    public void procesa(){
        while (ini != null) {
            System.out.println(ini.getData().toString());
            for (int i = 0; i < 4; i++) {
                if (ini.getData().teje != 0) {
                    ini.redux();
                    this.contador++;
                    System.out.println(ini.getData().id+" en ejecucion "+ini.getData().teje+" msg");
                }else{
                    break;
                }
            }
            if (ini.getData().teje != 0) {
                insertar(ini.getData());
            }
            ini = ini.getSig();
        }
    }

    // public void extrae(String id) {
    //     nodo actual = ini;
    //     nodo anterior = null;
    //     boolean flag = false;
    //     do{
    //         if (actual.getData().id == id){
    //             flag = true;
    //             if(actual == ini){
    //                 ini = ini.getSig();
    //                 fin = ini.getAnt();
    //                 ini = fin.getSig();
    //             }else if(actual == fin){
    //                 fin = anterior;
    //                 ini = fin.getSig();
    //                 fin = ini.getAnt();
    //             }else{
    //                 anterior.setSig(actual.getSig());
    //                 anterior = actual.getSig().getAnt();
    //             }
    //         }
    //         anterior = actual;
    //         actual = actual.getSig();
    //     } while (actual != null && flag == false);
    // }
}

public class Main {
    public static void main(String []args) {
        colaProcesos uno = new colaProcesos();
        uno.insertar(new proceso("1", "powershell",0,14,0,0));
        uno.insertar(new proceso("2", "cmd",0,4,0,0));
        uno.insertar(new proceso("3", "Chrome",0,20,0,0));
        uno.procesa();
        System.out.println("El tiempo total fue: "+uno.contador);
    }
}