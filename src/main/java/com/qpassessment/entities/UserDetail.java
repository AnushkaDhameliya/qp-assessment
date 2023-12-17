package com.qpassessment.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="USER_DETAIL")
public class UserDetail {

    public enum Role{
        USER,ADMIN;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_detail_userid_seq")
    @SequenceGenerator(name="user_detail_userid_seq", sequenceName = "user_detail_userid_seq", allocationSize=1)
    @Column(name="USERID")
    private Long userId;

    @Column(name="NAME")
    private String name;

    @Column(name="USERNAME",unique = true)
    private String username;

    @Column(name="PASSWORD")
    private String password;

    @Column(name="EMAIL",unique = true)
    private String email;

    @Column(name="ROLE")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name="CREATED_DATE")
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(name="LAST_LOGIN_DATE")
    @UpdateTimestamp
    private Timestamp lastLoginDate;

//    @OneToMany
//    @JoinColumn(name = "USER_ID")
//    private Set<UserOrder> userOrdersSet;
}
