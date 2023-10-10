package edu.umg.test;

import edu.umg.datos.Conexion;
import edu.umg.datos.PersonaDAO;
import edu.umg.domain.Persona;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class ManejoPersonas {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Connection conexion = null;
        try {
            conexion = Conexion.getConnection();
            if (conexion.getAutoCommit()) {
                conexion.setAutoCommit(false);
            }

            PersonaDAO personaDAO = new PersonaDAO(conexion);

            int opcion;
            do {
                System.out.println("Menú de opciones:");
                System.out.println("1. Listar Personas");
                System.out.println("2. Agregar Persona");
                System.out.println("3. Actualizar Persona");
                System.out.println("4. Eliminar Persona");
                System.out.println("5. Salir");
                System.out.print("Seleccione una opción: ");
                opcion = sc.nextInt();
                sc.nextLine(); // Limpiar el salto de línea

                switch (opcion) {
                    case 1:
                        List<Persona> personas = personaDAO.listarPersonas();
                        for (Persona persona : personas) {
                            System.out.println(persona);
                        }
                        break;
                    case 2:
                        Persona nuevaPersona = new Persona();
                        System.out.print("Ingrese el nombre: ");
                        nuevaPersona.setNombre(sc.nextLine());
                        System.out.print("Ingrese el apellido: ");
                        nuevaPersona.setApellido(sc.nextLine());
                        System.out.print("Ingrese el email: ");
                        nuevaPersona.setEmail(sc.nextLine());
                        System.out.print("Ingrese el teléfono: ");
                        nuevaPersona.setTelefono(sc.nextLine());

                        personaDAO.agregarPersona(nuevaPersona);
                        conexion.commit();
                        System.out.println("Se ha agregado la persona exitosamente.");
                        break;
                    case 3:
                        System.out.print("Ingrese el ID de la persona a actualizar: ");
                        int idActualizar = sc.nextInt();
                        sc.nextLine(); // Limpiar el salto de línea
                        Persona personaActualizar = personaDAO.listarPersonas().stream()
                                .filter(p -> p.getId_persona() == idActualizar)
                                .findFirst()
                                .orElse(null);

                        if (personaActualizar != null) {
                            System.out.print("Ingrese el nuevo nombre: ");
                            personaActualizar.setNombre(sc.nextLine());
                            System.out.print("Ingrese el nuevo apellido: ");
                            personaActualizar.setApellido(sc.nextLine());
                            System.out.print("Ingrese el nuevo email: ");
                            personaActualizar.setEmail(sc.nextLine());
                            System.out.print("Ingrese el nuevo teléfono: ");
                            personaActualizar.setTelefono(sc.nextLine());

                            personaDAO.actualizarPersona(personaActualizar);
                            conexion.commit();
                            System.out.println("Se ha actualizado la persona exitosamente.");
                        } else {
                            System.out.println("La persona con el ID especificado no existe.");
                        }
                        break;
                    case 4:
                        System.out.print("Ingrese el ID de la persona a eliminar: ");
                        int idEliminar = sc.nextInt();
                        sc.nextLine(); // Limpiar el salto de línea

                        personaDAO.eliminarPersona(idEliminar);
                        conexion.commit();
                        System.out.println("Se ha eliminado la persona exitosamente.");
                        break;
                    case 5:
                        break;
                    default:
                        System.out.println("Opción no válida.");
                        break;
                }
            } while (opcion != 5);
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.out.println("Entramos al rollback");
            try {
                if (conexion != null) {
                    conexion.rollback();
                }
            } catch (SQLException ex1) {
                ex1.printStackTrace(System.out);
            }
        } finally {
            Conexion.close(conexion);
        }
    }
}
