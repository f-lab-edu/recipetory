package com.recipetory.user.domain.follow;

import com.recipetory.user.domain.User;
import com.recipetory.utils.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Builder
@Getter
public class Follow extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(nullable = false)
    private User following;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(nullable = false)
    private User followed;
}
