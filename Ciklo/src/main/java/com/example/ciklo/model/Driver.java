package com.example.ciklo.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Builder
@Data
@Table(name="_driver")
public class Driver implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer driverId;
    @Nationalized
    private String dFirstname;
    @Nationalized
    private String dLastname;
    @Column(nullable= false)
    private String DEmail;
    @Column(nullable= false)
    @JsonIgnore
    private String dPassword;
    private String dPhone;
    private boolean dStatus;
    private boolean isActive ;
//    @OneToMany(fetch = FetchType.EAGER)
//    @Transient
//    private List<Bill> bill = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        list.add(new SimpleGrantedAuthority("DRIVER"));
        return list;
    }

    @Override
    public String getPassword() {
        return dPassword;
    }

    @Override
    public String getUsername() {
        return DEmail;
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
        return isActive;
    }
}
