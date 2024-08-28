package br.com.aplicacao.cupom_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação CupomSystem.
 * Esta classe inicializa a aplicação Spring Boot, configurando automaticamente os componentes e as dependências.
 */
@SpringBootApplication
public class CupomSystemApplication {

	/**
	 * O método main é o ponto de entrada da aplicação.
	 * Ele usa o SpringApplication.run para iniciar o contexto da aplicação Spring, configurando automaticamente os componentes e iniciando a aplicação.
	 *
	 * @param args Argumentos de linha de comando (não utilizados nesta aplicação).
	 */
	public static void main(String[] args) {
		SpringApplication.run(CupomSystemApplication.class, args);
	}

}
