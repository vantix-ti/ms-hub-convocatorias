package cl.vantix.mshub.application.usecase;

import cl.vantix.mshub.domain.model.ConfiguracionPlataforma;
import cl.vantix.mshub.domain.port.in.ConfiguracionUseCase;
import cl.vantix.mshub.domain.port.out.ConfiguracionPlataformaPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class ConfiguracionUseCaseImpl implements ConfiguracionUseCase {

    private final ConfiguracionPlataformaPersistencePort port;

    @Override @Transactional(readOnly = true)
    public List<ConfiguracionPlataforma> obtenerPorInstitucion(Long institucionId) {
        return port.findByInstitucionId(institucionId);
    }

    @Override @Transactional
    public List<ConfiguracionPlataforma> actualizarConfiguracion(Long institucionId,
                                                                  Map<String, String> valores) {
        Map<String, ConfiguracionPlataforma> existentes = port.findByInstitucionId(institucionId)
                .stream().collect(Collectors.toMap(ConfiguracionPlataforma::getClave, c -> c));
        List<ConfiguracionPlataforma> toSave = new ArrayList<>();
        for (Map.Entry<String, String> entry : valores.entrySet()) {
            ConfiguracionPlataforma cfg = existentes.getOrDefault(entry.getKey(),
                    ConfiguracionPlataforma.builder()
                            .institucionId(institucionId).clave(entry.getKey()).build());
            cfg.setValor(entry.getValue());
            toSave.add(cfg);
        }
        return port.saveAll(toSave);
    }
}
