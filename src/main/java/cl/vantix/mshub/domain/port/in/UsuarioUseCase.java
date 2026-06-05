package cl.vantix.mshub.domain.port.in;
import cl.vantix.mshub.domain.model.Rol;
import cl.vantix.mshub.domain.model.Usuario;
import java.util.List;

public interface UsuarioUseCase {
    Usuario obtenerPorEmail(String email);
    Usuario actualizarPerfil(String email, String nombre, String apellidoPaterno,
                             String apellidoMaterno, String telefono);
    List<Usuario> listarPorRol(Rol rol);
    List<Usuario> listarTodos();
    /**
     * @param password null/vacío → contraseña automática + email de activación (confirmado=false).
     *                 Con valor  → contraseña definitiva, cuenta activa de inmediato (confirmado=true).
     */
    Usuario crearUsuario(String nombre, String apellidoPaterno, String apellidoMaterno,
                         String email, String telefono, Rol rol, String password);

    Usuario cambiarRol(Long id, Rol rol);

    /** Cambia la contraseña del usuario autenticado, verificando la contraseña actual. */
    void cambiarPassword(String email, String passwordActual, String nuevaPassword);

}

