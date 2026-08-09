package com.shashi.comhub.specification;

import com.shashi.comhub.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"
                );
    }

    public static Specification<Product> hasBrand(String brand) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("brand"), brand);
    }

    public static Specification<Product> priceGreaterThanOrEqual(BigDecimal price) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), price);
    }

    public static Specification<Product> priceLessThanOrEqual(BigDecimal price) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), price);
    }

    public static Specification<Product> build(
            String name,
            String brand,
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) {
        Specification<Product> specification = null;

        if (name != null && !name.isBlank()) {
            specification = hasName(name);
        }

        if (brand != null && !brand.isBlank()) {
            specification = specification == null
                    ? hasBrand(brand)
                    : specification.and(hasBrand(brand));
        }

        if (minPrice != null) {
            specification = specification == null
                    ? priceGreaterThanOrEqual(minPrice)
                    : specification.and(priceGreaterThanOrEqual(minPrice));
        }

        if (maxPrice != null) {
            specification = specification == null
                    ? priceLessThanOrEqual(maxPrice)
                    : specification.and(priceLessThanOrEqual(maxPrice));
        }

        return specification;
    }
}