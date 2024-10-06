## 🐦 Twitter Clone
Um clone do Twitter - Uma aplicação de rede social que permite aos usuários se conectarem e compartilharem seus pensamentos e experiências de maneira fácil e intuitiva! 🚀 Com uma arquitetura robusta e tecnologias modernas, este projeto oferece uma experiência de usuário rápida, segura e escalável.

* 📜 Índice
* 🚀 Visão Geral
* 🛠️ Tecnologias Utilizadas
* 🖼️ Diagrama de Arquitetura
* 🔧 Ferramentas Adicionais
* 💼 Serviços Principais
* 🔑 Serviço de Autenticação
* 💾 Serviço de Armazenamento
* 👤 Serviço de Perfil
* 💬 Serviço de Tweets
* ⏳ Serviço de Timeline
* 💡 Serviços Auxiliares
* 📊 Cobertura de Testes
* 💻 Como Rodar Localmente
* 🎯 Contribuições
* 📞 Contato
* 🚀 Visão Geral

Este projeto é um clone do Twitter, desenvolvido com uma arquitetura de microsserviços utilizando o framework Spring. Ele segue uma abordagem modular, garantindo escalabilidade, facilidade de manutenção e um desempenho robusto, mesmo em situações de grande tráfego.

## 🛠️ Tecnologias Utilizadas
* Java 17 ☕️
* Spring Boot 3.0.5 ⚡
* Spring Web
* Spring Security 🔐
* Spring Data JPA
* Spring Cloud
* Redis 🧑‍🔧
* MongoDB 🗄️
* PostgreSQL 🐘

## 🔧 Ferramentas Adicionais
* Apache Kafka 📡 (mensageria em tempo real)
* Armazenamento AWS S3 ☁️ (armazenamento de mídia)
* OpenFeign (chamadas entre microsserviços simplificadas)
* Discovery Server (Eureka) 🔍
* Spring API Gateway 🌐 (roteamento centralizado)
* Rastreamento Distribuído com Zipkin 🕵️
* Spring Cloud Config Server ⚙️ (gerenciamento de configurações)
* MapStruct (mapeamento de objetos)
* Swagger 📄 (documentação de APIs)
* Testcontainers (testes integrados com containers Docker)
* Mockito (testes unitários)
* 💼 Serviços Principais
* 🔑 Serviço de Autenticação

Responsável pelo registro de usuários, autenticação e geração de tokens JWT que expiram em 24 horas. O usuário pode se autenticar usando e-mail e senha, e após a ativação da conta, recebe o token JWT.

## 💾 Serviço de Armazenamento
Este serviço gerencia o armazenamento de arquivos multimídia no AWS S3, permitindo o upload e download de imagens, vídeos e outros arquivos.

## 👤 Serviço de Perfil
Permite que os usuários personalizem seus perfis, façam upload de avatares e banners. O serviço usa caching para melhorar o desempenho, evitando consultas repetidas ao banco de dados.

##  💬 Serviço de Tweets
Gerencia a criação, atualização e exclusão de tweets, bem como funcionalidades de curtir, retweetar e responder. Também existe um sistema de visualizações para cada tweet e um cache para otimizar a experiência.

## ⏳ Serviço de Timeline
Oferece uma timeline personalizada para cada usuário, com base nos tweets dos perfis que ele segue. Utiliza cache para melhorar a velocidade e a eficiência no carregamento da timeline.

# 💡 Serviços Auxiliares

## 📡 Serviço de Fanout
Este serviço escuta mensagens da fila de mensagens e distribui dados para o cache dos usuários relevantes.

## 🌐 API Gateway
Roteia todas as requisições entre os microsserviços, garantindo uma comunicação segura usando tokens JWT.

## 🔍 Discovery Server e Load Balancer
Facilita a descoberta e comunicação entre os microsserviços, além de balancear a carga para otimizar o desempenho.

## ⚙️ Cloud Config Server
Centraliza e gerencia as configurações de todos os microsserviços em diferentes ambientes de produção, homologação e desenvolvimento.

## 🎯 Contribuições
Contribuições são bem-vindas! Se você encontrar algum bug ou quiser adicionar novas funcionalidades, sinta-se à vontade para abrir um Pull Request ou criar uma Issue.

Como Contribuir:
* Faça um fork deste repositório.
* Crie uma branch para sua funcionalidade: git checkout -b 
* feature/nova-funcionalidade.
* Faça suas alterações e adicione commits.
* Envie um Pull Request.

## 📞 Contato
Caso tenha dúvidas ou sugestões, entre em contato:

Email: ikauedeveloper@gmail.com
<br>
LinkedIn: https://www.linkedin.com/in/ikauematos/
