package com.briamcarrasco.arriendomaquinaria.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.briamcarrasco.arriendomaquinaria.model.Status;
import com.briamcarrasco.arriendomaquinaria.repository.StatusRepository;

@ExtendWith(MockitoExtension.class)
class StatusServiceImplTest {

    @Mock
    private StatusRepository statusRepository;

    @InjectMocks
    private StatusServiceImpl service;

    @Test
    void findAll_returnsRepositoryList() {
        Status s1 = new Status();
        Status s2 = new Status();
        List<Status> list = Arrays.asList(s1, s2);
        when(statusRepository.findAll()).thenReturn(list);

        List<Status> res = service.findAll();

        assertSame(list, res);
        verify(statusRepository).findAll();
    }
}