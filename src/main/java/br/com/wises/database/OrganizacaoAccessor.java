package br.com.wises.database;

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

    private final EntityManager manager;
    private final Object operationLock;

    public OrganizacaoAccessor(EntityManager manager, Object operationLock) {
        this.manager = manager;
        this.operationLock = operationLock;
    }
    
    public List<Organizacao> getAllOrganizacoes() {
        return this.manager.createNamedQuery("Organizacao.findAll").getResultList();
    }

    public Organizacao getOrganizacaoById(int id) {
        try {
            return (Organizacao) this.manager.createNamedQuery("Organizacao.findById").setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Organizacao getOrganizacaoByDominio(String dominio) {
        try {
            return (Organizacao) this.manager.createNamedQuery("Organizacao.findDominioLike").setParameter("dominio", dominio).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Organizacao> getOrganizacoesByDominio(String dominio) {
        try {
            return this.manager.createNamedQuery("Organizacao.findDominioLike").setParameter("dominio", dominio).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

//
//    public void modificaUsuario(Usuario usuario) {
//        synchronized (this.operationLock) {
//            this.manager.getTransaction().begin();
//            this.manager.merge(usuario);
//            this.manager.getTransaction().commit();
//        }
//    }
//
//    public void excluirUsuario(Usuario usuario) {
//        synchronized (this.operationLock) {
//            this.manager.getTransaction().begin();
//            this.manager.remove(usuario);
//            this.manager.getTransaction().commit();
//        }
//    }
}
