package ggs.ggs.member;

import lombok.Getter;

public class memberRole {
    @Getter
    public enum MemberRole {
        ADMIN("ROLE_ADMIN"),
        USER("ROLE_USER");

        MemberRole(String value) {
            this.value = value;
        }

        private String value;
    }
}

