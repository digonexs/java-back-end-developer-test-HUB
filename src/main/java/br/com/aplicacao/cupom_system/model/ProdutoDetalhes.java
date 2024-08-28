package br.com.aplicacao.cupom_system.model;

/**
 * A classe ProdutoDetalhes é uma classe de modelo que encapsula os detalhes
 * de um produto, incluindo o EAN, nome do produto, preço mínimo e preço máximo.
 * Essa classe é usada para transferir dados entre camadas da aplicação de uma maneira organizada.
 */
public class ProdutoDetalhes {

    private String ean; // Código EAN do produto, usado para identificação única.
    private String productName; // Nome do produto.
    private double minPrice; // Preço mínimo encontrado para o produto.
    private double maxPrice; // Preço máximo encontrado para o produto.

    /**
     * Construtor para inicializar um objeto ProdutoDetalhes com os valores fornecidos.
     * @param ean Código EAN do produto.
     * @param productName Nome do produto.
     * @param minPrice Preço mínimo do produto.
     * @param maxPrice Preço máximo do produto.
     */
    public ProdutoDetalhes(String ean, String productName, double minPrice, double maxPrice) {
        this.ean = ean;
        this.productName = productName;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    // Getters e Setters para acesso e modificação dos atributos da classe

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }
}