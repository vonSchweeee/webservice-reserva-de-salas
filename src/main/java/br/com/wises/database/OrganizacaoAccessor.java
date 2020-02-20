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

public class OrganizacaoAccessor {


    public OrganizacaoAccessor(EntityManager manager, Object operationLock) {

    }
    
    public static List<Organizacao> getAllOrganizacoes() {
        List<Organizacao> lista = EManager.getInstance().createNamedQuery("Organizacao.findAll").getResultList();
        return lista;
    }

    public static Organizacao getOrganizacaoById(int id) {
        try {
            Organizacao o = (Organizacao) EManager.getInstance().createNamedQuery("Organizacao.findById").setParameter("id", id).getSingleResult();
            clear();
            return o;
        } catch (NoResultException e) {
            clear();
            return null;
        }
    }

    public static Organizacao getOrganizacaoByDominio(String dominio) {
        try {
            Organizacao o = (Organizacao) EManager.getInstance().createNamedQuery("Organizacao.findDominioLike").setParameter("dominio", dominio).getSingleResult();
            clear();
            return o;
        } catch (NoResultException e) {
            clear();
            return null;
        }
    }

      public static List<Organizacao> getOrganizacoesByDominio(String dominio) {
        try {
            List<Organizacao> lista = EManager.getInstance().createNamedQuery("Organizacao.findDominioLike").setParameter("dominio", dominio).getResultList();
            clear();
            return lista;
        } catch (NoResultException e) {
            return null;
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
