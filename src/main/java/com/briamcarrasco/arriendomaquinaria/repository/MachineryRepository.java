package com.briamcarrasco.arriendomaquinaria.repository;

import com.briamcarrasco.arriendomaquinaria.model.Machinery;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repositorio para la entidad Machinery.
 * Proporciona métodos para acceder y gestionar maquinarias en la base de datos.
 */
public interface MachineryRepository extends JpaRepository<Machinery, Long> {

    /**
     * Verifica si existe una maquinaria con el nombre especificado.
     *
     * @param nameMachinery nombre de la maquinaria
     * @return true si existe, false en caso contrario
     */
    boolean existsByNameMachinery(String nameMachinery);

    /**
     * Busca una maquinaria por su nombre.
     *
     * @param nameMachinery nombre de la maquinaria
     * @return la maquinaria encontrada o null si no existe
     */
    Machinery findByNameMachinery(String nameMachinery);

    /**
     * Busca maquinarias cuyo nombre contiene el texto especificado, ignorando
     * mayúsculas y minúsculas.
     *
     * @param nameMachinery texto a buscar en el nombre de la maquinaria
     * @return lista de maquinarias que coinciden con el criterio
     */
    List<Machinery> findByNameMachineryContainingIgnoreCase(String nameMachinery);

    /**
     * Busca maquinarias por el nombre de la categoría, ignorando mayúsculas y
     * minúsculas.
     *
     * @param name nombre de la categoría
     * @return lista de maquinarias que pertenecen a la categoría
     */
    List<Machinery> findByCategory_NameIgnoreCase(String name);
}