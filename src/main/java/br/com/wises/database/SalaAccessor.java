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

public class SalaAccessor {



    public SalaAccessor() {

    }

   public static List<Sala> getAllSalas() {
        List<Sala> lista = EManager.getInstance().createNamedQuery("Sala.findAll").getResultList();
        clear();
        return lista;
    }

    public static List<Sala> getSalasByOrganizacaoId(int id) {
        try {
            List<Sala> lista = EManager.getInstance().createNamedQuery("Sala.findByOrganizacaoId").setParameter("idOrganizacao", id).getResultList();
            clear();
            return lista;
        } catch (NoResultException e) {
            clear();
            return null;
        }
    }
    
    public static Sala getSalaById(int id) {
        try {
            Sala s = (Sala) EManager.getInstance().createNamedQuery("Sala.findById").setParameter("id", id).getSingleResult();
            clear();
            return s;
        } catch (NoResultException e) {
            clear();
            return null;
        }
    }
}