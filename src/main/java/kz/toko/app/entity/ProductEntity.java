package kz.toko.app.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@Table(name = "products")
@EqualsAndHashCode(callSuper=false)
public class ProductEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_link")
    private String imagePath;

    @NotEmpty
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

}
