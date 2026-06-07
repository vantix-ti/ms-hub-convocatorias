package cl.vantix.mshub.domain.port.in;

import cl.vantix.mshub.domain.model.ConfiguracionPlataforma;
import java.util.List;
import java.util.Map;

public interface ConfiguracionUseCase {
    List<ConfiguracionPlataforma> obtenerPorInstitucion(Long institucionId);
    List<ConfiguracionPlataforma> actualizarConfiguracion(Long institucionId, Map<String, String> valores);
}
