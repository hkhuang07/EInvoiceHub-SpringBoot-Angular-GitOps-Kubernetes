package com.einvoicehub.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "vw_sys_user_author")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EinvUserAuthorEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "full_name")
    private String fullName;
}