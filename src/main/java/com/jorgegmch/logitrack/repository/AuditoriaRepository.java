package com.jorgegmch.logitrack.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jorgegmch.logitrack.entity.Auditoria;
import com.jorgegmch.logitrack.entity.Usuario;
import com.jorgegmch.logitrack.entity.enums.TipoOperacion;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    List<Auditoria> findByUsuarioId(Usuario usuarioId);
    List<Auditoria> findByTipoOperacion(TipoOperacion tipoOperacion);

    @Query("SELECT a FROM Auditoria a "
            + "WHERE (:productoId IS NULL OR "
            + "       (a.entidadAfectada = 'Producto' AND "
            + "        (a.valoresNuevos LIKE CONCAT('%\"idProducto\":', :productoId, ',%') "
            + "         OR a.valoresAnteriores LIKE CONCAT('%\"idProducto\":', :productoId, ',%')))) "
            + "AND (:fechaInicio IS NULL OR a.fechaHora >= :fechaInicio) "
            + "AND (:fechaFin IS NULL OR a.fechaHora <= :fechaFin) "
            + "AND (:campoModificado IS NULL OR "
            + "     a.valoresNuevos LIKE CONCAT('%\"', :campoModificado, '\":%') "
            + "     OR a.valoresAnteriores LIKE CONCAT('%\"', :campoModificado, '\":%')) "
            + "ORDER BY a.fechaHora DESC")
    List<Auditoria> buscarConFiltros(
            @Param("productoId") Long productoId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            @Param("campoModificado") String campoModificado);
}