package cl.vantix.mshub.domain.port.in;

import cl.vantix.mshub.domain.model.Institucion;
import java.util.List;

public interface InstitucionUseCase {
    Institucion crear(String nombre, String rut, String direccion, String telefono, String email, String logoUrl);
    Institucion actualizar(Long id, String nombre, String rut, String direccion, String telefono, String email, String logoUrl, Boolean activo);
    Institucion obtenerPorId(Long id);
    List<Institucion> listarTodas();
}
