package kz.toko.app.entity;

import kz.toko.app.entity.audit.AuditableEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;

import static java.util.Collections.emptySet;
import static java.util.Objects.nonNull;

@Data
@Entity
@Table(name = "users")
public class UserEntity extends AuditableEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String username;

    @NotEmpty
    @Column(nullable = false)
    private String firstName;

    @NotEmpty
    @Column(nullable = false)
    private String lastName;

    private String email;

    @NotEmpty
    @Column
    private String password;

    @Column
    private String phone;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return emptySet();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return nonNull(this.deletedAt);
    }

    public String getFullName() {
        return String.format("%s %s", this.firstName, this.lastName);
    }
}
