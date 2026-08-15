package api.api_methods;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Good {
    private Long id;
    private String name;
    private Double price;

    public Good() {}

    public Good(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    // Геттеры
    public Long getId() { return id; }
    public String getName() { return name; }
    public Double getPrice() { return price; }

    // Сеттеры для десериализации ответа GET
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(Double price) { this.price = price; }
}
