package com.example.ciklo.model;

import jakarta.persistence.*;
import lombok.*;
import org.codehaus.jackson.annotate.JsonIgnore;
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
@Builder
@Entity
@Data
@Table(name = "_customer")
public class Customer implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cusId")
    @SequenceGenerator(name="cusId",sequenceName="cusId_seq")
    @Column(nullable=false)
    private Integer cusId;
    @Nationalized
    private String cFirstname;
    @Nationalized
    private String cLastname;
    @Column(nullable = false)
    private String CEmail;
    @Column(nullable = false)
    @JsonIgnore
    private String cPassword;
    private String cPhone;
    private Boolean locked = false;
    private Boolean enabled = false;

//    @OneToMany(fetch = FetchType.EAGER)
//    @Transient
//    private List<Bill> bill = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("CUSTOMER"));
        return list;
    }

    @Override
    public String getPassword() {
        return cPassword;
    }

    @Override
    public String getUsername() {
        return CEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
