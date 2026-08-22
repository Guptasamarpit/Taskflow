package com.taskflow.entity; import jakarta.persistence.*; import lombok.*;
@Entity @Getter @Setter @NoArgsConstructor public class Project { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id; @Column(nullable=false) String name; String description; @ManyToOne(fetch=FetchType.LAZY,optional=false) User owner; }
