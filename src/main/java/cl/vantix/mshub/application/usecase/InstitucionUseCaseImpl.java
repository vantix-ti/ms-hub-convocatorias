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
                             String telefono, String email, String logoUrl, String slug) {
        if (rut != null && !rut.isBlank() && port.existsByRut(rut))
            throw new BusinessException("Ya existe una institución con ese RUT.");
        String safeSlug = (slug != null && !slug.isBlank())
            ? slug.toLowerCase().replaceAll("[^a-z0-9-]", "-") : null;
        return port.save(Institucion.builder()
                .nombre(nombre).rut(rut).direccion(direccion)
                .telefono(telefono).email(email).logoUrl(logoUrl)
                .slug(safeSlug).activo(true).build());
    }

    @Override @Transactional
    public Institucion actualizar(Long id, String nombre, String rut, String direccion,
                                  String telefono, String email, String logoUrl, Boolean activo, String slug) {
        Institucion inst = port.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Institución no encontrada."));
        inst.setNombre(nombre);
        inst.setRut(rut);
        inst.setDireccion(direccion);
        inst.setTelefono(telefono);
        inst.setEmail(email);
        inst.setLogoUrl(logoUrl);
        if (activo != null) inst.setActivo(activo);
        if (slug != null && !slug.isBlank()) inst.setSlug(slug.toLowerCase().replaceAll("[^a-z0-9-]", "-"));
        return port.save(inst);
    }

    @Override @Transactional(readOnly = true)
    public Institucion obtenerPorSlug(String slug) {
        return port.findBySlug(slug)
                .orElseThrow(() -> new cl.vantix.mshub.domain.exception.ResourceNotFoundException("Institución no encontrada."));
    }

    public Institucion obtenerPorId(Long id) {
        return port.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Institución no encontrada."));
    }

    @Override @Transactional(readOnly = true)
    public List<Institucion> listarTodas() {
        return port.findAll();
    }
}
