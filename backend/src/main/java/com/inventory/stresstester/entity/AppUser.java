package com.inventory.stresstester.entity;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity @Table(name="users")
public class AppUser extends BaseEntity {
    @Column(nullable=false, length=100) private String name;
    @Column(nullable=false, unique=true, length=150) private String email;
    @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
    @Column(nullable=false) private String passwordHash;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="role_id") private Role role;
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPasswordHash(){return passwordHash;} public void setPasswordHash(String v){passwordHash=v;}
    public Role getRole(){return role;} public void setRole(Role v){role=v;}
}
