package com.tui.proof.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import nonapi.io.github.classgraph.json.Id;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tb_address",
        indexes = {@Index(name = "idx_address", columnList = "street, postcode, city, houseNumber, flatNumber, country")})
public class Address {
  @Id
  @GeneratedValue(
          strategy = GenerationType.SEQUENCE,
          generator = "seq_address_generator")
  @SequenceGenerator(name = "seq_address_generator", sequenceName = "seq_address")
  private Long id;

  @Version
  @Column(name = "version")
  private Integer version;

  @Column(name = "street", length = 256)
  @NotNull(message = "Street cannot be null")
  @Size(min = 2, max = 256, message = "Length of street should be in range: from 2 to 256")
  private String street;

  @Column(name = "postcode", length = 64)
  @NotNull(message = "Postcode cannot be null")
  @Size(min = 2, max = 64, message = "Length of post code should be in range: from 2 to 64")
  private String postcode;

  @Column(name = "city", length = 256)
  @NotNull(message = "City cannot be null")
  @Size(min = 2, max = 256, message = "Length of city should be in range: from 2 to 64")
  private String city;

  @Column(name = "houseNumber", length = 16)
  @NotNull(message = "House number cannot be null")
  @Size(min = 1, max = 16, message = "Length of house number should be in range: from 1 to 16")
  private String houseNumber;

  @Column(name = "flatNumber", length = 16)
  @Size(min = 1, max = 16, message = "Length of flat number should be in range: from 1 to 16")
  private String flatNumber;

  @Column(name = "country", length = 256)
  @NotNull(message = "Country cannot be null")
  @Size(min = 2, max = 256, message = "Length of country should be in range: from 2 to 32")
  private String country;

  @ManyToMany(mappedBy = "addresses")
  private Set<Client> clients = new HashSet<>();

}
