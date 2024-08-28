package br.com.aplicacao.cupom_system.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Nome da fila que será usada para processamento de cupons
    public static final String QUEUE_NAME = "cuponsQueue";
    // Nome da exchange do tipo Topic que será usada para rotear mensagens
    public static final String EXCHANGE_NAME = "cuponsExchange";
    // Chave de roteamento usada para associar a fila à exchange
    public static final String ROUTING_KEY = "cupons.routingkey";

    /**
     * Define a fila do RabbitMQ.
     * @return A fila configurada com o nome especificado.
     * O segundo argumento 'true' indica que a fila é durável (persiste entre reinicializações do servidor).
     */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true);
    }

    /**
     * Define a exchange do tipo Topic para o RabbitMQ.
     * @return A exchange configurada com o nome especificado.
     * Exchanges do tipo Topic permitem roteamento flexível de mensagens usando padrões de chave.
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    /**
     * Define o binding entre a fila e a exchange com a chave de roteamento.
     * @param queue A fila que será ligada à exchange.
     * @param exchange A exchange à qual a fila será ligada.
     * @return O binding configurado para conectar a fila e a exchange usando a chave de roteamento especificada.
     * Este binding permite que mensagens publicadas na exchange com a chave de roteamento especificada
     * sejam entregues à fila.
     */
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
}
