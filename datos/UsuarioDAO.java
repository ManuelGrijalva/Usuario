package edu.umg.datos;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    private static final String SQL_INSERT_USUARIO = "INSERT INTO usuario(username, password) VALUES (?, ?)";
    private static final String SQL_SELECT_USUARIO = "SELECT id_usuario, username, password FROM usuario WHERE username = ? AND password = ?";

    public UsuarioDAO(Connection conexion) {

    }


    public boolean insertUsuario(Connection connection, String username, String password) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT_USUARIO)) {
            statement.setString(1, username);
            statement.setString(2, encriptarContrasena(password));
            int rows = statement.executeUpdate();
            return rows > 0;
        }
    }


    public boolean validarUsuario(Connection connection, String username, String password) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_USUARIO)) {
            statement.setString(1, username);
            statement.setString(2, encriptarContrasena(password));
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Devuelve true si se encontró un usuario con las credenciales proporcionadas.
            }
        }
    }


    private String encriptarContrasena(String contrasena) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(contrasena.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return contrasena; // Devolver la contraseña original en caso de error
        }
    }



}
