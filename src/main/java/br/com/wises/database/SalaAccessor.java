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

public class SalaAccessor {

    private final EntityManager manager;
    private final Object operationLock;

    public SalaAccessor(EntityManager manager, Object operationLock) {
        this.manager = manager;
        this.operationLock = operationLock;
    }

    public List<Sala> getAllSalas() {
        return this.manager.createNamedQuery("Sala.findAll").getResultList();
    }

    public List<Sala> getSalasByOrganizacaoId(int id) {
        try {
            return this.manager.createNamedQuery("Sala.findByOrganizacaoId").setParameter("idOrganizacao", id).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public Sala getSalaById(int id) {
        try {
            return (Sala) this.manager.createNamedQuery("Sala.findById").setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}