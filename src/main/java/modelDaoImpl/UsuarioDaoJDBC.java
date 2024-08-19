package modelDaoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import entites.Usuario;
import modelDao.UsuarioDao;

public class UsuarioDaoJDBC implements UsuarioDao{

    private Connection conn;

    public UsuarioDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Usuario obj) {
        PreparedStatement st = null;

        try{
            st = conn.prepareStatement("INSERT INTO usuario "
            + "(Nome, Email, Senha) "
            + "VALUES "
            + "(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getNome());
            st.setString(2, obj.getEmail());
            st.setString(3, obj.getSenha());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            }
            else{
                throw new DbException("Unexpected error! No rows affected!");
            }
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
        }
    }

    @Override
public void update(Usuario obj) {
    PreparedStatement st = null;
    try {
        st = conn.prepareStatement("UPDATE usuario "
        + "SET Nome = ?, Email = ?, Senha = ? "
        + "WHERE Id = ?");

        st.setString(1, obj.getNome());
        st.setString(2, obj.getEmail());
        st.setString(3, obj.getSenha());
        st.setInt(4, obj.getId());

        st.executeUpdate();
    } catch (SQLException e) {
        throw new DbException(e.getMessage());
    } finally {
        DB.closeStatement(st);
    }
}


    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM usuario WHERE Id = ?");

            st.setInt(1, id);

            st.executeUpdate();
        }
        catch (SQLException e){
            throw new DbException(e.getLocalizedMessage());
        }
        finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public Usuario findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM usuario WHERE Id = ?");
            st.setInt(1, id);

            rs = st.executeQuery();
            if (rs.next()){
                Usuario obj = new Usuario();
                obj.setId(rs.getInt("Id"));
                obj.setNome(rs.getString("Nome"));
                return obj;
            }
            return null;
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public List<Usuario> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("SELECT * FROM usuario ORDER BY NAME");

            rs = st.executeQuery();

            List<Usuario> list = new ArrayList<>();

            while(rs.next()){
                Usuario obj = new Usuario();
                obj.setId(rs.getInt("Id"));
                obj.setNome(rs.getString("Nome"));
                list.add(obj);

            }
            return list;
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());

        }
        finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
    
    public Usuario findByUsernameAndPassword(String username, String password) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM usuario WHERE Nome = ? AND Senha = ?");
            st.setString(1, username);
            st.setString(2, password);
    
            rs = st.executeQuery();
            if (rs.next()) {
                Usuario obj = new Usuario();
                obj.setId(rs.getInt("Id"));
                obj.setNome(rs.getString("Nome"));
                obj.setEmail(rs.getString("Email"));
                obj.setSenha(rs.getString("Senha"));
                return obj;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

}
