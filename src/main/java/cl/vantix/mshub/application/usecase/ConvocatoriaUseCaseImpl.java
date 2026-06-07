package cl.vantix.mshub.application.usecase;

import cl.vantix.mshub.domain.exception.ResourceNotFoundException;
import cl.vantix.mshub.domain.model.*;
import cl.vantix.mshub.domain.port.in.ConvocatoriaUseCase;
import cl.vantix.mshub.domain.port.out.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service @RequiredArgsConstructor
public class ConvocatoriaUseCaseImpl implements ConvocatoriaUseCase {

    private final ConvocatoriaPersistencePort convocatoriaPort;
    private final UsuarioPersistencePort usuarioPort;
    private final DocumentoAdjuntoPersistencePort documentoPort;

    @Override public List<Convocatoria> listarPublicas() { return convocatoriaPort.findPublicas(); }
    @Override public List<Convocatoria> listarTodas()    { return convocatoriaPort.findAll(); }

    @Override
    public List<Convocatoria> listarPorInstitucion(Long institucionId) {
        java.util.Set<Long> idsInstitucion = usuarioPort.findByInstitucionId(institucionId)
                .stream().map(Usuario::getId).collect(java.util.stream.Collectors.toSet());
        return convocatoriaPort.findAll().stream()
                .filter(c -> c.getCreadoPorId() != null && idsInstitucion.contains(c.getCreadoPorId()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override public Convocatoria obtenerPorId(Long id) {
        return convocatoriaPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Convocatoria " + id + " no encontrada."));
    }

    @Override @Transactional
    public Convocatoria crear(String titulo, String descripcion, String organizacion,
                              LocalDate fechaInicio, LocalDate fechaFin, String imagen,
                              Long usuarioId, List<DocumentoAdjunto> documentos) {
        Convocatoria conv = Convocatoria.builder()
                .titulo(titulo).descripcion(descripcion).organizacion(organizacion)
                .fechaInicio(fechaInicio).fechaFin(fechaFin).imagen(imagen)
                .estado(EstadoConvocatoria.BORRADOR).tipoRegistro(TipoRegistro.ABIERTO)
                .maxPostulacionesPorUsuario(1).creadoPorId(usuarioId).build();
        Convocatoria saved = convocatoriaPort.save(conv);
        if (documentos != null) {
            for (DocumentoAdjunto doc : documentos) {
                doc.setConvocatoriaId(saved.getId());
                documentoPort.save(doc);
            }
        }
        return saved;
    }

    @Override @Transactional
    public Convocatoria actualizar(Long id, String titulo, String descripcion, String organizacion,
                                   LocalDate fechaInicio, LocalDate fechaFin, String imagen,
                                   Long usuarioId, List<DocumentoAdjunto> documentos) {
        Convocatoria conv = obtenerPorId(id);
        conv.setTitulo(titulo); conv.setDescripcion(descripcion); conv.setOrganizacion(organizacion);
        if (fechaInicio != null) conv.setFechaInicio(fechaInicio);
        if (fechaFin    != null) conv.setFechaFin(fechaFin);
        if (imagen != null)      conv.setImagen(imagen);
        Convocatoria saved = convocatoriaPort.save(conv);
        // Reemplazar documentos: borrar los anteriores y guardar los nuevos
        if (documentos != null) {
            documentoPort.deleteByConvocatoriaId(id);
            for (DocumentoAdjunto doc : documentos) {
                doc.setConvocatoriaId(id);
                documentoPort.save(doc);
            }
        }
        return saved;
    }

    @Override @Transactional
    public Convocatoria cambiarEstado(Long id, EstadoConvocatoria estado, Long usuarioId) {
        Convocatoria conv = obtenerPorId(id); conv.setEstado(estado); return convocatoriaPort.save(conv);
    }

    @Override @Transactional
    public void eliminar(Long id, Long usuarioId) {
        if (!convocatoriaPort.existsById(id))
            throw new ResourceNotFoundException("Convocatoria " + id + " no encontrada.");
        documentoPort.deleteByConvocatoriaId(id);
        convocatoriaPort.deleteById(id);
    }
}
