package br.com.aplicacao.cupom_system.service;

import br.com.aplicacao.cupom_system.model.Produto;
import br.com.aplicacao.cupom_system.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * A classe ProdutoService contém a lógica de negócios para operações relacionadas a produtos.
 * Esta classe é responsável por criar, listar e buscar produtos, além de validar as informações dos produtos.
 */
@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository; // Repositório para realizar operações de persistência com produtos.

    /**
     * Cria um novo produto após realizar validações necessárias.
     *
     * @param produto O objeto Produto a ser criado.
     * @return O produto salvo no banco de dados.
     */
    @Transactional
    public Produto criarProduto(Produto produto) {
        // Validação do nome do produto: não pode ser nulo ou vazio.
        if (produto.getName() == null || produto.getName().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto não pode ser vazio.");
        }

        // Verifica se já existe um produto com o mesmo EAN.
        Optional<Produto> produtoExistenteOpt = produtoRepository.findByEan(produto.getEan());

        if (produtoExistenteOpt.isPresent()) {
            Produto produtoExistente = produtoExistenteOpt.get();

            // Verifica se o nome do produto existente é diferente.
            if (!produtoExistente.getName().equals(produto.getName())) {
                throw new IllegalArgumentException("O código EAN já está em uso por um produto com um nome diferente.");
            }
            // Se o nome for o mesmo, permite a duplicação do EAN.
        }

        // Tenta salvar o produto se todas as validações forem aprovadas.
        try {
            return produtoRepository.save(produto); // Salva o produto no banco de dados.
        } catch (DataIntegrityViolationException e) {
            // Lança uma exceção se ocorrer um erro de integridade de dados.
            throw new IllegalArgumentException("Erro de integridade de dados: " + e.getMessage());
        }
    }

    /**
     * Lista todos os produtos.
     *
     * @return Uma lista de todos os produtos no banco de dados.
     */
    public List<Produto> listarProdutos() {
        return produtoRepository.findAll(); // Retorna todos os produtos do repositório.
    }

    /**
     * Busca um produto pelo seu ID.
     *
     * @param id O ID do produto a ser buscado.
     * @return Um Optional contendo o produto encontrado ou vazio se não for encontrado.
     */
    public Optional<Produto> buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id); // Busca um produto pelo ID.
    }
}
