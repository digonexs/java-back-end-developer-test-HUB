package br.com.aplicacao.cupom_system.controller;

import br.com.aplicacao.cupom_system.model.Produto;
import br.com.aplicacao.cupom_system.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService; // Injeta o serviço ProdutoService para operações de negócio com produtos.

    /**
     * Endpoint para criar um novo produto.
     * @param produto Objeto Produto recebido no corpo da requisição, validado automaticamente.
     * @return ResponseEntity com o produto criado ou mensagem de erro.
     */
    @PostMapping
    public ResponseEntity<?> criarProduto(@Valid @RequestBody Produto produto) {
        try {
            // Cria e salva o novo produto usando o serviço
            Produto novoProduto = produtoService.criarProduto(produto);
            return ResponseEntity.ok(novoProduto); // Retorna o produto criado
        } catch (IllegalArgumentException e) {
            // Captura e retorna mensagens de erro para argumentos ilegais
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint para listar todos os produtos.
     * @return ResponseEntity com a lista de produtos.
     */
    @GetMapping
    public ResponseEntity<List<Produto>> listarProdutos() {
        List<Produto> produtos = produtoService.listarProdutos(); // Obtém a lista de produtos usando o serviço
        return ResponseEntity.ok(produtos); // Retorna a lista de produtos
    }

    /**
     * Endpoint para buscar um produto específico por ID.
     * @param id ID do produto a ser buscado.
     * @return ResponseEntity com o produto encontrado ou resposta 404 se não encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable Long id) {
        Optional<Produto> produto = produtoService.buscarProdutoPorId(id); // Busca o produto pelo ID
        // Retorna o produto se encontrado, ou 404 se não encontrado
        return produto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
