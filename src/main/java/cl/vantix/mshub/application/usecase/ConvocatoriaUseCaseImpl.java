package cl.vantix.mshub.application.usecase;

import cl.vantix.mshub.domain.exception.ResourceNotFoundException;
import cl.vantix.mshub.domain.model.*;
import cl.vantix.mshub.domain.port.in.ConvocatoriaUseCase;
import cl.vantix.mshub.domain.port.out.ConvocatoriaPersistencePort;
import cl.vantix.mshub.domain.port.out.UsuarioPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service @RequiredArgsConstructor
public class ConvocatoriaUseCaseImpl implements ConvocatoriaUseCase {

    private final ConvocatoriaPersistencePort convocatoriaPort;
    private final UsuarioPersistencePort usuarioPort;

    @Override public List<Convocatoria> listarPublicas() { return convocatoriaPort.findPublicas(); }
    @Override public List<Convocatoria> listarTodas()    { return convocatoriaPort.findAll(); }
    @Override public Convocatoria obtenerPorId(Long id)  {
        return convocatoriaPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Convocatoria " + id + " no encontrada."));
    }

    @Override @Transactional
    public Convocatoria crear(String titulo, String descripcion, String organizacion,
                              LocalDate fechaInicio, LocalDate fechaFin, String imagen, Long usuarioId) {
        Convocatoria conv = Convocatoria.builder()
                .titulo(titulo).descripcion(descripcion).organizacion(organizacion)
                .fechaInicio(fechaInicio).fechaFin(fechaFin).imagen(imagen)
                .estado(EstadoConvocatoria.BORRADOR).tipoRegistro(TipoRegistro.ABIERTO)
                .maxPostulacionesPorUsuario(1).creadoPorId(usuarioId).build();
        return convocatoriaPort.save(conv);
    }

    @Override @Transactional
    public Convocatoria actualizar(Long id, String titulo, String descripcion, String organizacion,
                                   LocalDate fechaInicio, LocalDate fechaFin, String imagen, Long usuarioId) {
        Convocatoria conv = obtenerPorId(id);
        conv.setTitulo(titulo); conv.setDescripcion(descripcion); conv.setOrganizacion(organizacion);
        if (fechaInicio != null) conv.setFechaInicio(fechaInicio);
        if (fechaFin != null)    conv.setFechaFin(fechaFin);
        if (imagen != null && !imagen.isBlank()) conv.setImagen(imagen);
        return convocatoriaPort.save(conv);
    }

    @Override @Transactional
    public Convocatoria cambiarEstado(Long id, EstadoConvocatoria estado, Long usuarioId) {
        Convocatoria conv = obtenerPorId(id); conv.setEstado(estado); return convocatoriaPort.save(conv);
    }

    @Override @Transactional
    public void eliminar(Long id, Long usuarioId) {
        if (!convocatoriaPort.existsById(id))
            throw new ResourceNotFoundException("Convocatoria " + id + " no encontrada.");
        convocatoriaPort.deleteById(id);
    }
}
