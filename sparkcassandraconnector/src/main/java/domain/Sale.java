package domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public class Sale implements Serializable {
   private UUID  id;
   private int product;
   private BigDecimal price;

    public Sale() {
    }

    public Sale(UUID id, int product, BigDecimal price) {
        this.id = id;
        this.product = product;
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
