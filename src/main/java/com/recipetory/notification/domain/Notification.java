package com.recipetory.notification.domain;

import com.recipetory.user.domain.User;
import com.recipetory.utils.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Notification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean isRead;

    @Column(length = 200)
    private String message;

    // notification 관련된 정보 링크
    @Column
    private String path;

    @Column
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @JoinColumn(name = "receiver_id",
            updatable = false)
    @ManyToOne(targetEntity = User.class,
            fetch = FetchType.LAZY)
    private User receiver;

    @JoinColumn(name = "sender_id")
    @ManyToOne(targetEntity = User.class,
            fetch = FetchType.LAZY)
    private User sender;

    public void checkNotification() {
        this.isRead = true;
    }
}
