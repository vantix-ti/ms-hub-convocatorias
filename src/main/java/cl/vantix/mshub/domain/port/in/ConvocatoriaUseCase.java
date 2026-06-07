package cl.vantix.mshub.domain.port.in;

import cl.vantix.mshub.domain.model.Convocatoria;
import cl.vantix.mshub.domain.model.DocumentoAdjunto;
import cl.vantix.mshub.domain.model.EstadoConvocatoria;
import java.time.LocalDate;
import java.util.List;

public interface ConvocatoriaUseCase {
    List<Convocatoria> listarPublicas();
    List<Convocatoria> listarTodas();
    List<Convocatoria> listarPorInstitucion(Long institucionId);
    Convocatoria obtenerPorId(Long id);
    Convocatoria crear(String titulo, String descripcion, String organizacion,
                       LocalDate fechaInicio, LocalDate fechaFin, String imagen,
                       Long usuarioId, List<DocumentoAdjunto> documentos);
    Convocatoria actualizar(Long id, String titulo, String descripcion, String organizacion,
                            LocalDate fechaInicio, LocalDate fechaFin, String imagen,
                            Long usuarioId, List<DocumentoAdjunto> documentos);
    Convocatoria cambiarEstado(Long id, EstadoConvocatoria estado, Long usuarioId);
    void eliminar(Long id, Long usuarioId);
}
