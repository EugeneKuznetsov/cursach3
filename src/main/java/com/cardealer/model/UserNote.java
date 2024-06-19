package com.cardealer.model;

import com.cardealer.controller.main.Main;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class UserNote implements Serializable {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    public String date = Main.getDateNow();
    @Column(length = 5000)
    public String text;

    @ManyToOne
    private Users admin;
    @ManyToOne
    private Users user;

    public UserNote(String text, Users admin, Users user) {
        this.text = text;
        this.admin = admin;
        this.user = user;
    }
}
