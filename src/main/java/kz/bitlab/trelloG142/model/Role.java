package kz.bitlab.trelloG142.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "t_roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity implements GrantedAuthority {
    @Column(name = "name")
    private String name;

    @Override
    public @Nullable String getAuthority() {
        return name;
    }
}
