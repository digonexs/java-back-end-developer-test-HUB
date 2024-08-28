package br.com.aplicacao.cupom_system.controller;

import br.com.aplicacao.cupom_system.model.Cupom;
import br.com.aplicacao.cupom_system.model.Produto;
import br.com.aplicacao.cupom_system.model.ProdutoDetalhes;
import br.com.aplicacao.cupom_system.service.CupomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cupons")
public class CupomController {

    @Autowired
    private CupomService cupomService; // Injeta o serviço CupomService para realizar operações de negócio com cupons.

    /**
     * Endpoint para criar um novo cupom.
     * @param cupom Objeto Cupom recebido no corpo da requisição, validado automaticamente.
     * @return ResponseEntity com o cupom criado ou mensagem de erro.
     */
    @PostMapping
    public ResponseEntity<?> criarCupom(@Valid @RequestBody Cupom cupom) {
        try {
            // Associa cada produto ao cupom para garantir a consistência de dados
            if (cupom.getProdutos() != null) {
                for (Produto produto : cupom.getProdutos()) {
                    produto.setCupom(cupom); // Define o cupom para cada produto
                }
            }

            // Salva o cupom usando o serviço e retorna o cupom criado
            Cupom novoCupom = cupomService.criarCupom(cupom);
            return ResponseEntity.ok(novoCupom);
        } catch (MethodArgumentNotValidException e) {
            // Tratamento específico para erros de validação, retorna uma mensagem detalhada
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getBindingResult().toString());
        } catch (IllegalArgumentException e) {
            // Tratamento para argumentos ilegais, retorna mensagem de erro
            return ResponseEntity.badRequest().body("Argumento ilegal: " + e.getMessage());
        } catch (Exception e) {
            // Captura outras exceções gerais e retorna uma mensagem de erro genérica
            return ResponseEntity.badRequest().body("Erro ao processar a requisição: " + e.getMessage());
        }
    }

    /**
     * Endpoint para listar todos os cupons.
     * @return ResponseEntity com a lista de cupons.
     */
    @GetMapping
    public ResponseEntity<List<Cupom>> listarCupons() {
        List<Cupom> cupons = cupomService.listarCupons(); // Obtém a lista de cupons usando o serviço
        return ResponseEntity.ok(cupons); // Retorna a lista de cupons
    }

    /**
     * Endpoint para buscar um cupom específico por ID.
     * @param id ID do cupom a ser buscado.
     * @return ResponseEntity com o cupom encontrado ou resposta 404 se não encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cupom> buscarCupomPorId(@PathVariable Long id) {
        Optional<Cupom> cupom = cupomService.buscarCupomPorId(id); // Busca o cupom pelo ID
        // Retorna o cupom se encontrado, ou 404 se não encontrado
        return cupom.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para buscar um cupom usando um código específico (code44).
     * @param code44 Código específico do cupom.
     * @return ResponseEntity com detalhes dos produtos ou mensagem de erro.
     */
    @GetMapping("/code44/{code44}")
    public ResponseEntity<?> buscarCupomPorCode44(@PathVariable String code44) {
        Optional<Cupom> cupomOpt = cupomService.buscarCupomPorCode44(code44); // Busca o cupom pelo código fornecido

        if (cupomOpt.isPresent()) {
            Cupom cupom = cupomOpt.get(); // Obtém o cupom encontrado
            List<Produto> produtos = cupom.getProdutos(); // Obtém a lista de produtos associados ao cupom

            if (produtos.isEmpty()) {
                // Retorna uma mensagem de erro se não houver produtos associados ao cupom
                return ResponseEntity.badRequest().body("Nenhum produto encontrado para o cupom fornecido.");
            }

            // Calcula o preço mínimo e máximo dos produtos associados ao cupom
            double minPrice = produtos.stream().mapToDouble(Produto::getUnitaryPrice).min().orElse(0);
            double maxPrice = produtos.stream().mapToDouble(Produto::getUnitaryPrice).max().orElse(0);

            // Cria uma lista de detalhes dos produtos com os preços mínimos e máximos calculados
            List<ProdutoDetalhes> detalhesProdutos = produtos.stream()
                    .map(produto -> new ProdutoDetalhes(produto.getEan(), produto.getName(), minPrice, maxPrice))
                    .toList();

            // Retorna a lista de detalhes dos produtos
            return ResponseEntity.ok(detalhesProdutos);
        } else {
            // Retorna 404 se o cupom não for encontrado
            return ResponseEntity.notFound().build();
        }
    }
}
