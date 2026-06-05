package cl.vantix.mshub.domain.port.out;
import cl.vantix.mshub.domain.model.CampoFormulario;
import java.util.Optional;

public interface CampoFormularioPersistencePort {
    Optional<CampoFormulario> findById(Long id);
    CampoFormulario save(CampoFormulario campo);
    void deleteById(Long id);
}
