package com.devops.usermanager.user;

import com.devops.usermanager.common.EmailAlreadyUsedException;
import com.devops.usermanager.common.UserNotFoundException;
import com.devops.usermanager.user.dto.UserRequest;
import com.devops.usermanager.user.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void createNormaliseLEmailEtEnregistre() {
        when(userRepository.existsByEmail("ali@mail.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });

        UserResponse created = userService.create(new UserRequest(" Alami ", "Ali", " ALI@mail.com "));

        assertThat(created.id()).isEqualTo(1L);
        assertThat(created.nom()).isEqualTo("Alami");
        assertThat(created.email()).isEqualTo("ali@mail.com");
    }

    @Test
    void createRefuseUnEmailDejaUtilise() {
        when(userRepository.existsByEmail("ali@mail.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.create(new UserRequest("Alami", "Ali", "ali@mail.com")))
                .isInstanceOf(EmailAlreadyUsedException.class);
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateModifieLUtilisateur() {
        User existant = new User("Alami", "Ali", "ali@mail.com");
        existant.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existant));
        when(userRepository.existsByEmailAndIdNot("ali.alami@mail.com", 1L)).thenReturn(false);
        when(userRepository.save(existant)).thenReturn(existant);

        UserResponse updated = userService.update(1L, new UserRequest("Alami", "Ali", "ali.alami@mail.com"));

        assertThat(updated.email()).isEqualTo("ali.alami@mail.com");
    }

    @Test
    void deleteLeveUneExceptionSiLUtilisateurNExistePas() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> userService.delete(99L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("99");
    }
}
