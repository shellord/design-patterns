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

    public static class Builder {
        private String id;
        private String name;
        private String email;
        private MemberType type;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder type(MemberType type) {
            this.type = type;
            return this;
        }

        public Member build() {
            if (id == null || name == null || email == null || type == null) {
                throw new IllegalArgumentException("All fields are required");
            }
            return new Member(id, name, email, type);
        }

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
