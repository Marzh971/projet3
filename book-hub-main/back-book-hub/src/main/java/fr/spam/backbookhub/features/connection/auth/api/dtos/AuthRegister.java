package fr.spam.backbookhub.features.connection.auth.api.dtos;

public record AuthRegister(
        String username,
        String firstName,
        String lastName,
        String email,
        String password,
        String phone
) {
    @Override
    public String username() {
        return username;
    }

    @Override
    public String firstName() {
        return firstName;
    }

    @Override
    public String lastName() {
        return lastName;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public String phone() {
        return phone;
    }
}
