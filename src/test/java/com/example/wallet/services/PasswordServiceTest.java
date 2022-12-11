package com.example.wallet.services;


import com.example.wallet.readmodel.readonly.PasswordProjection;
import com.example.wallet.readmodel.readonly.User;
import com.example.wallet.repositories.PasswordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordServiceTest {

    @Mock
    private PasswordService passwordService;

    @Mock
    private PasswordRepository passwordRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void shouldReturnAllPasswords() throws Exception {
        //GIVEN
        List<PasswordProjection> passwordList = List.of(new PasswordProjection(1, "passwd1"),
                new PasswordProjection(1, "passwd2"));

        //WHEN
        lenient().when(passwordService.getAllPasswords(1, false))
                        .thenReturn(passwordList);

        assertEquals(2, passwordList.size());
        verify(passwordService,never()).getAllPasswords(1, true);

    }

    @Test
    void shouldReturnNoPasswords() throws Exception {
        //Given
        List<PasswordProjection> passwordProjections = new ArrayList<>();

        //WHEN
        lenient().when(passwordService.getAllPasswords(1,false)).thenReturn(passwordProjections);

        //THEN
        assertEquals(0, 0);
        verify(passwordRepository,times(0)).findAllPasswordsByUserId(1);
        verifyNoInteractions(passwordService);
    }

    @Test
    void shouldUpdatePasswords() throws Exception {
        //GIVEN
        final User user= mock(User.class);
        final List<PasswordProjection> passwordProjections = List.of(new PasswordProjection(1, "passwd1"),
                new PasswordProjection(1, "passwd2"));

        //WHEN
        lenient().when(passwordService.updatePasswords(user,passwordProjections)).thenReturn(HttpStatus.OK);
        assertEquals(HttpStatus.OK, HttpStatus.OK);

    }

}
