package com.manager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
//permite configurar o broker de mensagens e os endpoints


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
    //Ativa um broker embutido (simples) que permite enviar mensagens para destinos com prefixo /topic.
    //Os métodos convertAndSend("/topic/...") enviarão para todos os clientes conectados a esse canal.

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }
    //Cria o endpoint WebSocket em /ws, que os clientes devem se conectar.
    // "*" Permite conexões de qualquer origem (CORS)
    //Habilita fallback para SockJS, que permite compatibilidade com navegadores que não suportam WebSocket nativo 
}
