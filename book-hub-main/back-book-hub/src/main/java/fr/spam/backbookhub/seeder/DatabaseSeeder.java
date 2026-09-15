package fr.spam.backbookhub.seeder;

import fr.spam.backbookhub.features.connection.role.seeders.RoleSeeder;
import fr.spam.backbookhub.features.connection.user.seeders.UserSeeder;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DatabaseSeeder implements ApplicationRunner {

    private final RoleSeeder roleSeeder;
    private final UserSeeder userSeeder;

    public DatabaseSeeder(RoleSeeder roleSeeder, UserSeeder userSeeder) {
        this.roleSeeder = roleSeeder;
        this.userSeeder = userSeeder;
    }

    @Override
    public void run(ApplicationArguments args) {
        roleSeeder.seed();
        userSeeder.seed();
    }
}