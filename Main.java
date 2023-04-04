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