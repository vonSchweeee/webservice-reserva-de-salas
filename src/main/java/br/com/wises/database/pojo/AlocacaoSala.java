/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wises.database.pojo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author jvito
 */
@Entity
@Table(name = "alocacao_sala")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AlocacaoSala.findAll", query = "SELECT a FROM AlocacaoSala a"),
    @NamedQuery(name = "AlocacaoSala.findById", query = "SELECT a FROM AlocacaoSala a WHERE a.id = :id"),
    @NamedQuery(name = "AlocacaoSala.findByDataHoraInicio", query = "SELECT a FROM AlocacaoSala a WHERE a.dataHoraInicio = :dataHoraInicio"),
    @NamedQuery(name = "AlocacaoSala.findByDataHoraFim", query = "SELECT a FROM AlocacaoSala a WHERE a.dataHoraFim = :dataHoraFim"),
    @NamedQuery(name = "AlocacaoSala.findByDescricao", query = "SELECT a FROM AlocacaoSala a WHERE a.descricao = :descricao"),
    @NamedQuery(name = "AlocacaoSala.findByAtivo", query = "SELECT a FROM AlocacaoSala a WHERE a.ativo = :ativo"),
    @NamedQuery(name = "AlocacaoSala.findByDataCriacao", query = "SELECT a FROM AlocacaoSala a WHERE a.dataCriacao = :dataCriacao"),
    @NamedQuery(name = "AlocacaoSala.findByDataAlteracao", query = "SELECT a FROM AlocacaoSala a WHERE a.dataAlteracao = :dataAlteracao"),
    @NamedQuery(name = "AlocacaoSala.findBySalaId", query = "SELECT a FROM AlocacaoSala a JOIN a.idSala s WHERE s.id = :idSala"),
    @NamedQuery(name = "AlocacaoSala.findByUsuarioId", query = "SELECT a FROM AlocacaoSala a JOIN a.idUsuario u WHERE u.id = :idUsuario"),
    @NamedQuery(name = "AlocacaoSala.findBySalaIdDataHoraInicio", query = "SELECT a FROM AlocacaoSala a Join a.idSala s WHERE s.id = :idSala AND a.dataHoraInicio >= :diaEscolhido AND a.dataHoraInicio < :fimDiaEscolhido AND a.ativo = true"),
    @NamedQuery(name = "AlocacaoSala.setAlocacaoInativa", query = "UPDATE AlocacaoSala a SET a.ativo = false WHERE a.id = :id"),
    @NamedQuery(name = "AlocacaoSala.verificarConsistencia", query = "SELECT a FROM AlocacaoSala a Join a.idSala s WHERE s.id = :idSala AND :dataHoraInicio >= a.dataHoraInicio AND :dataHoraFim <= a.dataHoraFim and a.ativo = true")
    })
public class AlocacaoSala implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "dataHoraInicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHoraInicio;
    @Column(name = "dataHoraFim")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHoraFim;
    @Size(max = 45)
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "ativo")
    private Boolean ativo;
    @Column(name = "dataCriacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    @Column(name = "dataAlteracao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAlteracao;
    @JoinColumn(name = "id_sala", referencedColumnName = "id")
    @ManyToOne
    private Sala idSala;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    @ManyToOne
    private Usuario idUsuario;
    
    
    public AlocacaoSala() {
    }

    public AlocacaoSala(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Sala getIdSala() {
        return idSala;
    }

    public void setIdSala(Sala idSala) {
        this.idSala = idSala;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(Date dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public Date getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(Date dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AlocacaoSala)) {
            return false;
        }
        AlocacaoSala other = (AlocacaoSala) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.wises.database.pojo.AlocacaoSala[ id=" + id + " ]";
    }
    
}
