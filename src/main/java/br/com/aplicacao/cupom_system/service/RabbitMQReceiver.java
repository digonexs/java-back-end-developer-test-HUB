package br.com.aplicacao.cupom_system.service;

import br.com.aplicacao.cupom_system.config.RabbitMQConfig;
import br.com.aplicacao.cupom_system.model.Cupom;
import br.com.aplicacao.cupom_system.repository.CupomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

/**
 * A classe RabbitMQReceiver é responsável por receber e processar mensagens da fila RabbitMQ.
 * Ela escuta mensagens enviadas para a fila configurada e atualiza informações de cupons com base nos dados recebidos.
 */
@Component
public class RabbitMQReceiver {

    @Autowired
    private CupomRepository cupomRepository; // Repositório para operações de persistência com cupons.

    @Autowired
    private ObjectMapper objectMapper; // Utilizado para deserializar mensagens JSON em objetos Java.

    /**
     * Método que escuta a fila RabbitMQ especificada e recebe mensagens.
     *
     * @param message A mensagem recebida da fila RabbitMQ.
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(String message) {
        System.out.println("Mensagem recebida do RabbitMQ: " + message);
        processarMensagem(message); // Processa a mensagem recebida.
    }

    /**
     * Processa a mensagem recebida, deserializando-a e atualizando o cupom correspondente no banco de dados.
     *
     * @param message A mensagem JSON recebida contendo os dados do cupom.
     */
    private void processarMensagem(String message) {
        try {
            // Supõe que a mensagem seja um JSON com os campos: couponId, buyerName, buyerBirthDate, e buyerDocument.
            CupomData cupomData = objectMapper.readValue(message, CupomData.class);

            // Busca o cupom no banco de dados pelo couponId fornecido na mensagem.
            Optional<Cupom> cupomOpt = cupomRepository.findById(cupomData.getCouponId());
            if (cupomOpt.isPresent()) {
                Cupom cupom = cupomOpt.get();
                // Atualiza os dados do comprador no cupom.
                cupom.setBuyerName(cupomData.getBuyerName());
                cupom.setBuyerBirthDate(cupomData.getBuyerBirthDate());
                cupom.setBuyerDocument(cupomData.getBuyerDocument());

                // Salva as atualizações no banco de dados.
                cupomRepository.save(cupom);
                System.out.println("Cupom atualizado com sucesso: " + cupom);
            } else {
                System.out.println("Cupom com ID " + cupomData.getCouponId() + " não encontrado.");
            }
        } catch (Exception e) {
            // Captura e trata qualquer exceção que ocorra durante o processamento da mensagem.
            System.err.println("Erro ao processar mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Classe auxiliar para mapear os dados da mensagem JSON em um objeto Java.
     * Esta classe representa os campos esperados na mensagem recebida do RabbitMQ.
     */
    private static class CupomData {
        private Long couponId; // ID do cupom.
        private String buyerName; // Nome do comprador.
        private LocalDate buyerBirthDate; // Data de nascimento do comprador.
        private String buyerDocument; // Documento do comprador.

        // Getters e Setters para os campos da classe CupomData.

        public Long getCouponId() {
            return couponId;
        }

        public void setCouponId(Long couponId) {
            this.couponId = couponId;
        }

        public String getBuyerName() {
            return buyerName;
        }

        public void setBuyerName(String buyerName) {
            this.buyerName = buyerName;
        }

        public LocalDate getBuyerBirthDate() {
            return buyerBirthDate;
        }

        public void setBuyerBirthDate(LocalDate buyerBirthDate) {
            this.buyerBirthDate = buyerBirthDate;
        }

        public String getBuyerDocument() {
            return buyerDocument;
        }

        public void setBuyerDocument(String buyerDocument) {
            this.buyerDocument = buyerDocument;
        }
    }
}
