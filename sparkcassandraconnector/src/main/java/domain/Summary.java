package domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class Summary implements Serializable{

    private int product;
    private BigDecimal summary;

    public Summary() {
    }

    public Summary(int product, BigDecimal summary) {
        this.product = product;
        this.summary = summary;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public BigDecimal getSummary() {
        return summary;
    }

    public void setSummary(BigDecimal summary) {
        this.summary = summary;
    }
}
