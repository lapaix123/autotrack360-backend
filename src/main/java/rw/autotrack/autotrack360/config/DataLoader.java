package rw.autotrack.autotrack360.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rw.autotrack.autotrack360.user.Role;
import rw.autotrack.autotrack360.user.User;
import rw.autotrack.autotrack360.user.UserRepository;
import rw.autotrack.autotrack360.vehicle.Vehicle;
import rw.autotrack.autotrack360.vehicle.VehicleRepository;
import rw.autotrack.autotrack360.vehicle.VehicleStatus;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(User.builder()
                    .name("Admin User").email("admin@autotrack360.com")
                    .password(passwordEncoder.encode("admin123")).role(Role.ADMIN).build());

            userRepository.save(User.builder()
                    .name("Sales Agent").email("sales@autotrack360.com")
                    .password(passwordEncoder.encode("sales123")).role(Role.SALES).build());

            userRepository.save(User.builder()
                    .name("Logistics Officer").email("logistics@autotrack360.com")
                    .password(passwordEncoder.encode("logistics123")).role(Role.LOGISTICS).build());

            userRepository.save(User.builder()
                    .name("Demo Customer").email("customer@autotrack360.com")
                    .password(passwordEncoder.encode("customer123")).role(Role.CUSTOMER).build());

            userRepository.save(User.builder()
                    .name("FastShip Ltd").email("shipping@autotrack360.com")
                    .password(passwordEncoder.encode("shipping123")).role(Role.SHIPPING_COMPANY).build());

            System.out.println("[DataLoader] Users seeded.");
        }

        if (vehicleRepository.count() == 0) {
            vehicleRepository.save(Vehicle.builder()
                    .chassisNumber("1HGCM82633A123456").make("Toyota").model("Land Cruiser")
                    .year(2023).color("White").status(VehicleStatus.AVAILABLE)
                    .price(new BigDecimal("75000.00")).build());

            vehicleRepository.save(Vehicle.builder()
                    .chassisNumber("2T1BURHE0JC123457").make("Honda").model("CR-V")
                    .year(2022).color("Black").status(VehicleStatus.IMPORTED)
                    .price(new BigDecimal("35000.00")).build());

            vehicleRepository.save(Vehicle.builder()
                    .chassisNumber("3VWFE21C04M123458").make("Mercedes").model("GLE 350")
                    .year(2023).color("Silver").status(VehicleStatus.IN_TRANSIT)
                    .price(new BigDecimal("95000.00")).build());

            System.out.println("[DataLoader] Vehicles seeded.");
        }
    }
}
