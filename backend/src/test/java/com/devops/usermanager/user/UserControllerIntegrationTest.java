package com.devops.usermanager.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Teste l'API complète (contrôleur + service + base H2), sans MySQL. */
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void viderLaBase() {
        userRepository.deleteAll();
    }

    @Test
    void creerPuisListerUnUtilisateur() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nom": "Alami", "prenom": "Ali", "email": "ali@mail.com"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("ali@mail.com"));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nom").value("Alami"));
    }

    @Test
    void refuserDesDonneesInvalides() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nom": "", "prenom": "Ali", "email": "pas-un-email"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.nom").exists())
                .andExpect(jsonPath("$.details.email").exists());
    }

    @Test
    void supprimerUnUtilisateurInexistantRenvoie404() throws Exception {
        mockMvc.perform(delete("/api/users/12345"))
                .andExpect(status().isNotFound());
    }
}
