package cl.vantix.mshub.domain.port.in;
import cl.vantix.mshub.domain.model.Dashboard;
import cl.vantix.mshub.domain.model.DashboardGlobal;

public interface DashboardUseCase {
    Dashboard obtenerDashboard(Long convocatoriaId);
    DashboardGlobal obtenerDashboardGlobal();
}
