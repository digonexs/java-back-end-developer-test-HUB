package br.com.aplicacao.cupom_system.repository;

import br.com.aplicacao.cupom_system.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A interface ProdutoRepository é um repositório JPA para a entidade Produto.
 * Ela permite realizar operações CRUD na entidade Produto e fornece um método de consulta
 * personalizado para buscar produtos pelo campo 'ean'.
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    /**
     * Método de consulta personalizado para buscar um produto usando o campo 'ean'.
     * Este método segue a convenção de nomenclatura do Spring Data JPA, que automaticamente
     * gera a implementação da consulta com base no nome do método.
     *
     * @param ean Código EAN do produto, usado para identificação única.
     * @return Um Optional contendo o produto encontrado ou vazio se não for encontrado.
     */
    Optional<Produto> findByEan(String ean);

    // Métodos de consulta personalizados adicionais podem ser adicionados aqui se necessário
}
