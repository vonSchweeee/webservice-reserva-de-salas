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

    private final EntityManager manager;
    private final Object operationLock;

    public DbAccessor(EntityManager manager, Object operationLock) {
        this.manager = manager;
        this.operationLock = operationLock;
    }

    public List<Usuario> getAllUsuarios() {
        return this.manager.createNamedQuery("Usuario.findAll").getResultList();
    }

    public Usuario getUserById(int id) {
        try {
            return (Usuario) this.manager.createNamedQuery("Usuario.findById").setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Usuario getUserByEmail(String email) {
        try {
            return (Usuario) this.manager.createNamedQuery("Usuario.findByEmail").setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Usuario getCredencials(String email, String senha) {
        try {
            return (Usuario) this.manager.createNamedQuery("Usuario.findByEmailAndPassword").setParameter("email", email).setParameter("senha", senha).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    

    public void setAlocacaoInativa (AlocacaoSala alocacao) {
        synchronized (this.operationLock) {
            this.manager.getTransaction().begin();
            this.manager.merge(alocacao);
            this.manager.getTransaction().commit();
        }
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
        public void novoUsuario(Usuario usuario) {
        synchronized (this.operationLock) {
            this.manager.getTransaction().begin();
            this.manager.persist(usuario);
            this.manager.getTransaction().commit();
        }
    }

    public List<Organizacao> getOrganizacoesByDominio(String dominio) {
        try {
            return this.manager.createNamedQuery("Organizacao.findDominioLike").setParameter("dominio", dominio).getResultList();
        } catch (NoResultException e) {
            return null;
        }
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

    public List<AlocacaoSala> getAllAlocacaoSalas() {
        return this.manager.createNamedQuery("AlocacaoSala.findAll").getResultList();
    }
    
    public List<AlocacaoSala> getAlocacaoSalasBySalaId(int id) {
        try {
            return this.manager.createNamedQuery("AlocacaoSala.findBySalaId").setParameter("idSala", id).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public List<AlocacaoSala> getAlocacaoSalasByUsuarioId(int id) {
        try {
            return this.manager.createNamedQuery("AlocacaoSala.findByUsuarioId").setParameter("idSala", id).getResultList();
        } catch (NoResultException e) {
            return null;
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
                Logger.getLogger(DbAccessor.class.getName()).log(Level.SEVERE, null, ex);
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
