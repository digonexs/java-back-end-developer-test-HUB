package br.com.aplicacao.cupom_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cupom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 44, unique = true, nullable = false)
    @NotNull(message = "O código não pode ser nulo")
    @Size(min = 44, max = 44, message = "O código deve ter 44 dígitos")
    private String code44;

    @NotNull(message = "A data da compra não pode ser nula")
    private LocalDate purchaseDate;

    @Positive(message = "O valor total deve ser positivo")
    private double totalValue;

    @NotEmpty(message = "O documento da empresa não pode ser vazio")
    private String companyDocument;

    @NotEmpty(message = "O estado não pode ser vazio")
    private String state;

    @OneToMany(mappedBy = "cupom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // Previne recursão infinita ao serializar objetos para JSON
    private List<Produto> produtos = new ArrayList<>();

    @NotEmpty(message = "O nome do comprador não pode ser vazio")
    private String buyerName;

    @NotNull(message = "A data de nascimento do comprador não pode ser nula")
    private LocalDate buyerBirthDate;

    @NotEmpty(message = "O documento do comprador não pode ser vazio")
    private String buyerDocument;

    // Construtor sem argumentos
    public Cupom() {
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode44() {
        return code44;
    }

    public void setCode44(String code44) {
        this.code44 = code44;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public String getCompanyDocument() {
        return companyDocument;
    }

    public void setCompanyDocument(String companyDocument) {
        this.companyDocument = companyDocument;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public LocalDate getBuyerBirthDate() {
        return buyerBirthDate;
    }

    public void setBuyerBirthDate(LocalDate buyerBirthDate) {
        this.buyerBirthDate = buyerBirthDate;
    }

    public String getBuyerDocument() {
        return buyerDocument;
    }

    public void setBuyerDocument(String buyerDocument) {
        this.buyerDocument = buyerDocument;
    }
}
