package edu.umg;

import edu.umg.datos.Conexion;
import edu.umg.datos.PersonaDAO;
import edu.umg.datos.UsuarioDAO;
import edu.umg.domain.Persona;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Connection conexion = null;

        try {
            conexion = Conexion.getConnection();
            if (conexion.getAutoCommit()) {
                conexion.setAutoCommit(false);
            }

            UsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
            PersonaDAO personaDAO = new PersonaDAO(conexion);

            System.out.println("Menú de opciones:");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Crear nuevo usuario");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = sc.nextInt();
            sc.nextLine(); // Limpiar el salto de línea

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el nombre de usuario: (Manuel)");
                    String nombreUsuario = sc.nextLine();
                    System.out.print("Ingrese la contraseña: (2003)");
                    String contrasena = sc.nextLine();

                    boolean usuarioValido = usuarioDAO.validarUsuario(conexion, nombreUsuario, contrasena);

                    if (usuarioValido) {
                        System.out.println("Inicio de sesión exitoso.");
                        int opcion2;
                        do {
                            System.out.println("Menú de opciones:");
                            System.out.println("1. Listar Personas");
                            System.out.println("2. Agregar Persona");
                            System.out.println("3. Salir");
                            System.out.print("Seleccione una opción: ");
                            opcion2 = sc.nextInt();
                            sc.nextLine(); // Limpiar el salto de línea

                            switch (opcion2) {
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
                                    break;
                                default:
                                    System.out.println("Opción no válida.");
                                    break;
                            }
                        } while (opcion2 != 3);
                    } else {
                        System.out.println("Inicio de sesión fallido. Nombre de usuario o contraseña incorrectos.");
                    }
                    break;
                case 2:
                    System.out.print("Ingrese el nombre de usuario: ");
                    String nuevoNombreUsuario = sc.nextLine();
                    System.out.print("Ingrese la contraseña: ");
                    String nuevaContrasena = sc.nextLine();

                    boolean usuarioCreado = usuarioDAO.insertUsuario(conexion, nuevoNombreUsuario, nuevaContrasena);

                    if (usuarioCreado) {
                        System.out.println("Usuario creado exitosamente.");
                    } else {
                        System.out.println("Error al crear el usuario.");
                    }
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Opción no válida.");
                    break;
            }
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
