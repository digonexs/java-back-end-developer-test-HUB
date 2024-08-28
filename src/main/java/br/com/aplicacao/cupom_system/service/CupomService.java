package br.com.aplicacao.cupom_system.service;

import br.com.aplicacao.cupom_system.model.Cupom;
import br.com.aplicacao.cupom_system.repository.CupomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service // Indica que esta classe é um serviço Spring, que contém a lógica de negócios.
public class CupomService {

    @Autowired
    private CupomRepository cupomRepository; // Repositório para realizar operações de persistência com cupons.

    @Autowired
    private RabbitMQSender rabbitMQSender; // Componente para enviar mensagens usando RabbitMQ.

    /**
     * Cria um novo cupom após realizar validações necessárias.
     * Envia uma mensagem para o RabbitMQ com detalhes do cupom após a criação.
     *
     * @param cupom O objeto Cupom a ser criado.
     * @return O cupom salvo no banco de dados.
     */
    @Transactional // Garante que todas as operações dentro deste método sejam tratadas como uma única transação.
    public Cupom criarCupom(Cupom cupom) {
        // Validação do campo code44: deve conter exatamente 44 dígitos numéricos.
        if (!cupom.getCode44().matches("\\d{44}")) {
            throw new IllegalArgumentException("code44 deve ter 44 dígitos.");
        }

        // Validação do CNPJ da empresa: verifica se o formato do CNPJ é válido.
        if (!validaCNPJ(cupom.getCompanyDocument())) {
            throw new IllegalArgumentException("CNPJ inválido.");
        }

        // Verificar se a lista de produtos não é nula ou vazia.
        // A lista de produtos deve existir e conter pelo menos um produto.
        if (cupom.getProdutos() == null || cupom.getProdutos().isEmpty()) {
            throw new IllegalArgumentException("A lista de produtos não pode ser nula ou vazia.");
        }

        // Validação do valor total: verifica se o valor total informado é igual à soma dos preços dos produtos.
        double somaProdutos = cupom.getProdutos().stream()
                .mapToDouble(p -> p.getUnitaryPrice() * p.getQuantity()) // Calcula o preço total de cada produto.
                .sum(); // Soma os preços totais de todos os produtos.

        // Se o valor total não corresponder à soma dos valores dos produtos, lança uma exceção.
        if (Double.compare(somaProdutos, cupom.getTotalValue()) != 0) {
            throw new IllegalArgumentException("Total value não corresponde à soma dos valores dos produtos.");
        }

        // Salva o cupom validado no banco de dados e retorna o cupom salvo.
        Cupom salvo = cupomRepository.save(cupom);

        // Prepara um mapa de dados para enviar como mensagem ao RabbitMQ.
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("couponId", salvo.getId());
        messageData.put("buyerName", "John Doe"); // Exemplo de dado, substituir por dados reais se necessário.
        messageData.put("buyerBirthDate", "1990-01-01"); // Exemplo de dado.
        messageData.put("buyerDocument", "12345678900"); // Exemplo de dado.

        // Envia a mensagem contendo detalhes do cupom usando o RabbitMQ.
        rabbitMQSender.sendMessage(messageData);

        return salvo; // Retorna o cupom salvo após todas as operações.
    }

    /**
     * Retorna uma lista de todos os cupons salvos no banco de dados.
     *
     * @return Lista de cupons.
     */
    public List<Cupom> listarCupons() {
        return cupomRepository.findAll(); // Consulta e retorna todos os cupons do repositório.
    }

    /**
     * Busca um cupom pelo seu ID.
     *
     * @param id O ID do cupom a ser buscado.
     * @return Um Optional contendo o cupom encontrado, ou vazio se não for encontrado.
     */
    public Optional<Cupom> buscarCupomPorId(Long id) {
        return cupomRepository.findById(id); // Busca um cupom pelo seu ID no repositório.
    }

    /**
     * Busca um cupom pelo campo 'code44'.
     *
     * @param code44 O código de 44 dígitos do cupom.
     * @return Um Optional contendo o cupom encontrado, ou vazio se não for encontrado.
     */
    public Optional<Cupom> buscarCupomPorCode44(String code44) {
        return cupomRepository.findByCode44(code44); // Busca um cupom pelo código de 44 dígitos.
    }

    /**
     * Valida o CNPJ usando regras específicas de validação.
     *
     * @param cnpj O CNPJ a ser validado.
     * @return true se o CNPJ for válido, caso contrário false.
     */
    private boolean validaCNPJ(String cnpj) {
        // Remove todos os caracteres não numéricos.
        cnpj = cnpj.replaceAll("\\D", "");

        // Verifica se o CNPJ tem exatamente 14 dígitos.
        if (cnpj.length() != 14) {
            return false;
        }

        // Verifica se todos os dígitos são iguais, o que indicaria um CNPJ inválido.
        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        // Verifica o primeiro dígito verificador.
        int digito1 = calcularDigitoVerificador(cnpj, 12);
        if (digito1 != Character.getNumericValue(cnpj.charAt(12))) {
            return false;
        }

        // Verifica o segundo dígito verificador.
        int digito2 = calcularDigitoVerificador(cnpj, 13);
        return digito2 == Character.getNumericValue(cnpj.charAt(13));
    }

    /**
     * Calcula o dígito verificador do CNPJ.
     *
     * @param cnpj O CNPJ para calcular o dígito verificador.
     * @param tamanho O tamanho da parte do CNPJ a ser usada no cálculo.
     * @return O dígito verificador calculado.
     */
    private int calcularDigitoVerificador(String cnpj, int tamanho) {
        // Pesos para multiplicação dos dígitos do CNPJ.
        int[] pesos = (tamanho == 12) ? new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2}
                : new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma = 0;

        // Multiplica cada dígito do CNPJ pelo seu peso correspondente e soma os resultados.
        for (int i = 0; i < tamanho; i++) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * pesos[i];
        }

        // Calcula o dígito verificador com base na soma total.
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto; // Retorna o dígito verificador.
    }
}
