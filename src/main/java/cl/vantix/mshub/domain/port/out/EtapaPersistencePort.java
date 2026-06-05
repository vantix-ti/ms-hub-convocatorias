package cl.vantix.mshub.domain.port.out;
import cl.vantix.mshub.domain.model.Etapa;
import java.util.List;
import java.util.Optional;

public interface EtapaPersistencePort {
    List<Etapa> findByConvocatoriaId(Long convocatoriaId);
    Optional<Etapa> findById(Long id);
    Etapa save(Etapa etapa);
    void deleteById(Long id);
}
