/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wises.services;

import br.com.wises.database.AlocacaoSalaAccessor;
import br.com.wises.database.EManager;
import br.com.wises.database.SalaAccessor;
import br.com.wises.database.UsuarioAccessor;
import br.com.wises.database.pojo.AlocacaoSala;
import br.com.wises.database.pojo.Organizacao;
import br.com.wises.database.pojo.Usuario;
import java.util.Base64;
import java.util.List;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import org.json.JSONObject;
import br.com.wises.database.pojo.Sala;
import br.com.wises.database.pojo.Status;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;

/**
 *
 * @author jvito
 */
@Path("alocacao")
public class AlocacaoSalaService {
    

    @GET
    @Path("alocacoes")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<AlocacaoSala> getAllAlocacaoSalas() {
        List<AlocacaoSala> lista = AlocacaoSalaAccessor.getAllAlocacaoSalas();
        for (int i = 0; i < lista.size(); i++) {
            lista.get(i).getIdSala().setAlocacaoSalaCollection(null);
        }
        return lista;
    }
    
    @GET
    @Path("getalocacoesbysaladata")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<AlocacaoSala> getAlocacaoSalasByIdSalaAndData(
            @HeaderParam("authorization") String authorization,
            @HeaderParam("idSala") int id,
            @HeaderParam("diaEscolhido") String data,
            @HeaderParam("fimDiaEscolhido") String fimDiaEscolhido){
        
        if (authorization != null && authorization.equals("secret")) {
            List<AlocacaoSala> lista = AlocacaoSalaAccessor.getAlocacaoSalasByIdSalaAndData(id, data, fimDiaEscolhido);
            for (int i = 0; i < lista.size(); i++) {
                lista.get(i).getIdSala().setAlocacaoSalaCollection(null);
                lista.get(i).getIdSala().getIdOrganizacao().setSalaCollection(null);
                lista.get(i).getIdSala().getIdOrganizacao().setUsuarioCollection(null);
            }
            return lista;
        }
        else {
            return null;
        }
    }
    
    @POST
    @Path("reservar")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response createAlocacao(
            @HeaderParam("novaAlocacao") String novaAlocacaoEncoded,
            @HeaderParam("authorization") String authorization) {
        if (authorization != null && authorization.equals("secret")) {
            String alocacaoDecoded = new String(Base64.getDecoder().decode(novaAlocacaoEncoded.getBytes()), Charset.forName("UTF-8"));
            JSONObject userObj = new JSONObject(alocacaoDecoded);
            AlocacaoSala alocacao = new AlocacaoSala();
            int idSala;
            int idUsuario;
            String descricao = null;
            Date dataHoraInicio = null, dataHoraFim = null;
            if (userObj.has("idSala") && userObj.has("idUsuario") && userObj.has("descricao") && userObj.has("dataHoraInicio") && userObj.has("dataHoraFim")) {
                SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                idSala = userObj.getInt("idSala");
                idUsuario = userObj.getInt("idUsuario");
                descricao = userObj.getString("descricao");
                try{
                dataHoraInicio = formatoData.parse(userObj.getString("dataHoraInicio"));
                dataHoraFim = formatoData.parse(userObj.getString("dataHoraFim"));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                Sala sala = new Sala();
                try {
                    sala = SalaAccessor.getSalaById(idSala);
                    if (sala == null) {
                        return Response
                                .status(Response.Status.BAD_REQUEST)
                                .entity(new Status("Erro ao realizar a alocação, a sala informada não existe."))
                                .build();
                    }
                    String validade = AlocacaoSalaAccessor.verificarConsistenciaAlocacao(dataHoraInicio, dataHoraFim, idSala);
                    if (! validade.equals("validado"))
                    {
                        return Response
                                .status(Response.Status.BAD_REQUEST)
                                .entity(new Status("Alocação conflita em horário com outra alocação"))
                                .build();
                    }
                } catch (Exception e) {
                        return Response
                                .status(Response.Status.BAD_REQUEST)
                                .entity(new Status("Erro ao criar a alocação."))
                                .build();
                }
                
                alocacao.setIdSala(sala);
                alocacao.setIdUsuario(idUsuario);
                alocacao.setDescricao(descricao);
                alocacao.setDataHoraInicio(dataHoraInicio);
                alocacao.setDataHoraFim(dataHoraFim);
                Date date = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(c.HOUR, -3);
                date = c.getTime();
                alocacao.setDataCriacao(date);
                alocacao.setDataAlteracao(date);
                alocacao.setAtivo(true);


                AlocacaoSalaAccessor.novaAlocacao(alocacao);
                        return Response
                                .status(Response.Status.CREATED)
                                .entity(new Status("Alocação conflita em horário com outra alocação"))
                                .build();
            } else {
                        return Response
                                .status(Response.Status.BAD_REQUEST)
                                .entity(new Status("Dados incorretos."))
                                .build();
            }
        } else {
                        return Response
                                .status(Response.Status.FORBIDDEN)
                                .entity(new Status("Token inválido."))
                                .build();
        }
    }
    
    
    @DELETE
    @Path("excluir")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String excluirAlocacao (
        @HeaderParam("id") int id,
        @HeaderParam("authorization") String authorization) {
        if (authorization != null && authorization.equals("secret")){
            try {
                AlocacaoSala alocacao = new AlocacaoSala();
                alocacao = AlocacaoSalaAccessor.getAlocacaoSalaById(id);
                alocacao.setAtivo(false);
                AlocacaoSalaAccessor.alterarAlocacao(alocacao);
                return "Alocação desativada com sucesso.";
            }
            catch (Exception e) {
                e.printStackTrace();
                return "Erro ao desativar alocação.";
            }
        }
        else{
            return "Token inválido.";
        }    
    }
    @PUT
    @Path("alterar")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response alterarAlocacao (
    @HeaderParam("alocacaoAlterada") String alocacaoAlterada, 
    @HeaderParam("authorization") String authorization){
            if(authorization != null && authorization.equals("secret")){
                try{
                    SimpleDateFormat formatoDataHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    JSONObject alocacaoJson = new JSONObject(new String(Base64.getDecoder().decode(alocacaoAlterada.getBytes()), Charset.forName("UTF-8")));
                    AlocacaoSala alocacao = new AlocacaoSala();
                    alocacao.setId(alocacaoJson.getInt("id"));
                    alocacao.setAtivo(Boolean.TRUE);
                    alocacao.setDataHoraInicio(formatoDataHora.parse(alocacaoJson.getString("dataHoraInicio")));
                    alocacao.setDataHoraFim(formatoDataHora.parse(alocacaoJson.getString("dataHoraFim")));
                    alocacao.setDescricao(alocacaoJson.getString("descricao"));
                    Sala sala = SalaAccessor.getSalaById(alocacaoJson.getInt("idSala"));
                    alocacao.setIdSala(sala);
                    int idUsuario = alocacaoJson.getInt("idUsuario");
                    alocacao.setIdUsuario(idUsuario);
                    Calendar c = Calendar.getInstance();
                    Date dataAgora = c.getTime();
                    alocacao.setDataCriacao(dataAgora);
                    alocacao.setDataAlteracao(dataAgora);
                    
                    String validade = AlocacaoSalaAccessor.verificarConsistenciaAlocacao(alocacao.getDataHoraInicio(), alocacao.getDataHoraFim(), alocacao.getId());
                    if (! validade.equals("validado"))
                    {
                        return Response
                                .status(Response.Status.BAD_REQUEST)
                                .entity(new Status("Alocação conflita em horário com outra alocação"))
                                .build();
                    }
                    
                    AlocacaoSalaAccessor.alterarAlocacao(alocacao);
                         return Response
                                .status(Response.Status.CREATED)
                                .entity(new Status("Alocacação alterada com sucesso!"))
                                .build();
                }
                catch(Exception e){
                               return Response
                                .status(Response.Status.INTERNAL_SERVER_ERROR)
                                .entity(new Status("Erro no servidor"))
                                .build();
                }
            }
            else {
                return Response
                                .status(Response.Status.FORBIDDEN)
                                .entity(new Status("Token inválido."))
                                .build();
            }
        }
}

