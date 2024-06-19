package com.cardealer.model;

import com.cardealer.model.enums.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Users implements UserDetails, Serializable {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String photo = "def.png";

    private String fio = "";
    private int age = 18;
    private String tel = "";
    private String email = "";

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<ProductApp> apps = new ArrayList<>();
    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<UserNote> notesAdmin = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserNote> notesUser = new ArrayList<>();

    public Users(String username, String password, Role role) {
        this.role = role;
        this.username = username;
        this.password = passwordEncoder().encode(password);
    }

    public void set(String fio, int age, String tel, String email) {
        this.fio = fio;
        this.age = age;
        this.tel = tel;
        this.email = email;
    }

    public List<UserNote> getNotesUser() {
        notesUser.sort(Comparator.comparing(UserNote::getId));
        Collections.reverse(notesUser);
        return notesUser;
    }

    public int getAppsSize() {
        return apps.size();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(getRole());
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
