package br.com.aplicacao.cupom_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "O nome do produto não pode ser vazio")
    private String name;

    @Column(nullable = false)
    @NotEmpty(message = "O EAN não pode ser vazio")
    private String ean;

    @Positive(message = "O preço unitário deve ser positivo")
    private double unitaryPrice;

    @PositiveOrZero(message = "A quantidade não pode ser negativa")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "cupom_id", nullable = true)
    @JsonBackReference // Previne recursão infinita ao serializar objetos para JSON
    private Cupom cupom;

    // Construtor sem argumentos
    public Produto() {
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public double getUnitaryPrice() {
        return unitaryPrice;
    }

    public void setUnitaryPrice(double unitaryPrice) {
        this.unitaryPrice = unitaryPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Cupom getCupom() {
        return cupom;
    }

    public void setCupom(Cupom cupom) {
        this.cupom = cupom;
    }
}
