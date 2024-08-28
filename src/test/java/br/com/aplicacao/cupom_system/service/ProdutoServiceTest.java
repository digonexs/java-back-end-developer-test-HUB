package br.com.aplicacao.cupom_system.service;

import br.com.aplicacao.cupom_system.model.Produto;
import br.com.aplicacao.cupom_system.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Classe de teste para ProdutoService, usando JUnit e Mockito.
 * Esta classe testa a lógica de negócios da classe ProdutoService, validando a criação e a busca de produtos.
 */
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository; // Mock do repositório de produtos para simular operações de banco de dados.

    @InjectMocks
    private ProdutoService produtoService; // Instância da classe sendo testada, com mocks injetados.

    /**
     * Configuração inicial antes de cada teste.
     * Inicializa os mocks anotados com @Mock e os injeta na classe anotada com @InjectMocks.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Abre os mocks para a inicialização.
    }

    /**
     * Testa se o método criarProduto lança uma exceção quando o nome do produto é vazio.
     */
    @Test
    void criarProduto_DeveLancarExcecao_SeNomeVazio() {
        Produto produto = new Produto();
        produto.setName(""); // Nome vazio

        // Verifica se uma IllegalArgumentException é lançada com a mensagem correta.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            produtoService.criarProduto(produto);
        });

        assertEquals("Nome do produto não pode ser vazio.", exception.getMessage());
    }

    /**
     * Testa se o método criarProduto salva o produto corretamente quando os dados são válidos.
     */
    @Test
    void criarProduto_DeveSalvarProduto_SeDadosValidos() {
        Produto produto = new Produto();
        produto.setName("Produto Teste");
        produto.setEan("1234567890123");

        // Configura o mock para retornar o produto quando save() é chamado.
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        // Chama o método criarProduto com um produto válido.
        Produto resultado = produtoService.criarProduto(produto);

        // Verifica se o resultado não é nulo e se o método save() foi chamado uma vez.
        assertNotNull(resultado);
        verify(produtoRepository, times(1)).save(produto);
    }

    /**
     * Testa se o método buscarProdutoPorId retorna um produto quando o ID existe.
     */
    @Test
    void buscarProdutoPorId_DeveRetornarProduto_SeIdExistir() {
        Long id = 1L;
        Produto produto = new Produto();
        produto.setId(id);

        // Configura o mock para retornar um produto quando findById() é chamado.
        when(produtoRepository.findById(id)).thenReturn(Optional.of(produto));

        // Chama o método buscarProdutoPorId com um ID existente.
        Optional<Produto> resultado = produtoService.buscarProdutoPorId(id);

        // Verifica se o resultado contém um produto e se o ID do produto é igual ao esperado.
        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
    }

    /**
     * Testa se o método buscarProdutoPorId retorna vazio quando o ID não existe.
     */
    @Test
    void buscarProdutoPorId_DeveRetornarVazio_SeIdNaoExistir() {
        Long id = 1L;

        // Configura o mock para retornar vazio quando findById() é chamado com um ID inexistente.
        when(produtoRepository.findById(id)).thenReturn(Optional.empty());

        // Chama o método buscarProdutoPorId com um ID inexistente.
        Optional<Produto> resultado = produtoService.buscarProdutoPorId(id);

        // Verifica se o resultado está vazio.
        assertFalse(resultado.isPresent());
    }
}
