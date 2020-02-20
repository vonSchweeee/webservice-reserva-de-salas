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

public class DbAccessor {


    public DbAccessor() {

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
    

    public static void setAlocacaoInativa (AlocacaoSala alocacao) {
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
    

    public static List<Organizacao> getOrganizacoesByDominio(String dominio) {
        try {
            List<Organizacao> lista = EManager.getInstance().createNamedQuery("Organizacao.findDominioLike").setParameter("dominio", dominio).getResultList();
            clear();
            return lista;
        } catch (NoResultException e) {
            return null;
        }
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
            return aloca;
                    }
        catch (Exception e) {
            clear();
            return null;
        }
    }
    
    public static List<AlocacaoSala> getAlocacaoSalasByIdSalaAndData(int id, String dataRaw, String fimDiaRaw) {
        try {
            SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
            Date data=null;
            Date dataFim=null;
            try {
                data = formatoData.parse(dataRaw);
                dataFim = formatoData.parse(fimDiaRaw);
            } catch (ParseException ex) {
                Logger.getLogger(DbAccessor.class.getName()).log(Level.SEVERE, null, ex);
            }
            List<AlocacaoSala> l =  EManager.getInstance().createNamedQuery("AlocacaoSala.findBySalaIdDataHoraInicio").setParameter("idSala", id).setParameter("diaEscolhido", data, TemporalType.TIMESTAMP).setParameter("fimDiaEscolhido", dataFim, TemporalType.TIMESTAMP).getResultList();
            clear();
            return l;
        } catch (NoResultException e) {
            clear();
            return null;
        }
    }
    
        public static void novaAlocacao (AlocacaoSala alocacao, Usuario usuario) {
        try {
            EManager.getInstance().getTransaction().begin();
            EManager.getInstance().persist(alocacao);
            EManager.getInstance().merge(usuario);
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
                return "validado";
            }
            else {
                return "invalido";
            }
        }
        catch (Exception e) {
            clear();
            return null;
        } 
    }
        public static void clear() {
        EManager.getInstance().clear();
    }
//
//    public staticvoid modificaUsuario(Usuario usuario) {
//        synchronized () {
//            EManager.getInstance().getTransaction().begin();
//            EManager.getInstance().merge(usuario);
//            EManager.getInstance().getTransaction().commit();
//        }
//    }
//
//    public staticvoid excluirUsuario(Usuario usuario) {
//        synchronized () {
//            EManager.getInstance().getTransaction().begin();
//            EManager.getInstance().remove(usuario);
//            EManager.getInstance().getTransaction().commit();
//        }
//    }
}
