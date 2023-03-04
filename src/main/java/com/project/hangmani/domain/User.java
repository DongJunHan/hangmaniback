package com.project.hangmani.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class User {
    private String member_id;
    private int money;

    public User() {
    }

    public User(String member_id, int money) {
        this.member_id = member_id;
        this.money = money;
    }
}
