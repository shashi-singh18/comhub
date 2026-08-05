package com.shashi.comhub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Category name cannot be blank")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Description name cannot be blank")
    @Column(nullable = false, length = 500)
    private String description;

    @NotNull(message = "Price cannot be null.")
    @Positive(message = "Price must be greater than zero.")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;;

    @NotBlank(message = "Brand name cannot be blank")
    @Column(nullable = false, length = 100)
    private String brand;

    @NotBlank(message = "Image url cannot be blank")
    @Column(nullable = false, length = 500)
    private String imageUrl;

    @NotNull(message = "Stock cannot be null.")
    @PositiveOrZero(message = "Stock cannot be negative.")
    @Column(nullable = false)
    private Long stock;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
