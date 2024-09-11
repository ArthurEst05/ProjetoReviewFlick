package Model.modelDao;

import Controller.db.DB;
import Model.modelDaoImpl.ComentarioDaoJDBC;
import Model.modelDaoImpl.UsuarioDaoJDBC;

public class DaoFactory {
    public static UsuarioDao createUsuarioDao(){
        return new UsuarioDaoJDBC(DB.gConnection());
    }

    public static ComentarioDao createComentarioDao(){
        return new ComentarioDaoJDBC(DB.gConnection());
    }
}
