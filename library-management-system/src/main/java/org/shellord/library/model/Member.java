package org.shellord.library.model;

public class Member {
    private String id;
    private String name;
    private String email;
    private MemberType type;

    public Member(String id, String name, String email, MemberType type) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public MemberType getType() {
        return type;
    }
}
