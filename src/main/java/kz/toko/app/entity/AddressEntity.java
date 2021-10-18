package kz.toko.app.entity;

import kz.toko.app.entity.audit.AuditableEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@Table(name = "addresses")
@EqualsAndHashCode(callSuper=false)
public class AddressEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String addressLine;

    @NotEmpty
    @Column(nullable = false)
    private String city;

    @Column
    private String adminArea;

    @NotEmpty
    @Column(nullable = false)
    private String postalCode;

    @NotEmpty
    @Column(nullable = false)
    private String country;

}
