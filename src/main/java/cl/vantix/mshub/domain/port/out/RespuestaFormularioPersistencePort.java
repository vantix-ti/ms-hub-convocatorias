package cl.vantix.mshub.domain.port.out;
import cl.vantix.mshub.domain.model.RespuestaFormulario;
import java.util.List;
import java.util.Optional;

public interface RespuestaFormularioPersistencePort {
    Optional<RespuestaFormulario> findByPostulacionIdAndCampoId(Long postulacionId, Long campoId);
    List<RespuestaFormulario> findByPostulacionId(Long postulacionId);
    RespuestaFormulario save(RespuestaFormulario respuesta);
}
