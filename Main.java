import java.util.Scanner;

/** 
 * Esta clase maneja la creacion de los procesos
 * @author Equipo 5
 * @version 04/04/2023
 * */

// Clase principal que inicializa todo
public class Main {
    public static void main(String []args) {
        RoundRobin uno = new RoundRobin(1000, 4);
        Scanner input = new Scanner(System.in);
        String id;
        boolean avanzar;
        System.out.print("Ingresa el numero de procesos a trabajar: ");
        int numeroP = input.nextInt();
        for (int i = 0; i < numeroP; i++) {
            System.out.print("\n\n NUEVA INSERCIÓN DE PROCESO \n\n");
            proceso nuevoP = new proceso();
            do{
                System.out.print("Ingresa el id del proceso: ");
                id = input.next();
                if (uno.searchIds(id)==true){
                    System.out.println("\nEste id ya existe, ingresa uno válido\n");
                }
                else{
                    nuevoP.setId(id);
                }
            } while(uno.searchIds(id)==true);
            System.out.print("Ingresa el nombre del proceso: ");
            nuevoP.setNombre(input.next());
            do{
                System.out.print("Ingresa el tamaño del proceso: ");
                avanzar=true;
                try{
                    nuevoP.setSize(input.nextInt());
                }catch(Exception e){
                    System.out.println("\nDebes ingresar un valor numerico entero y positivo\n");
                    avanzar=false;
                    input.nextLine();
                }
            }while(avanzar==false);
            do{
                System.out.print("Ingresa el tiempo de ejecucion del proceso en milisegundos: ");
                avanzar=true;
                try{  
                    nuevoP.setTeje(input.nextInt());
                }catch(Exception e){
                    System.out.println("\nDebes ingresar un valor numerico entero y positivo\n");
                    avanzar=false;
                    input.nextLine();
                }
            }while(avanzar==false);
            do{
                System.out.print("Ingresa la prioridad del proceso: ");
                avanzar=true;
                try{
                    nuevoP.setPrioridad(input.nextInt());
                }catch(Exception e){
                    System.out.println("\nDebes ingresar un valor numerico entero y positivo\n");
                    avanzar=false;
                    input.nextLine();
                }
            }while(avanzar==false);
            do{
                System.out.print("Ingresa el tiempo de llegada del proceso: ");
                avanzar=true;
                try{
                    nuevoP.setTllega(input.nextInt());
                }catch(Exception e){
                    System.out.println("\nDebes ingresar un valor numerico entero y positivo\n");
                    avanzar=false;
                    input.nextLine();
                }
            }while(avanzar==false);
            uno.inserta(nuevoP);
        }
        System.out.println("\n");
        input.close();
        uno.rr();
    }
}

