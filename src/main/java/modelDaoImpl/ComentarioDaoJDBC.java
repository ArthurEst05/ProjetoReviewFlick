package modelDaoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DbException;
import entites.Comentario;
import entites.Usuario;
import modelDao.ComentarioDao;

public class ComentarioDaoJDBC implements ComentarioDao {
    private Connection conn;

    public ComentarioDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void save(Comentario obj) {
        String sql = "INSERT INTO comentario (comentario, usuario_id, data, imdbID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, obj.getComentario());
            st.setInt(2, obj.getUsuario().getId());
            st.setDate(3, obj.getData());
            st.setString(4, obj.getImdbID());  // Salvar o imdbID
    
            int rowsAffected = st.executeUpdate();
    
            if (rowsAffected > 0) {
                try (ResultSet rs = st.getGeneratedKeys()) {
                    if (rs.next()) {
                        obj.setId(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void update(Comentario obj) {
        String sql = "UPDATE comentario SET comentario = ?, usuario_id = ?, data = ? WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, obj.getComentario());
            st.setInt(2, obj.getUsuario().getId());
            st.setDate(3, obj.getData());
            st.setInt(4, obj.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM comentario WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public Comentario findById(Integer id) {
        String sql = "SELECT comentario.*, usuario.nome as usuario_nome FROM comentario INNER JOIN usuario ON comentario.usuario_id = usuario.id WHERE comentario.id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Comentario comentario = instantiateComentario(rs);
                    return comentario;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public List<Comentario> findAll() {
        String sql = "SELECT comentario.*, usuario.nome as usuario_nome FROM comentario INNER JOIN usuario ON comentario.usuario_id = usuario.id ORDER BY data";
        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            List<Comentario> list = new ArrayList<>();
            while (rs.next()) {
                Comentario comentario = instantiateComentario(rs);
                list.add(comentario);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    private Comentario instantiateComentario(ResultSet rs) throws SQLException {
        Comentario comentario = new Comentario();
        comentario.setId(rs.getInt("id"));
        comentario.setComentario(rs.getString("comentario"));
        comentario.setData(rs.getDate("data"));

        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("usuario_id"));
        usuario.setNome(rs.getString("usuario_nome"));
        comentario.setUsuario(usuario);

        return comentario;
    }

    public List<Comentario> findAllByImdbID(String imdbID) {
        String sql = "SELECT comentario.*, usuario.nome as usuario_nome FROM comentario INNER JOIN usuario ON comentario.usuario_id = usuario.id WHERE comentario.imdbID = ? ORDER BY data";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, imdbID);
            try (ResultSet rs = st.executeQuery()) {
                List<Comentario> list = new ArrayList<>();
                while (rs.next()) {
                    Comentario comentario = instantiateComentario(rs);
                    list.add(comentario);
                }
                return list;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }
    
}
