package cl.vantix.mshub.domain.port.in;
import cl.vantix.mshub.domain.model.Postulacion;
import java.time.LocalDate;
import java.util.List;

public interface PostulacionUseCase {
    Postulacion crear(Long convocatoriaId, Long postulanteId);
    Postulacion obtenerPorId(Long id, Long usuarioId);
    List<Postulacion> listarPorUsuario(Long usuarioId);
    List<Postulacion> listarPorConvocatoria(Long convocatoriaId);
    Postulacion guardarRespuesta(Long postulacionId, Long campoId,
                                  String valorTexto, Double valorNumero,
                                  LocalDate valorFecha, Long usuarioId);
    Postulacion enviar(Long postulacionId, Long usuarioId);
    byte[] generarPdf(Long postulacionId);
}
