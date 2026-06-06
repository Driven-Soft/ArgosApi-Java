# Argos API

API REST para monitoramento de desastres naturais, desenvolvida com Java 21 e Spring Boot 4 como parte da Global Solution FIAP.

Integra dados de chuva em tempo real via **Open-Meteo** e imagens de satélite via **NASA Earth Imagery**.

---

## 👥 Integrantes

| Nome | RM |
|---|---|
| Felipe Bezerra Beatrici | RM 564723 |
| Max Hayashi Batista | RM 563717 |
| Henrique Cunha Torres | RM 565119 |

---

## 🔗 Links

| Recurso | URL |
|---|---|
| **Deploy (Render)** | https://argosapi-java.onrender.com/ |
| **Swagger UI** | https://argosapi-java.onrender.com/swagger-ui.html |
| **API Docs (JSON)** | https://argosapi-java.onrender.com/v3/api-docs |
| **Vídeo de Apresentação** | `https://youtube.com/` ← substituir |

---

## 🛠️ Tecnologias

- Java 21
- Spring Boot 4.0.6
- Spring Security + JWT (jjwt 0.11.5)
- Spring Data JPA + H2 (ambiente atual)
- Oracle Database (suportado para produção)
- Spring HATEOAS
- Springdoc OpenAPI 3 (Swagger)
- Lombok
- Docker

---

## 📐 Arquitetura

```
src/main/java/br/com/fiap/java/ArgosApi/
├── config/          # CORS, Swagger, DataInitializer
├── controller/      # Endpoints REST com HATEOAS
├── dto/             # Records de request e response
│   ├── request/
│   └── response/
├── entity/          # Entidades JPA
├── exception/       # GlobalExceptionHandler + ResourceNotFoundException
├── repository/      # Interfaces JpaRepository
├── security/        # JWT filter, SecurityConfig, UserDetailsService
└── service/         # Regras de negócio + integração com APIs externas
```

### 🗂️ Modelagem Avançada

| Requisito | Implementação |
|---|---|
| **Herança** | `AnaliseRiscoDetalhada extends AnaliseRisco` com `@Inheritance(SINGLE_TABLE)` e `@DiscriminatorValue` |
| **Chave Composta** | `AlertaZonaId` com `@EmbeddedId` (chave: `zonaRiscoId` + `nivelAlerta`) em `AlertaZona` |
| **Embedded** | `Localizacao` (`@Embeddable`) com latitude e longitude, usado em `ZonaRisco` e `Ocorrencia` com `@AttributeOverrides` |
| **Múltiplas Tabelas** | `usuarios`, `zonas_risco`, `alertas`, `ocorrencias`, `comentarios_ocorrencia`, `analises_risco`, `alertas_zona`, `tipos_ocorrencia` |

---

## 🔑 Autenticação

A API usa **JWT Bearer Token**.

1. Registre-se: `POST /api/auth/register`
2. Faça login: `POST /api/auth/login` → recebe o token
3. Use o token no header: `Authorization: Bearer <token>`

**Usuário admin padrão (seed automático):**
```
email:  admin@argos.com
senha:  admin123
```

---

## 🚀 Como executar localmente

### Pré-requisitos
- Java 21+
- Maven (ou use o `./mvnw` incluído)

### Executar
```bash
# Clone o repositório
git clone https://github.com/Driven-Soft/ArgosApi-Java.git
cd ArgosApi

# Defina as variáveis de ambiente (opcional em dev — há fallbacks)
export NASA_API_KEY=DEMO_KEY
export JWT_SECRET=meu_secret_local

# Execute
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`.
Swagger disponível em `http://localhost:8080/swagger-ui.html`.
H2 Console em `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:argosdb`).

### Docker
```bash
docker build -t argos-api .
docker run -p 8080:8080 \
  -e NASA_API_KEY=DEMO_KEY \
  -e JWT_SECRET=meu_secret \
  argos-api
```

---

## 📋 Endpoints

### Auth
| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/auth/register` | Registrar novo usuário |
| POST | `/api/auth/login` | Login → JWT token |

### Usuários
| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/usuarios/me` | Dados do usuário autenticado |
| PUT | `/api/usuarios/me` | Atualizar perfil |

### Zonas de Risco
| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/zonas` | Listar todas (HATEOAS) |
| GET | `/api/zonas/{id}` | Buscar por ID (HATEOAS) |
| POST | `/api/zonas` | Criar zona |
| PUT | `/api/zonas/{id}` | Atualizar zona |
| DELETE | `/api/zonas/{id}` | Deletar zona |
| PATCH | `/api/zonas/{id}/nivel-risco` | Atualizar nível manualmente |
| POST | `/api/zonas/{id}/analisar-risco` | Análise via Open-Meteo |
| GET | `/api/zonas/{id}/analises` | Histórico de análises |
| GET | `/api/zonas/dashboard/{id}` | Dashboard: chuva + NASA APOD |
| GET | `/api/zonas/{id}/nasa-earth-image` | Imagem de satélite NASA |

### Alertas
| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/alertas` | Listar todos (HATEOAS) |
| GET | `/api/alertas/{id}` | Buscar por ID (HATEOAS) |
| POST | `/api/alertas` | Criar alerta |
| PUT | `/api/alertas/{id}` | Atualizar alerta |
| DELETE | `/api/alertas/{id}` | Deletar alerta |
| PATCH | `/api/alertas/{id}/status` | Ativar/desativar |

### Configurações de Alerta por Zona *(chave composta)*
| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/zonas/{zonaId}/configuracoes-alerta` | Listar configurações da zona |
| POST | `/api/zonas/{zonaId}/configuracoes-alerta` | Criar/atualizar config (zonaId + nivelAlerta) |
| DELETE | `/api/zonas/{zonaId}/configuracoes-alerta/{nivelAlerta}` | Remover pela chave composta |

### Ocorrências
| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/ocorrencias` | Listar todas (HATEOAS) |
| GET | `/api/ocorrencias/{id}` | Buscar por ID (HATEOAS) |
| POST | `/api/ocorrencias` | Registrar ocorrência |
| PUT | `/api/ocorrencias/{id}` | Atualizar ocorrência |
| DELETE | `/api/ocorrencias/{id}` | Deletar ocorrência |
| PATCH | `/api/ocorrencias/{id}/status` | Atualizar status |

### Comentários
| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/ocorrencias/{id}/comentarios` | Listar comentários |
| POST | `/api/ocorrencias/{id}/comentarios` | Adicionar comentário |

### Tipos de Ocorrência
| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/tipos-ocorrencia` | Listar tipos ativos |

---

## 🌍 Integração com APIs Externas

### Open-Meteo (chuva)
Chamada em `POST /api/zonas/{id}/analisar-risco`:
- Busca precipitação das últimas 24h para lat/lon da zona
- Calcula nível de risco: >60mm → CRÍTICO, >30mm → ALTO, >10mm → MÉDIO, ≤10mm → BAIXO
- Salva `AnaliseRisco` com `fonteDados = "OPEN-METEO"`

### NASA Planetary API
- `GET /api/zonas/dashboard/{id}` → APOD (Astronomy Picture of the Day)
- `GET /api/zonas/{id}/nasa-earth-image` → Imagem de satélite da zona

---

## ⚙️ Variáveis de Ambiente

| Variável | Padrão | Descrição |
|---|---|---|
| `PORT` | `8080` | Porta da aplicação |
| `JWT_SECRET` | *(valor padrão dev)* | **Trocar em produção** |
| `JWT_EXPIRATION_MS` | `3600000` | Validade do token (1h) |
| `NASA_API_KEY` | `DEMO_KEY` | Chave da NASA API |

---

## 📚 Fontes de Dados

- [NASA Open APIs](https://api.nasa.gov)
- [Open-Meteo](https://open-meteo.com) — dados de precipitação
- [International Charter Space and Major Disasters](https://www.disasterscharter.org)
