package io.bbahaida.bshort.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.bbahaida.bshort.model.enums.Role;
import io.bbahaida.bshort.security.TokenUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bshort_user", indexes = {
        @Index(name = "username", columnList = "username"),
        @Index(name = "email", columnList = "email")
})
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    @JsonIgnore
    private String password;
    private Boolean userActive;
    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDateTime createdAt;

    public TokenUser toTokenUser() {
        TokenUser tokenUser = new TokenUser();
        tokenUser.setId(getId());
        tokenUser.setPermissions(new String[]{getRole().name()});
        tokenUser.setUsername(getUsername());
        tokenUser.setPassword(null);
        tokenUser.setRememberMe(false);
        return tokenUser;
    }

}
