package com.taskflow.entity; import jakarta.persistence.*; import lombok.*;
@Entity @Table(name="users") @Getter @Setter @NoArgsConstructor public class User { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id; @Column(nullable=false,unique=true) String email; @Column(nullable=false) String passwordHash; @Column(nullable=false) String name; }
