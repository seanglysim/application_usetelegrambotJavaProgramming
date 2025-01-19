package application_usetelegrambot.model;

import lombok.Getter;
import lombok.Setter;

public class User {
    @Getter
    private final int id;
    @Getter
    private final String uuid;
    private String name;
    private String email;
    private boolean isDeleted;

    public User(int id, String uuid, String name, String email, boolean isDeleted) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.isDeleted = isDeleted;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return id + " | " + uuid + " | " + name + " | " + email + " | " + (isDeleted ? "True" : "False");
    }
}
