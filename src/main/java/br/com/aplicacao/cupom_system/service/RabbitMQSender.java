package br.com.aplicacao.cupom_system.service;

import br.com.aplicacao.cupom_system.config.RabbitMQConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * A classe RabbitMQSender é responsável por enviar mensagens para o RabbitMQ.
 * Ela converte os dados em formato JSON e utiliza o RabbitTemplate para enviar as mensagens para a exchange configurada.
 */
@Service
public class RabbitMQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate; // Ferramenta principal para enviar mensagens para o RabbitMQ.

    @Autowired
    private ObjectMapper objectMapper; // Utilizado para converter objetos Java em strings JSON.

    /**
     * Envia uma mensagem para o RabbitMQ.
     *
     * @param messageData Um Map contendo os dados da mensagem que serão enviados.
     */
    public void sendMessage(Map<String, Object> messageData) {
        try {
            // Converter o Map para uma string JSON usando o ObjectMapper.
            String message = objectMapper.writeValueAsString(messageData);

            // Envia a mensagem JSON para a exchange especificada com a chave de roteamento configurada.
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, message);

            // Imprime no console a mensagem enviada.
            System.out.println("Mensagem enviada para RabbitMQ: " + message);
        } catch (JsonProcessingException e) {
            // Trata exceções que ocorrem ao converter o Map em JSON.
            System.err.println("Erro ao converter mensagem para JSON: " + e.getMessage());
        }
    }
}
