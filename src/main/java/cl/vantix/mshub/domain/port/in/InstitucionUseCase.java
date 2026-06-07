package cl.vantix.mshub.domain.port.in;

import cl.vantix.mshub.domain.model.Institucion;
import java.util.List;

public interface InstitucionUseCase {
    Institucion crear(String nombre, String rut, String direccion, String telefono, String email, String logoUrl, String slug);
    Institucion actualizar(Long id, String nombre, String rut, String direccion, String telefono, String email, String logoUrl, Boolean activo, String slug);
    Institucion obtenerPorId(Long id);
    Institucion obtenerPorSlug(String slug);
    List<Institucion> listarTodas();
}
