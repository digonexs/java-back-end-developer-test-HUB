package br.com.aplicacao.cupom_system.service;

import br.com.aplicacao.cupom_system.model.Cupom;
import br.com.aplicacao.cupom_system.model.Produto;
import br.com.aplicacao.cupom_system.repository.CupomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CupomServiceTest {

    @Mock
    private CupomRepository cupomRepository; // Mock do repositório de cupons para simular operações de banco de dados.

    @Mock
    private RabbitMQSender rabbitMQSender; // Mock do RabbitMQSender para simular o envio de mensagens.

    @InjectMocks
    private CupomService cupomService; // Instância da classe sendo testada, com mocks injetados.

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Abre os mocks para a inicialização.
    }

    @Test
    void criarCupom_DeveLancarExcecao_SeCode44Invalido() {
        Cupom cupom = new Cupom();
        cupom.setCode44("123"); // Código inválido com menos de 44 dígitos

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cupomService.criarCupom(cupom);
        });

        assertEquals("code44 deve ter 44 dígitos.", exception.getMessage());
    }

    @Test
    void criarCupom_DeveLancarExcecao_SeCNPJInvalido() {
        Cupom cupom = new Cupom();
        cupom.setCode44("12345678901234567890123456789012345678901234"); // Código válido
        cupom.setCompanyDocument("12345678901234"); // CNPJ inválido

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cupomService.criarCupom(cupom);
        });

        assertEquals("CNPJ inválido.", exception.getMessage());
    }

    @Test
    void criarCupom_DeveSalvarCupom_SeDadosValidos() {
        Cupom cupom = new Cupom();
        cupom.setCode44("12345678901234567890123456789012345678901234");
        cupom.setCompanyDocument("12345678000195");
        cupom.setTotalValue(100.0);

        // Inicializa a lista de produtos
        Produto produto = new Produto();
        produto.setUnitaryPrice(50.0);
        produto.setQuantity(2); // Preço total será 100.0, o que é igual ao totalValue do cupom
        cupom.setProdutos(List.of(produto));

        when(cupomRepository.save(any(Cupom.class))).thenReturn(cupom);

        Cupom resultado = cupomService.criarCupom(cupom);

        assertNotNull(resultado);
        verify(cupomRepository, times(1)).save(cupom);
        verify(rabbitMQSender, times(1)).sendMessage(any());
    }

    @Test
    void buscarCupomPorId_DeveRetornarCupom_SeIdExistir() {
        Long id = 1L;
        Cupom cupom = new Cupom();
        cupom.setId(id);

        when(cupomRepository.findById(id)).thenReturn(Optional.of(cupom));

        Optional<Cupom> resultado = cupomService.buscarCupomPorId(id);

        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
    }

    @Test
    void buscarCupomPorId_DeveRetornarVazio_SeIdNaoExistir() {
        Long id = 1L;

        when(cupomRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Cupom> resultado = cupomService.buscarCupomPorId(id);

        assertFalse(resultado.isPresent());
    }
}
