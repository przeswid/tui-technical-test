package com.tui.proof.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tb_client")
public class Client {
  @Id
  @GeneratedValue(
          strategy = GenerationType.SEQUENCE,
          generator = "seq_client_generator")
  @SequenceGenerator(name = "seq_client_generator", sequenceName = "seq_client")
  private Long id;

  @Version
  @Column(name = "version")
  private Integer version;

  @Column(name = "first_name", length = 256)
  @NotNull(message = "First name cannot be null")
  @Size(min = 2, max = 256, message = "Length of first name should be in range: from 2 to 256")
  private String firstName;

  @Column(name = "last_name", length = 256)
  @NotNull(message = "Last name name cannot be null")
  @Size(min = 2, max = 256, message = "Length of last name should be in range: from 2 to 256")
  private String lastName;

  @Column(name = "telephone", length = 64)
  @NotNull(message = "Telephone number cannot be null")
  @Pattern(regexp = "^[0-9\\-+]{5,15}$")
  private String telephone;

  @Column(name = "email", length = 256)
  @NotNull(message = "Email cannot be null")
  @Email
  private String email;

  @ManyToMany
  @JoinTable(
          name = "tb_client_address",
          joinColumns = @JoinColumn(name = "client_id"),
          inverseJoinColumns = @JoinColumn(name = "address_id"),
          indexes = {
                  @Index(name = "idx_fk_client_address_client_id", columnList = "client_id"),
                  @Index(name = "idx_fk_client_address_address_id", columnList = "address_id")
          })
  private Set<Address> addresses = new HashSet<>();
}
