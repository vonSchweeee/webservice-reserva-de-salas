package br.com.wises.database;

import static br.com.wises.database.DbAccessor.clear;
import br.com.wises.database.pojo.AlocacaoSala;
import br.com.wises.database.pojo.Organizacao;
import br.com.wises.database.pojo.Sala;
import br.com.wises.database.pojo.Usuario;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TemporalType;
import org.joda.time.DateTime;

public class UsuarioAccessor {

    public UsuarioAccessor(EntityManager manager, Object operationLock) {

    }

    public static List<Usuario> getAllUsuarios() {
        List<Usuario> lista =  EManager.getInstance().createNamedQuery("Usuario.findAll").getResultList();
        clear();
        return lista;
    }

    public static Usuario getUserById(int id) {
        try {
            Usuario u = (Usuario) EManager.getInstance().createNamedQuery("Usuario.findById").setParameter("id", id).getSingleResult();
            clear();
            return u;
        } catch (NoResultException e) {
            clear();
            return null;
        }
    }

    public static Usuario getUserByEmail(String email) {
        try {
            Usuario u = (Usuario) EManager.getInstance().createNamedQuery("Usuario.findByEmail").setParameter("email", email).getSingleResult();
            clear();
            return u;
        } catch (NoResultException e) {
            clear();
            return null;
        }
    }

    public static Usuario getCredencials(String email, String senha) {
        try {
            Usuario u = (Usuario) EManager.getInstance().createNamedQuery("Usuario.findByEmailAndPassword").setParameter("email", email).setParameter("senha", senha).getSingleResult();
            clear();
            return u;
        } catch (NoResultException e) {
            clear();
            return null;
        }
    }
            public static void novoUsuario(Usuario usuario) {
            try {
                EManager.getInstance().getTransaction().begin();
                EManager.getInstance().persist(usuario);
                EManager.getInstance().getTransaction().commit();
            } 
            catch (Exception e) {
                if (EManager.getInstance().getTransaction().isActive()) {
                    EManager.getInstance().getTransaction().rollback();
                    clear();
                }
            } 
        }

//
//    public static void modificaUsuario(Usuario usuario) {
//        synchronized () {
//            EManager.getInstance().getTransaction().begin();
//            EManager.getInstance().merge(usuario);
//            EManager.getInstance().getTransaction().commit();
//        }
//    }
//
//    public static void excluirUsuario(Usuario usuario) {
//        synchronized () {
//            EManager.getInstance().getTransaction().begin();
//            EManager.getInstance().remove(usuario);
//            EManager.getInstance().getTransaction().commit();
//        }
//    }
}
