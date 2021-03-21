package tech.itpark.model;

public class RemovalModel {
    private final String login;
    private final String password;

    public RemovalModel(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
