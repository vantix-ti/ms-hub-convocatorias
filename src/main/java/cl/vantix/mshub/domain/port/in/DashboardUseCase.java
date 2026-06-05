package cl.vantix.mshub.domain.port.in;
import cl.vantix.mshub.domain.model.Dashboard;

public interface DashboardUseCase {
    Dashboard obtenerDashboard(Long convocatoriaId);
}
