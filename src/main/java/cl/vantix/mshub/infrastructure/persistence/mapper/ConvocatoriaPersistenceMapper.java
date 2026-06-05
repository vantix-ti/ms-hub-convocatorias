package cl.vantix.mshub.infrastructure.persistence.mapper;

import cl.vantix.mshub.domain.model.*;
import cl.vantix.mshub.infrastructure.persistence.entity.*;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class ConvocatoriaPersistenceMapper {

    public Convocatoria toDomain(ConvocatoriaJpa jpa) {
        if (jpa == null) return null;
        return Convocatoria.builder()
                .id(jpa.getId())
                .titulo(jpa.getTitulo())
                .descripcion(jpa.getDescripcion())
                .organizacion(jpa.getOrganizacion())
                .fechaInicio(jpa.getFechaInicio())
                .fechaFin(jpa.getFechaFin())
                .imagen(jpa.getImagen())
                .estado(EstadoConvocatoria.valueOf(jpa.getEstado().getNombre()))
                .tipoRegistro(TipoRegistro.valueOf(jpa.getTipoRegistro().getNombre()))
                .maxPostulacionesPorUsuario(jpa.getMaxPostulacionesPorUsuario())
                .creadoPorId(jpa.getCreadoPor() != null ? jpa.getCreadoPor().getId() : null)
                .etiquetas(jpa.getEtiquetas().stream()
                        .map(e -> Etiqueta.builder()
                                .id(e.getId()).nombre(e.getNombre()).color(e.getColor()).build())
                        .collect(Collectors.toSet()))
                .creadoEn(jpa.getCreadoEn())
                .actualizadoEn(jpa.getActualizadoEn())
                .build();
    }
}
