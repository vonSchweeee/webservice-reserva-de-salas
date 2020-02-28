package br.com.wises.services;

import br.com.wises.database.EManager;
import br.com.wises.database.OrganizacaoAccessor;
import br.com.wises.database.UsuarioAccessor;
import br.com.wises.database.pojo.Organizacao;
import br.com.wises.database.pojo.Status;
import br.com.wises.database.pojo.Usuario;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

@Path("usuario")
public class UsuarioService {

    @GET
    @Path("getusuario")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Usuario getUserJson(
            @HeaderParam("email") String email,
            @HeaderParam("authorization") String authorization) {
        if (authorization != null && authorization.equals("secret")) {
            Usuario user = UsuarioAccessor.getUserByEmail(email);
            if (user != null) {
                user.getIdOrganizacao().setUsuarioCollection(null);
                user.getIdOrganizacao().setSalaCollection(null);
                user.setSenha(null);
                return user;
            }
        } else {
            return null;
        }
        return null;
    }
    
    
    
    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response authentication(
            @HeaderParam("email") String email,
            @HeaderParam("password") String password,
            @HeaderParam("authorization") String authorization) {
        if (authorization != null && authorization.equals("secret")) {
            Usuario user = UsuarioAccessor.getCredencials(email, password);
            if (user != null) {
                user.getIdOrganizacao().setUsuarioCollection(null);
                user.getIdOrganizacao().setSalaCollection(null);
                user.getIdOrganizacao().setDataAlteracao(null);
                user.getIdOrganizacao().setDataCriacao(null);
                user.getIdOrganizacao().setAtivo(null);
                user.setSenha(null);
                return Response.ok(user).build();
            } else {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(new Status("Usuário não encontrado"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
        } else {
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity(new Status("Request inválido"))
                    .build();
        }
    }

    @GET
    @Path("usuariosorganizacao")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response getUsuariosOrganizacao(
            @HeaderParam("idOrganizacao") int idOrganizacao,
            @HeaderParam("authorization") String authorization) {
        if (authorization != null && authorization.equals("secret")) {
            List<Usuario> usuarios = UsuarioAccessor.getUsuariosByOrganizacaoId(idOrganizacao);
            if (usuarios != null) {
                for (int i=0; i<usuarios.size() ; i++) {
                    usuarios.get(i).getIdOrganizacao().setUsuarioCollection(null);
                    usuarios.get(i).getIdOrganizacao().setSalaCollection(null);
                    usuarios.get(i).getIdOrganizacao().setDataAlteracao(null);
                    usuarios.get(i).getIdOrganizacao().setDataCriacao(null);
                    usuarios.get(i).getIdOrganizacao().setAtivo(null);
                    usuarios.get(i).setSenha(null);
                }
                return Response.ok(usuarios).build();
            } else {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(new Status("Usuário não encontrado"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
        } else {
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity(new Status("Request inválido"))
                    .build();
        }
    }

//    @GET
//    @Path("loginOld")
//    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
//    public String authentication(
//            @HeaderParam("email") String email,
//            @HeaderParam("password") String password,
//            @HeaderParam("authorization") String authorization) {
//        if (authorization != null && authorization.equals("secret")) {
//            Usuario user = UsuarioAccessor.getUsuarioAccessor().getCredencials(email, password);
//            if (user != null) {
//                return "Login efetuado com sucesso!";
//            } else {
//                return "Credenciais Inválidas!";
//            }
//        } else {
//            return "Token Inválido";
//        }
//    }

    @POST
    @Path("cadastro")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response cadastrarUsuario(@HeaderParam("authorization") String authorization,
            @HeaderParam("novoUsuario") String novoUsuarioEncoded) {
        if (authorization != null && authorization.equals("secret")) {
            try {
                //String userEncodedOk = "ewogICAgImVtYWlsIjogInJvZHJpZ28ucXVpc2VuQHdpc2VzLmNvbS5iciIsCiAgICAibm9tZSI6ICJSb2RyaWdvIFF1aXNlbiAzIiwKICAgICJzZW5oYSI6ICIxMjMiCn0=";
                //String userEncodedNotOk = "ewogICAgImVtYWlsIjogInJvZHJpZ28ucXVpc2VuQHdpc2UuY29tLmJyIiwKICAgICJub21lIjogIlJvZHJpZ28gUXVpc2VuIDUiLAogICAgInNlbmhhIjogIjEyMyIKfQ==";
                String userDecoded = new String(Base64.getDecoder().decode(novoUsuarioEncoded.getBytes()), Charset.forName("UTF-8"));

                JSONObject userObj = new JSONObject(userDecoded);
                Usuario novoUsuario = new Usuario();
                String email, nome, senha;
                String dominio = null;
                int idOrganizacao = 0;

                if (userObj.has("email") && userObj.has("nome") && userObj.has("senha") && userObj.has("idOrganizacao")) {
                    email = userObj.getString("email");
                    nome = userObj.getString("nome");
                    senha = userObj.getString("senha");
                    idOrganizacao = userObj.getInt("idOrganizacao");

                    if (email.isEmpty() || nome.isEmpty() || senha.isEmpty() || idOrganizacao == 0) {
                        return Response
                                .status(Response.Status.BAD_REQUEST)
                                .entity(new Status("Erro ao criar conta, os dados enviados estão incompletos."))
                                .build();
                   } else if (email.contains("@")) {
                        dominio = email.split("@")[1];
                    }
                } else {
                        return Response
                                .status(Response.Status.BAD_REQUEST)
                                .entity(new Status("Erro ao criar conta, os dados enviados estão incompletos."))
                                .build();
                }

                if (UsuarioAccessor.getUserByEmail(email) != null) {
                        return Response
                                .status(Response.Status.BAD_REQUEST)
                                .entity(new Status("Erro ao criar conta, e-mail já cadastrado."))
                                .build();
                }

                Organizacao organizacao = new Organizacao();
                try {
                    organizacao = OrganizacaoAccessor.getOrganizacaoById(idOrganizacao);
                    if (organizacao == null) {
                        return Response
                                .status(Response.Status.BAD_REQUEST)
                                .entity(new Status("Erro ao cadastrar usuário, a organização informada não existe."))
                                .build();
                    }
                } catch (Exception e) {
                        return Response
                                .status(Response.Status.BAD_REQUEST)
                                .entity(new Status("Erro ao criar conta, os dados enviados estão incompletos."))
                                .build();
                }

                novoUsuario.setEmail(email);
                novoUsuario.setNome(nome);
                novoUsuario.setSenha(senha);
                novoUsuario.setIdOrganizacao(organizacao);

                UsuarioAccessor.novoUsuario(novoUsuario);

                return Response.status(Response.Status.CREATED)
                        .entity(new Status("Usuário criado com sucesso!"))
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
                        return Response
                                .status(Response.Status.INTERNAL_SERVER_ERROR)
                                .entity(new Status("Erro ao criar o usuário."))
                                .build();  
            }

        } else {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(new Status("Token inválido."))
                        .build();
        }
    }
}
