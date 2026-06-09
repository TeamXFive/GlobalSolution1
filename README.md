# 🛰️ Tem Satélite Passando Por Mim Agora?

Projeto da **Global Solution FIAP — Indústria Espacial**.

Aplicativo que mostra ao usuário quais satélites estão passando (ou vão passar)
sobre sua localização e se as condições simuladas do céu são favoráveis para a
observação. O sistema é composto por **app mobile + API + banco de dados + IoT**.

---

## 📂 Estrutura do repositório

```
GS/
├── front-end-mobile/   # App Android (Kotlin + Jetpack Compose)
├── backend/            # API REST (Java 17 + Spring Boot 3)
├── database/           # Scripts SQL e diagrama ER (PostgreSQL)
├── iot/                # Simulador de sensor em Python
├── docs/               # Coleção do Postman
└── README.md           # Este arquivo
```

| Pasta | O que contém | Tecnologia |
|-------|--------------|-----------|
| [`front-end-mobile/`](front-end-mobile/) | App Android com 4 telas (Login, Home, Satélites, Alertas). Consome a API real via Retrofit (`data/remote/` + `data/Repository.kt`). | Kotlin, Jetpack Compose, Material 3, Navigation Compose |
| [`backend/`](backend/) | API REST em camadas **Controller → Service → Repository → Model**, com 13 endpoints, segurança (BCrypt, JWT, validação, proteção a SQL Injection) e Swagger. | Java 17, Spring Boot 3, JPA |
| [`database/`](database/) | `DIAGRAMA_ER.md` (diagrama ER), `01_schema.sql` (tabelas + PKs + FKs), `02_insert_dados.sql` (dados de exemplo), `03_consultas.sql` (consultas). | PostgreSQL |
| [`iot/`](iot/) | `simulador_sensor.py` — simula um sensor de clima e envia leituras para a API (`POST /leituras`). Sem hardware: os valores são gerados por código. | Python |
| [`docs/`](docs/) | `SatelliteTracker.postman_collection.json` — coleção para testar a API no Postman. | Postman / Swagger |

> **Como as partes se conectam:**
> ```
> [IoT / Python]  --POST /leituras-->  [Backend / API]  <--HTTP-->  [App Mobile]
>    (gera o clima)                      (regras + banco)            (mostra ao usuário)
> ```

---

## ▶️ Como rodar

### 1. Backend / API (comece por aqui)

API em Java 17 + Spring Boot. Sobe com **banco H2 em memória já populado** —
não precisa instalar banco nenhum. Na pasta `backend/`:

```bash
cd backend
./mvnw spring-boot:run        # Linux/Mac  (Windows: .\mvnw.cmd spring-boot:run)
```

- **API:** http://localhost:8080
- **Swagger (documentação):** http://localhost:8080/swagger-ui.html
- **Console do H2:** http://localhost:8080/h2-console
  (JDBC URL: `jdbc:h2:mem:satellitedb`, usuário `sa`, sem senha)

> 💡 **Windows (este PC):** o `java` não está no PATH. Antes do comando, rode:
> `$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"`

### 2. Mobile / App

Precisa do **backend no ar** (passo 1).

1. Abra a pasta `front-end-mobile/` no **Android Studio**.
2. Aguarde o Gradle sincronizar.
3. Rode no **emulador** (botão ▶ Run).
4. Faça login com as credenciais de teste (abaixo).

> A URL da API fica em `data/remote/ApiConfig.kt`. Para o emulador já vem
> configurada como `http://10.0.2.2:8080/` (o "localhost" do PC visto pelo emulador).

### 3. IoT (opcional)

Simulador de sensor que alimenta a API. Na pasta `iot/`:

```bash
cd iot
pip install requests
python simulador_sensor.py          # envia uma leitura a cada 5s
python simulador_sensor.py --once   # envia apenas uma leitura
```

> Sem a API no ar, o script apenas imprime o JSON gerado (modo mock).

### Banco PostgreSQL (alternativa ao H2)

O backend roda com H2 por padrão. Para usar PostgreSQL real:

```bash
createdb satellitedb
psql -d satellitedb -f database/01_schema.sql
psql -d satellitedb -f database/02_insert_dados.sql
# ajuste usuário/senha em backend/src/main/resources/application-postgres.properties
cd backend && ./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

---

## 🔑 Acesso de teste

| Campo | Valor |
|-------|-------|
| Email | `teste@email.com` |
| Senha | `123456` |

Funciona tanto no app mobile quanto no `POST /login` da API.
