package br.com.aplicacao.cupom_system.repository;

import br.com.aplicacao.cupom_system.model.Cupom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A interface CupomRepository é um repositório JPA para a entidade Cupom.
 * Ela permite realizar operações CRUD na entidade Cupom e fornece um método de consulta
 * personalizado para buscar cupons pelo campo 'code44'.
 */
@Repository
public interface CupomRepository extends JpaRepository<Cupom, Long> {

    /**
     * Método de consulta personalizado para buscar um cupom usando o campo 'code44'.
     * Este método segue a convenção de nomenclatura do Spring Data JPA, que automaticamente
     * gera a implementação da consulta com base no nome do método.
     *
     * @param code44 Código de 44 dígitos único do cupom.
     * @return Um Optional contendo o cupom encontrado ou vazio se não for encontrado.
     */
    Optional<Cupom> findByCode44(String code44);
}
