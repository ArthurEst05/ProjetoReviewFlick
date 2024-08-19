package modelDao;

import java.util.List;

import entites.Comentario;

public interface ComentarioDao {
    void save(Comentario obj);
    void update(Comentario obj);
    void deleteById(Integer id);
    Comentario findById(Integer id);
    List<Comentario> findAll();
    List<Comentario> findAllByImdbID(String imdbID);
}