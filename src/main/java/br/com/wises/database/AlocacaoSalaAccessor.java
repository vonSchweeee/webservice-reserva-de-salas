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

public class AlocacaoSalaAccessor {


    public AlocacaoSalaAccessor() {

    }
      public static void alterarAlocacao (AlocacaoSala alocacao) {
        try {
            EManager.getInstance().getTransaction().begin();
            EManager.getInstance().merge(alocacao);
            EManager.getInstance().getTransaction().commit();
            clear();
        }
        catch (Exception e) {
            if (EManager.getInstance().getTransaction().isActive()) {
                EManager.getInstance().getTransaction().rollback();
                clear();
            }
        }
      }
   public static List<AlocacaoSala> getAllAlocacaoSalas() {
        List<AlocacaoSala> lista = EManager.getInstance().createNamedQuery("AlocacaoSala.findAll").getResultList();
        clear();
        return lista;
    }
    
    public static List<AlocacaoSala> getAlocacaoSalasBySalaId(int id) {
        try {
            List<AlocacaoSala> lista = EManager.getInstance().createNamedQuery("AlocacaoSala.findBySalaId").setParameter("idSala", id).getResultList();
            clear();
            return lista;
        } catch (NoResultException e) {
            clear();
            return null;
        }
    }
    
    public static List<AlocacaoSala> getAlocacaoSalasByUsuarioId(int id) {
        try {
            List<AlocacaoSala> lista = EManager.getInstance().createNamedQuery("AlocacaoSala.findByUsuarioId").setParameter("idSala", id).getResultList(); 
            clear();
            return lista;
        } catch (NoResultException e) {
            clear();
            return null;
        }
    }
    public static AlocacaoSala getAlocacaoSalaById (int id) {
        try {
            AlocacaoSala aloca = (AlocacaoSala) EManager.getInstance().createNamedQuery("AlocacaoSala.findById").setParameter("id", id).getSingleResult();      
            clear();
            return aloca;
                    }
        catch (Exception e) {
            clear();
            return null;
        }
    }
    
    public static List<AlocacaoSala> getAlocacaoSalasByIdSalaAndData(int id, String dataRaw, String fimDiaRaw) {
        try {
            SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
            Date data=null;
            Date dataFim=null;
            try {
                data = formatoData.parse(dataRaw + " 00:00:00");
                dataFim = formatoData.parse(fimDiaRaw + " 00:00:00");
            } catch (ParseException ex) {
                Logger.getLogger(DbAccessor.class.getName()).log(Level.SEVERE, null, ex);
            }
            List<AlocacaoSala> l =  EManager.getInstance().createNamedQuery("AlocacaoSala.findBySalaIdDataHoraInicio").setParameter("idSala", id).setParameter("diaEscolhido", data, TemporalType.TIMESTAMP).setParameter("fimDiaEscolhido", dataFim, TemporalType.TIMESTAMP).getResultList();
            clear();
            return l;
        } catch (Exception e) {
            clear();
            return null;
        }
    }
    
        public static void novaAlocacao (AlocacaoSala alocacao) {
        try {
            EManager.getInstance().getTransaction().begin();
            EManager.getInstance().persist(alocacao);
            EManager.getInstance().getTransaction().commit();
            clear();
        }
        catch (Exception e) {
                if (EManager.getInstance().getTransaction().isActive()) {
                    EManager.getInstance().getTransaction().rollback();
                    clear();
                }
        } 
    }
    
    
    
    public static String verificarConsistenciaAlocacao (Date dataHoraInicio, Date dataHoraFim, int idSala){
        try {
            int retornos = EManager.getInstance().createNamedQuery("AlocacaoSala.verificarConsistencia").setParameter("idSala", idSala).setParameter("dataHoraInicio", dataHoraInicio, TemporalType.TIMESTAMP).setParameter("dataHoraFim", dataHoraFim, TemporalType.TIMESTAMP).getResultList().size();
            if (retornos == 0) {
                clear();
                return "validado";
            }
            else {
                clear();
                return "invalido";
            }
            
        }
        catch (Exception e) {
            clear();
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
