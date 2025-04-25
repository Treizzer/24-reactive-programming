package com.thymeleaf.thymeleaf_reactive_example.presentation.dto;

public class ProductDto {

    private Integer id;
    private String concept;
    private Integer amount;
    
    public ProductDto() {
    }

    public ProductDto(Integer id, String concept, Integer amount) {
        this.id = id;
        this.concept = concept;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ProductDto [id=" + id + ", concept=" + concept + ", amount=" + amount + "]";
    }
    
}
