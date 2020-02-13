package br.com.wises.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EManager implements java.io.Serializable {

    // Para inicializar corertamente o Emanager, na hora de configurar a PU
    // colocar ?useTimezone=true&serverTimezone=UTC na conexão
    private static final Object emLock = new Object();
    private static EManager instance = null;

    private static EntityManager em = null;

    private static final Object operationLock = new Object();

    private final DbAccessor dbAccessor;
    private final AlocacaoSalaAccessor alocacaoSalaAccessor;
    private final SalaAccessor salaAccessor;
    private final OrganizacaoAccessor organizacaoAccessor;
    private final UsuarioAccessor usuarioAccessor;
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ReservaDeSalasPU");

    public EManager() {
        this.em = Persistence.createEntityManagerFactory("ReservaDeSalasPU").createEntityManager();
        this.dbAccessor = new DbAccessor(this.em, this.operationLock);
        this.alocacaoSalaAccessor = new AlocacaoSalaAccessor(this.em, this.operationLock);
        this.salaAccessor = new SalaAccessor(this.em, this.operationLock);
        this.organizacaoAccessor = new OrganizacaoAccessor(this.em, this.operationLock);
        this.usuarioAccessor = new UsuarioAccessor(this.em, this.operationLock);
    }

    public static EManager getInstance() {
        if (instance == null) {
            synchronized (emLock) {
                if (instance == null) {
                    instance = new EManager();
                }
            }
        }
        return instance;
    }

    public DbAccessor getDbAccessor() {
        return dbAccessor;
    }
    
    public AlocacaoSalaAccessor getAlocacaoSalaAccessor(){
        return alocacaoSalaAccessor;
    }

    public SalaAccessor getSalaAccessor(){
        return salaAccessor;
    }
    
    public OrganizacaoAccessor getOrganizacaoAccessor(){
        return organizacaoAccessor;
    }
    
    public UsuarioAccessor getUsuarioAccessor(){
        return usuarioAccessor;
    }
}
