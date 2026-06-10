# Backend — API "Tem Satélite Passando Por Mim Agora?"

Java 17 + Spring Boot 3 · Camadas Controller → Service → Repository → Model.

## Rodar (H2 em memória, sem instalar banco)

```bash
./mvnw spring-boot:run        # Linux/Mac
mvnw.cmd spring-boot:run      # Windows
```

- API:     http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html
- H2:      http://localhost:8080/h2-console  (`jdbc:h2:mem:satellitedb`, user `sa`)

## Rodar com PostgreSQL

Ajuste `src/main/resources/application-postgres.properties` e:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

## Testes

Os testes ficam em `src/test/java` e cobrem cenários das regras de negócio com JUnit 5 + Mockito:

- criação de usuário com senha criptografada;
- bloqueio de e-mail duplicado;
- login com senha inválida;
- criação de alerta ativo por padrão;
- cálculo de observação favorável na leitura do sensor.

Para executar:

```bash
./mvnw test        # Linux/Mac
mvnw.cmd test      # Windows
```

## Login de teste
`teste@email.com` / `123456`

## Estrutura
```
controller/   -> recebe as requisições HTTP (REST)
service/      -> regras de negócio (login, validações, cálculo IoT)
repository/   -> acesso ao banco (Spring Data JPA, consultas parametrizadas)
model/        -> entidades JPA (tabelas)
dto/          -> objetos de entrada/saída (com validação)
config/       -> Security (BCrypt), Swagger, seed de dados
security/     -> geração de token JWT
exception/    -> tratamento global de erros
```

## Segurança
- **BCrypt** para senhas (`SecurityConfig` + `UsuarioService`).
- **JWT** opcional devolvido no `POST /login` (`JwtService`).
- **Validação** de entrada com Bean Validation (`dto/*Request`).
- **SQL Injection**: prevenido pelo uso de JPA (consultas parametrizadas).
