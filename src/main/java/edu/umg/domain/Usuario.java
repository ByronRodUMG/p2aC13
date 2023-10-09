package edu.umg.domain;

public class Usuario {

    private int id_usuario;
    private String username;
    private String password;

    public Usuario() {
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString(){
        return "Usuario{" + "id_usuario=" + id_usuario + ", username=" + username + ", password encriptado=" + password + '}';
    }
}
