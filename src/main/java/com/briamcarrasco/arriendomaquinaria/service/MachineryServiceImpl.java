package com.briamcarrasco.arriendomaquinaria.service;

import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import com.briamcarrasco.arriendomaquinaria.repository.MachineryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para la gestión de maquinaria en el sistema de
 * arriendo.
 * Proporciona métodos para crear, buscar, actualizar, eliminar y listar
 * maquinarias.
 */
@Service
public class MachineryServiceImpl implements MachineryService {

    
    private final MachineryRepository machineryRepository;

    public MachineryServiceImpl(MachineryRepository machineryRepository) {
        this.machineryRepository = machineryRepository;
    }

    /**
     * Crea una nueva maquinaria en el sistema.
     *
     * @param machinery objeto Machinery a crear
     * @return la maquinaria creada
     */
    @Override
    public Machinery createMachinery(Machinery machinery) {
        return machineryRepository.save(machinery);
    }

    /**
     * Busca una maquinaria por su identificador.
     *
     * @param id identificador de la maquinaria
     * @return un Optional con la maquinaria encontrada o vacío si no existe
     */
    @Override
    public Optional<Machinery> findById(Long id) {
        return machineryRepository.findById(id);
    }

    /**
     * Actualiza los datos de una maquinaria existente.
     *
     * @param id        identificador de la maquinaria a actualizar
     * @param machinery datos actualizados de la maquinaria
     * @return la maquinaria actualizada
     * @throws MachineryNotFoundException si la maquinaria no existe
     */
    @Override
    public Machinery updateMachinery(Long id, Machinery machinery) {
        Optional<Machinery> existing = machineryRepository.findById(id);
        if (existing.isPresent()) {
            Machinery m = existing.get();
            m.setNameMachinery(machinery.getNameMachinery());
            m.setCategory(machinery.getCategory());
            m.setStatus(machinery.getStatus());
            m.setPricePerDay(machinery.getPricePerDay());
            return machineryRepository.save(m);
        }
        throw new MachineryNotFoundException("Machinery not found with id: " + id);
    }

    /**
     * Elimina una maquinaria por su identificador.
     *
     * @param id identificador de la maquinaria a eliminar
     */
    @Override
    public void deleteMachinery(Long id) {
        machineryRepository.deleteById(id);
    }

    /**
     * Obtiene la lista de todas las maquinarias registradas.
     *
     * @return lista de objetos Machinery
     */
    @Override
    public List<Machinery> findAll() {
        return machineryRepository.findAll();
    }

    /**
     * Busca maquinarias por nombre, ignorando mayúsculas y minúsculas.
     *
     * @param nameMachinery nombre de la maquinaria a buscar
     * @return lista de maquinarias que coinciden con el nombre
     */
    @Override
    public List<Machinery> findByNameMachinery(String nameMachinery) {
        return machineryRepository.findByNameMachineryContainingIgnoreCase(nameMachinery);
    }

    /**
     * Busca maquinarias por categoría, ignorando mayúsculas y minúsculas.
     *
     * @param name nombre de la categoría
     * @return lista de maquinarias que pertenecen a la categoría
     */
    @Override
    public List<Machinery> findByCategory(String name) {
        return machineryRepository.findByCategory_NameIgnoreCase(name);
    }

    class MachineryNotFoundException extends RuntimeException {
        public MachineryNotFoundException(String message) {
            super(message);
        }
    }
}