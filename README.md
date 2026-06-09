# 🛰️ Tem Satélite Passando Por Mim Agora?

## ⚡ Como rodar tudo (início rápido)

**1. Backend (API)** — abra um PowerShell na pasta `backend/` e rode:

```powershell
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"   # Java do Android Studio
.\mvnw.cmd spring-boot:run                                        # baixa o Maven e sobe a API
```

> Sobe em **http://localhost:8080** com banco H2 em memória (já populado) — não precisa instalar nada.
> Swagger: http://localhost:8080/swagger-ui.html · Login de teste: `teste@email.com` / `123456`.
> Se já existe o JAR compilado, dá pra subir mais rápido sem recompilar:
> `& "$env:JAVA_HOME\bin\java.exe" -jar "target\satellite-tracker-api-1.0.0.jar"`.
> Erro `Port 8080 already in use`? Veja [a seção do backend](#-2-backend--api-backend).

**2. Mobile (app)** — abra a pasta `front-end-mobile/` no **Android Studio**, espere o Gradle
sincronizar e clique em ▶ Run (emulador). Faça login com `teste@email.com` / `123456`.
*(Precisa do backend no ar — passo 1.)*

**3. IoT (opcional)** — na pasta `iot/`: `pip install requests` e `python simulador_sensor.py`.

> Detalhes de cada parte (PostgreSQL, endpoints, etc.) estão nas seções abaixo.

---

Projeto da **Global Solution FIAP — Indústria Espacial**.

Aplicativo que mostra ao usuário quais satélites estão passando (ou vão passar)
sobre sua localização e se as condições simuladas do céu são favoráveis para
observação.

O projeto é composto por **4 partes**:

| Parte | Tecnologia | Pasta |
|-------|-----------|-------|
| 📱 Mobile | Kotlin + Jetpack Compose + Material 3 | [`front-end-mobile/`](front-end-mobile/) |
| ⚙️ Backend / API | Java Spring Boot (Controller/Service/Repository) | [`backend/`](backend/) |
| 🗄️ Banco de Dados | PostgreSQL (scripts .sql + diagrama ER) | [`database/`](database/) |
| 📡 IoT | Python (simulador de sensores) | [`iot/`](iot/) |
| 📚 Documentação | Swagger + Postman | [`docs/`](docs/) |

---

## 🔑 Acesso de teste (login)

| Campo | Valor |
|-------|-------|
| Email | `teste@email.com` |
| Senha | `123456` |

Funciona tanto no app mobile quanto no `POST /login` da API.

---

## 📱 1. Mobile (`front-end-mobile/`)

App Android nativo, sem XML de layout, com 4 telas. **Consome a API real do
backend** via Retrofit (camada `data/remote` + `data/Repository.kt`):

- **LoginScreen** — valida o formato e autentica no `POST /login`.
- **HomeScreen** — próxima passagem (`GET /passagens`) e condição do céu (`GET /leituras`).
- **SatellitesScreen** — lista de satélites do `GET /satelites`, com busca por nome.
- **AlertsScreen** — sensores (`GET /leituras`) + alerta ativo (`GET /alertas`).

Cada tela mostra estados de **carregando**, **erro (com “Tentar novamente”)** e dados.

### ⚠️ Antes de rodar: suba o backend
O app precisa do backend no ar (ver seção 2). A URL fica em
`data/remote/ApiConfig.kt`:
- **Emulador Android:** `http://10.0.2.2:8080/` (já configurado — `10.0.2.2` é o "localhost" do PC visto pelo emulador).
- **Celular físico:** troque para `http://SEU_IP_LOCAL:8080/` (ex.: `http://192.168.0.10:8080/`).

### Como rodar
1. Suba o backend (`cd GS/backend && ./mvnw spring-boot:run`).
2. Abra a pasta `GS/front-end-mobile/` no **Android Studio**.
3. Aguarde o Gradle sincronizar.
4. Rode no **emulador** (botão ▶ Run).
5. Faça login com `teste@email.com` / `123456`.

---

## ⚙️ 2. Backend / API (`backend/`)

API REST em **Java 17 + Spring Boot 3**, organizada em camadas
**Controller → Service → Repository → Model**, com:

- 🔒 **BCrypt** (`BCryptPasswordEncoder`) — senhas nunca em texto puro.
- 🔑 **JWT** (opcional) — token devolvido no login.
- ✅ **Validação de entrada** (`@NotBlank`, `@Email`, `@Size`...).
- 🛡️ **Proteção contra SQL Injection** — JPA usa consultas parametrizadas.
- 📚 **Swagger** — documentação automática.

### Endpoints

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/satelites` | Lista satélites (`?nome=` filtra) |
| GET | `/satelites/{id}` | Busca satélite por id |
| GET | `/passagens` | Lista passagens (`?localizacaoId=` filtra) |
| POST | `/usuarios` | Cria usuário (hash BCrypt) |
| POST | `/login` | Autentica e devolve token JWT |
| GET | `/localizacoes` | Lista localizações (`?usuarioId=`) |
| POST | `/localizacoes` | Cria localização |
| GET | `/alertas` | Lista alertas (`?usuarioId=&apenasAtivos=`) |
| POST | `/alertas` | Cria alerta |
| PUT | `/alertas/{id}` | Atualiza alerta |
| DELETE | `/alertas/{id}` | Remove alerta |
| GET | `/leituras` | Lista leituras IoT (`?localizacaoId=`) |
| POST | `/leituras` | Registra leitura (usado pelo IoT) |

### Como rodar (modo fácil, sem instalar banco — H2 em memória)

```bash
cd GS/backend
./mvnw spring-boot:run      # ou:  mvn spring-boot:run
```

> Já vem populado com os dados de exemplo (satélites, Niterói, passagens etc.).

- API:     http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html
- H2 web:  http://localhost:8080/h2-console
  (JDBC URL: `jdbc:h2:mem:satellitedb`, user `sa`, sem senha)

### Como rodar com PostgreSQL

1. Crie o banco e rode os scripts da pasta [`database/`](database/) (ver abaixo).
2. Ajuste usuário/senha em `src/main/resources/application-postgres.properties`.
3. Suba com o perfil `postgres`:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

---

## 🗄️ 3. Banco de Dados (`database/`)

Banco **PostgreSQL** com 6 entidades:
`usuarios`, `localizacoes`, `satelites`, `passagens`, `alertas`, `leituras_sensor`.

| Arquivo | Conteúdo |
|---------|----------|
| `DIAGRAMA_ER.md` | Diagrama ER (Mermaid + ASCII) |
| `01_schema.sql` | `CREATE TABLE` + PKs + FKs + índices |
| `02_insert_dados.sql` | `INSERT` com dados de exemplo |
| `03_consultas.sql` | Consultas SQL de simulação |

### Como executar os scripts

```bash
createdb satellitedb
psql -d satellitedb -f database/01_schema.sql
psql -d satellitedb -f database/02_insert_dados.sql
psql -d satellitedb -f database/03_consultas.sql
```

### Relacionamentos
- Usuário **1:N** Localização
- Localização **1:N** Passagem
- Satélite **1:N** Passagem
- Usuário **1:N** Alerta
- Localização **1:N** LeituraSensor

---

## 📡 4. IoT (`iot/`)

### O que é, na prática

Um **simulador de sensor escrito em Python** (`iot/simulador_sensor.py`) — **não há
hardware nenhum** (nenhum Arduino, ESP32 ou sensor físico). Ele *finge* ser um
dispositivo IoT instalado numa localização (Niterói, no seed) que mede as condições
do céu. É exatamente o padrão de um dispositivo IoT real — só que os números são
gerados por código em vez de virem de um sensor de verdade.

No mundo real seria um sensorzinho no quintal mandando o clima para a nuvem a cada
poucos segundos. Aqui, para fins acadêmicos, um **simulador em software** produz os
mesmos dados — o suficiente para demonstrar o fluxo **IoT → API → App** sem precisar
montar hardware.

### O que o script faz

1. **Gera uma leitura simulada** com valores aleatórios plausíveis: temperatura
   (15–32 °C), se está chovendo (30% de chance), nebulosidade (0–100%) e
   luminosidade / poluição luminosa (0–100%).
2. **Envia para o backend** via `POST /leituras` — o mesmo endpoint que o app
   mobile depois lê para mostrar os alertas.
3. **Repete a cada 5 segundos**, como um sensor real ficaria mandando dados
   continuamente (ou `--once` para enviar só uma vez).
4. **Modo "mock" de segurança**: se a API estiver fora do ar (ou faltar a lib
   `requests`), ele apenas **imprime o JSON na tela** em vez de quebrar.

### Como ele se encaixa no projeto

```
[Simulador IoT/Python]  --POST /leituras-->  [Backend/API]  <--GET /leituras--  [App Mobile]
   (inventa o clima)                          (salva no banco)                  (mostra alertas)
```

A API aplica a mesma regra de observação do app:
> **favorável** = não chove **E** nebulosidade < 40% **E** luminosidade < 50%

### Como rodar
```bash
cd GS/iot
pip install requests
python simulador_sensor.py          # envia leituras a cada 5s
python simulador_sensor.py --once   # envia uma única leitura
```

> Sem a API no ar, o script ainda imprime o JSON gerado (modo mock).
> Há também um exemplo estático em `iot/leitura_mockada.json`.

---

## 📚 5. Documentação e Testes (`docs/`)

- **Swagger UI**: http://localhost:8080/swagger-ui.html (com a API no ar).
- **Postman**: importe `docs/SatelliteTracker.postman_collection.json`.

---

## ✅ Checklist de requisitos atendidos

- [x] Mobile (Kotlin, Compose, Material 3, Navigation) **consumindo a API real** (Retrofit)
- [x] Backend Java Spring Boot com camadas Controller/Service/Repository
- [x] ≥ 5 endpoints (foram criados 13)
- [x] Banco PostgreSQL: diagrama ER, CREATE TABLE, PKs, FKs, INSERTs, consultas
- [x] Segurança: BCrypt, JWT (opcional), validação de entrada, proteção SQL Injection
- [x] IoT em Python + JSON mockado
- [x] Documentação: Swagger + Postman
