package com.briamcarrasco.arriendomaquinaria.service;

import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import com.briamcarrasco.arriendomaquinaria.model.Category;
import com.briamcarrasco.arriendomaquinaria.repository.MachineryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MachineryServiceImplTest {

    @Mock
    private MachineryRepository machineryRepository;

    @InjectMocks
    private MachineryServiceImpl service;

    @Test
    void createMachinery_savesAndReturnsMachinery() {
        Machinery m = new Machinery();
        when(machineryRepository.save(m)).thenReturn(m);

        Machinery result = service.createMachinery(m);

        assertSame(m, result);
        verify(machineryRepository).save(m);
    }

    @Test
    void findById_returnsOptionalFromRepository() {
        Machinery m = new Machinery();
        when(machineryRepository.findById(1L)).thenReturn(Optional.of(m));

        Optional<Machinery> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertSame(m, result.get());
        verify(machineryRepository).findById(1L);
    }

    @Test
    void findById_returnsEmptyOptional() {
        when(machineryRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Machinery> result = service.findById(2L);

        assertFalse(result.isPresent());
        verify(machineryRepository).findById(2L);
    }

    @Test
    void updateMachinery_whenExists_updatesAndReturns() {
        Machinery old = new Machinery();
        old.setNameMachinery("Old");
        old.setStatus("Disponible");
        old.setPricePerDay(BigDecimal.valueOf(1000));
        old.setCategory(new Category());

        Machinery updated = new Machinery();
        updated.setNameMachinery("New");
        updated.setStatus("Arrendada");
        updated.setPricePerDay(BigDecimal.valueOf(2000));
        Category cat = new Category();
        updated.setCategory(cat);

        when(machineryRepository.findById(5L)).thenReturn(Optional.of(old));
        when(machineryRepository.save(any(Machinery.class))).thenAnswer(inv -> inv.getArgument(0));

        Machinery result = service.updateMachinery(5L, updated);

        assertEquals("New", result.getNameMachinery());
        assertEquals("Arrendada", result.getStatus());
        assertEquals(BigDecimal.valueOf(2000), result.getPricePerDay());
        assertSame(cat, result.getCategory());
        verify(machineryRepository).findById(5L);
        verify(machineryRepository).save(old);
    }

    @Test
    void updateMachinery_whenNotExists_throwsException() {
        when(machineryRepository.findById(99L)).thenReturn(Optional.empty());

        Machinery updated = new Machinery();
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.updateMachinery(99L, updated));
        assertTrue(ex.getMessage().contains("Machinery not found"));
        verify(machineryRepository).findById(99L);
    }

    @Test
    void deleteMachinery_callsRepositoryDelete() {
        service.deleteMachinery(7L);
        verify(machineryRepository).deleteById(7L);
    }

    @Test
    void findAll_returnsListFromRepository() {
        Machinery m1 = new Machinery();
        Machinery m2 = new Machinery();
        when(machineryRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        List<Machinery> result = service.findAll();

        assertEquals(2, result.size());
        assertSame(m1, result.get(0));
        assertSame(m2, result.get(1));
        verify(machineryRepository).findAll();
    }

    @Test
    void findAll_returnsEmptyList() {
        when(machineryRepository.findAll()).thenReturn(Collections.emptyList());

        List<Machinery> result = service.findAll();

        assertTrue(result.isEmpty());
        verify(machineryRepository).findAll();
    }

    @Test
    void findByNameMachinery_returnsListFromRepository() {
        Machinery m = new Machinery();
        when(machineryRepository.findByNameMachineryContainingIgnoreCase("excavadora"))
                .thenReturn(Collections.singletonList(m));

        List<Machinery> result = service.findByNameMachinery("excavadora");

        assertEquals(1, result.size());
        assertSame(m, result.get(0));
        verify(machineryRepository).findByNameMachineryContainingIgnoreCase("excavadora");
    }

    @Test
    void findByCategory_returnsListFromRepository() {
        Machinery m = new Machinery();
        when(machineryRepository.findByCategory_NameIgnoreCase("industrial"))
                .thenReturn(Collections.singletonList(m));

        List<Machinery> result = service.findByCategory("industrial");

        assertEquals(1, result.size());
        assertSame(m, result.get(0));
        verify(machineryRepository).findByCategory_NameIgnoreCase("industrial");
    }
}