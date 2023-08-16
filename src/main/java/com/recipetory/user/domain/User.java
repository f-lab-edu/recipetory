package com.recipetory.user.domain;

import com.recipetory.recipe.domain.Recipe;
import com.recipetory.user.domain.exception.InvalidUserRoleException;
import com.recipetory.user.domain.follow.Follow;
import com.recipetory.utils.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,
            length = 50)
    private String email;

    @Column(nullable = false,
            length = 15)
    private String name;

    @Column(length = 200)
    private String bio = "";

    @Column(length = 255)
    private String imageUrl;

    @OneToMany(targetEntity = Recipe.class,
            mappedBy = "author")
    private List<Recipe> recipes;

    // 이 유저가 팔로잉하는 사람들
    @OneToMany(targetEntity = Follow.class,
            mappedBy = "followed",
            orphanRemoval = true)
    private List<Follow> followings;

    // 이 유저를 팔로우하는 사람들
    @OneToMany(targetEntity = Follow.class,
            mappedBy = "following",
            orphanRemoval = true)
    private List<Follow> followers;

    // OAuth2
    @Column(length = 15)
    private String provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,
            length = 15)
    private Role role;

    public GrantedAuthority getAuthority() {
        return new SimpleGrantedAuthority(role.getKey());
    }

    public void verifyUserHasRole(Role requiredRole) {
        if (role != requiredRole) {
            throw new InvalidUserRoleException(id, role, requiredRole);
        }
    }

    public void editNameAndBio(String name, String bio) {
        this.name = name;
        this.bio = bio;
        this.role = Role.USER;
    }
}
