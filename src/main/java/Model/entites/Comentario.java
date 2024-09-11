package Model.entites;

import java.sql.Date;


public class Comentario {
    private int id;
    private String comentario;
    private Usuario usuario;
    private Date data;
    private String imdbID;
    private int curtidas;

    public Comentario(String comentario, Usuario usuario, Date data, String imdbID) {
        this.comentario = comentario;
        this.usuario = usuario;
        this.data = data;
        this.imdbID = imdbID;
    }

    
    public Comentario() {
    }
    public int getCurtidas() {
        return curtidas;
    }
    public void setCurtidas(int curtidas) {
        this.curtidas = curtidas;
    }
    public String getComentario() {
        return comentario;
    }
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public Date getData() {
        return data;
    }
    public void setData(Date data) {
        this.data = data;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getImdbID() {
        return imdbID;
    }
    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }
    @Override
    public String toString() {
        return usuario.getNome() + " (" + data + "): " + comentario;
    }
}
