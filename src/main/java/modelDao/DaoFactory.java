package modelDao;

import db.DB;
import modelDaoImpl.ComentarioDaoJDBC;
import modelDaoImpl.UsuarioDaoJDBC;

public class DaoFactory {
    public static UsuarioDao createUsuarioDao(){
        return new UsuarioDaoJDBC(DB.gConnection());
    }

    public static ComentarioDao createComentarioDao(){
        return new ComentarioDaoJDBC(DB.gConnection());
    }
}
