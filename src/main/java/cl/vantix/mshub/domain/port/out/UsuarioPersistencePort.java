package cl.vantix.mshub.domain.port.out;
import cl.vantix.mshub.domain.model.Rol;
import cl.vantix.mshub.domain.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioPersistencePort {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByTokenConfirmacion(String token);
    Optional<Usuario> findByTokenReset(String token);
    List<Usuario> findByRol(Rol rol);
    List<Usuario> findAll();
    List<Usuario> findByInstitucionId(Long institucionId);
    Usuario save(Usuario usuario);
    boolean existsByEmail(String email);
}
