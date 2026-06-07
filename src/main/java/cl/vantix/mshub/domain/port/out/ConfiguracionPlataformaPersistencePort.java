package cl.vantix.mshub.domain.port.out;

import cl.vantix.mshub.domain.model.ConfiguracionPlataforma;
import java.util.List;
import java.util.Optional;

public interface ConfiguracionPlataformaPersistencePort {
    ConfiguracionPlataforma save(ConfiguracionPlataforma cfg);
    List<ConfiguracionPlataforma> saveAll(List<ConfiguracionPlataforma> cfgs);
    List<ConfiguracionPlataforma> findByInstitucionId(Long institucionId);
    Optional<ConfiguracionPlataforma> findByInstitucionIdAndClave(Long institucionId, String clave);
}
