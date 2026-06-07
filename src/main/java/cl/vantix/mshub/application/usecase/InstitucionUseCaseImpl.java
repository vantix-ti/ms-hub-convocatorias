package cl.vantix.mshub.application.usecase;

import cl.vantix.mshub.domain.exception.BusinessException;
import cl.vantix.mshub.domain.exception.ResourceNotFoundException;
import cl.vantix.mshub.domain.model.Institucion;
import cl.vantix.mshub.domain.port.in.InstitucionUseCase;
import cl.vantix.mshub.domain.port.out.InstitucionPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor
public class InstitucionUseCaseImpl implements InstitucionUseCase {

    private final InstitucionPersistencePort port;

    @Override @Transactional
    public Institucion crear(String nombre, String rut, String direccion,
                             String telefono, String email, String logoUrl) {
        if (rut != null && !rut.isBlank() && port.existsByRut(rut))
            throw new BusinessException("Ya existe una institución con ese RUT.");
        return port.save(Institucion.builder()
                .nombre(nombre).rut(rut).direccion(direccion)
                .telefono(telefono).email(email).logoUrl(logoUrl)
                .activo(true).build());
    }

    @Override @Transactional
    public Institucion actualizar(Long id, String nombre, String rut, String direccion,
                                  String telefono, String email, String logoUrl) {
        Institucion inst = port.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Institución no encontrada."));
        inst.setNombre(nombre);
        inst.setRut(rut);
        inst.setDireccion(direccion);
        inst.setTelefono(telefono);
        inst.setEmail(email);
        inst.setLogoUrl(logoUrl);
        return port.save(inst);
    }

    @Override @Transactional(readOnly = true)
    public Institucion obtenerPorId(Long id) {
        return port.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Institución no encontrada."));
    }

    @Override @Transactional(readOnly = true)
    public List<Institucion> listarTodas() {
        return port.findAll();
    }
}
