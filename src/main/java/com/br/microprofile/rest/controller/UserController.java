package com.br.microprofile.rest.controller;

import com.br.microprofile.rest.model.User;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    private static final Map<Long, User> usuarios = new HashMap<>();
    private static final AtomicLong idCounter = new AtomicLong();

    @POST
    public User criarUsuario(final User usuario) {
        log.info("iniciando a criacao de um novo usuario: {}", usuario.getName());
        final var id = idCounter.incrementAndGet();
        final var user = User.builder()
                .id(id)
                .name(usuario.getName())
                .email(usuario.getEmail())
                .build();

        usuarios.put(id, user);
        log.info("usuario criado com sucesso: {}", user.getName());
        return usuario;
    }

    @GET
    public Collection<User> getAllUsers() {
        log.info("Buscando todos os usuarios cadastrados.");
        Collection<User> allUsers = usuarios.values();
        log.info("Total de usuarios encontrados: {}", allUsers.size());
        return allUsers;
    }

    @GET
    @Path("/{id}")
    public User getById(@PathParam("id") final Long id) {
        log.info("Buscando usuario pelo ID: {}", id);
        User user = usuarios.get(id);
        if (user != null) {
            log.info("usuario encontrado: {}", user.getName());
        } else {
            log.info("usuario com ID {} nao encontrado.", id);
        }
        return user;
    }

    @PUT
    @Path("/{id}")
    public User updateUser(@PathParam("id") final Long id, final User updateUser) {
        log.info("Atualizando informações do usuario com ID: {}", id);
        final var user = User.builder()
                .id(updateUser.getId())
                .email(updateUser.getEmail())
                .name(updateUser.getName())
                .build();

        usuarios.put(id, user);
        log.info("usuario com ID {} atualizado com sucesso.", id);
        return updateUser;
    }

    @DELETE
    @Path("/{id}")
    public void deleteUser(@PathParam("id") Long id) {
        log.info("Iniciando a remocao do usuario com ID: {}", id);
        log.info("Buscando usuario pelo ID: {}", id);
        final var user = usuarios.get(id);
        if (user != null) {
            usuarios.remove(id);
            log.info("usuario com ID {} removido com sucesso.", id);
        } else {
            log.info("usuario com ID {} nao encontrado.", id);
        }
    }

}