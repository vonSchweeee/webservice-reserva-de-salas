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

public class AlocacaoSalaAccessor {

    private final EntityManager manager;
    private final Object operationLock;

    public AlocacaoSalaAccessor(EntityManager manager, Object operationLock) {
        this.manager = manager;
        this.operationLock = operationLock;
    }

    public List<AlocacaoSala> getAllAlocacaoSalas() {
        return this.manager.createNamedQuery("AlocacaoSala.findAll").getResultList();
    }

    public void setAlocacaoInativa (AlocacaoSala alocacao) {
        synchronized (this.operationLock) {
            this.manager.getTransaction().begin();
            this.manager.merge(alocacao);
            this.manager.getTransaction().commit();
        }
    }
    
    public AlocacaoSala getAlocacaoSalaById (int id) {
        try {
            return (AlocacaoSala) this.manager.createNamedQuery("AlocacaoSala.findById").setParameter("id", id).getSingleResult();      
                    }
        catch (Exception e) {
            return null;
        }
    }
    
    public List<AlocacaoSala> getAlocacaoSalasByIdSalaAndData(int id, String dataRaw, String fimDiaRaw) {
        try {
            SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
            Date data=null;
            Date dataFim=null;
            try {
                data = formatoData.parse(dataRaw);
                dataFim = formatoData.parse(fimDiaRaw);
            } catch (ParseException ex) {
                Logger.getLogger(AlocacaoSalaAccessor.class.getName()).log(Level.SEVERE, null, ex);
            }
            return this.manager.createNamedQuery("AlocacaoSala.findBySalaIdDataHoraInicio").setParameter("idSala", id).setParameter("diaEscolhido", data, TemporalType.TIMESTAMP).setParameter("fimDiaEscolhido", dataFim, TemporalType.TIMESTAMP).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
    
        public void novaAlocacao (AlocacaoSala alocacao, Usuario usuario) {
        synchronized (this.operationLock) {
            this.manager.getTransaction().begin();
            this.manager.persist(alocacao);
            this.manager.merge(usuario);
            this.manager.getTransaction().commit();
        }
    }
    
    
    
    public String verificarConsistenciaAlocacao (Date dataHoraInicio, Date dataHoraFim, int idSala){
        int retornos = this.manager.createNamedQuery("AlocacaoSala.verificarConsistencia").setParameter("idSala", idSala).setParameter("dataHoraInicio", dataHoraInicio, TemporalType.TIMESTAMP).setParameter("dataHoraFim", dataHoraFim, TemporalType.TIMESTAMP).getResultList().size();
        if (retornos == 0) {
            return "validado";
        }
        else {
            return "invalido";
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
