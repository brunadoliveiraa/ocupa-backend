# OCUPA - Arquitetura e Scripts de Banco de Dados

## 1. Visão geral da solução

OCUPA é uma plataforma web para fortalecer ecossistemas culturais periféricos, conectando artistas, coletivos, espaços e oportunidades. A solução atende aos requisitos do curso com:

- Backend em Java (API REST)
- Frontend em ReactJS com Tailwind CSS
- Banco de dados MySQL local na porta 3306
- Separação clara entre backend e frontend
- CRUDs integrados ao frontend

## 2. Arquitetura proposta

### 2.1 Backend

- Reuso e decisão de pasta:
  - Recomendo criar um backend isolado em `ocupa-backend/` (Spring Boot) para o desenvolvimento do OCUPA.

- Framework recomendado: Spring Boot (2.6+ ou 3.x conforme JDK).

- Camadas mínimas:
  - Controller (API REST) — endpoints organizados em `/api/artistas`, `/api/espacos`, `/api/oportunidades`, `/api/eventos`, `/api/requests`, `/api/portfolios`, `/api/analytics`.
  - Service (lógica de negócio simples)
  - Repository (persistência JPA/Hibernate)

- Conexão com MySQL via `application.properties` e perfis (`application-dev.properties`).
- API base (padrão): `http://localhost:8080/api` (configurável).

### 2.2 Frontend

-- Pasta: `ocupa-frontend/` (separada do ocupa-backend).
- Setup recomendado: Vite + React (+ TypeScript opcional) + Tailwind CSS.
- Bibliotecas sugeridas:
  - `axios` (consumo de API)
  - `react-router-dom` (rotas)
  - `react-query` ou `swr` (cache/estado de dados, opcional)
  - `leaflet` / `react-leaflet` (mapa afetivo)

- Componentização mínima:
  - `Navbar`, `Footer`, `Card`, `Form`, `Table/List`, `Modal`, `Profile`, `Dashboard`, `Map`.

- Páginas principais:
  - Home
  - Artistas (CRUD)
  - Espaços (CRUD + mapa)
  - Oportunidades (CRUD)
  - Eventos (CRUD / agenda)
  - Painel (Dashboard)
  - Contrate / Solicitar orçamento

### 2.3 Banco de dados

- Banco: `ocupa_db`
- Host: `localhost`
- Porta: `3306`
- Usuário e senha: configuráveis localmente
- Modelo relacional simples e funcional

## 3. Requisitos atendidos

### CRUDs principais
- 1. Artistas (CRUD completo)
- 2. Espaços culturais (CRUD completo)
- 3. Oportunidades (CRUD completo)
- 4. Eventos (CRUD completo)
- 5. Portfólios (CRUD completo para `portfolio` e `portfolio_media`)
- 6. Requests / Solicitações (criar, listar, atualizar status, remover)

Cada um desses CRUDs deve permitir:
- Criar registros
- Listar registros
- Atualizar registros
- Remover registros

### Integração frontend/backend

- Frontend consome API REST do backend
- Cada CRUD é acessível via interface React
- Formulários, listas e ações devem ser reais e funcionais
 
## Funcionalidades (mapeamento das 8 solicitadas)

O sistema implementará as oito funcionalidades descritas no enunciado. Abaixo cada funcionalidade e como será representada na solução (entidades e telas/API relacionadas):

1. Divulgação de oportunidades
  - O que: listagem e detalhamento de bolsas, residências, editais e chamadas.
  - Entidade/API: `oportunidade` — endpoints CRUD e página `Oportunidades`.
  - Frontend: formulário de criação/edição, lista com filtros e links de inscrição.

2. Catálogo de espaços
  - O que: cadastro e busca de espaços com atributos (cobertura, iluminação, energia, capacidade, permissões).
  - Entidade/API: `espaco` — endpoints CRUD e página `Espaços`.
  - Frontend: cards de espaço, filtros por atributos e página de detalhe.

3. Portfólio profissional (página do artista)
  - O que: páginas profissionais com biografia, mídia, contatos e serviços.
  - Entidade/API: `artista`, `portfolio`, `portfolio_media` — endpoints para CRUD e upload de URLs.
  - Frontend: perfil público do artista, galeria e formulário de edição.

4. Agenda cultural
  - O que: calendário de eventos e visualização por data/espaço.
  - Entidade/API: `evento` — endpoints CRUD; relação com `espaco` e `artista`.
  - Frontend: componente `Calendar` / lista por mês e detalhe do evento.

5. Painel do empreendedor
  - O que: dashboard para cada artista/usuário com métricas simples (acessos, contatos, propostas).
  - Entidade/API: `analytics`, `requests` e agregações via endpoints específicos.
  - Frontend: página `Painel` com gráficos e indicadores simples.

6. Solicite um orçamento
  - O que: funcionalidade para solicitar orçamento a um artista/coletivo via formulário.
  - Entidade/API: `requests` — criar e atualizar status (PENDING, ACCEPTED, REJECTED, COMPLETED).
  - Frontend: botão `Solicitar Orçamento` em perfis e cards, modal/form e lista de solicitações.

7. Contrate (busca por serviços)
  - O que: busca avançada que reúne serviços disponíveis na comunidade e permite contato/contratação.
  - Entidade/API: combina `artista` + `portfolio` + `requests`/contato.
  - Frontend: página `Contrate` com filtros por categoria/serviço e links para solicitar orçamento.

8. Mapa afetivo de ativos
  - O que: mapa simplificado (pontos georreferenciados) mostrando espaços, artistas e eventos com histórico.
  - Entidade/API: `espaco`, `evento`, `artista` (com campos de localização opcionais).
  - Frontend: componente `Map` (integração com Leaflet ou similar) exibindo camadas de ativos.

Observação: para manter o escopo simples e funcional, implementaremos versões básicas de cada item (sem integrações pesadas, uso de URLs para mídia e um mapa com coordenadas opcionais). Prioridade inicial: CRUDs de `artista`, `espaco` e `oportunidade`, depois `evento`, `portfolio` e `requests`, finalizando `analytics` e mapa.

## 4. Alinhamento com ODS

ODS trabalhados:

- ODS 8 — Trabalho Decente e Crescimento Econômico
- ODS 10 — Redução das Desigualdades
- ODS 4 — Educação de Qualidade (apoio indireto via cultura e formação social)

### Impacto social

- Aumentar visibilidade de artistas periféricos
- Organizar oportunidades culturais
- Facilitar contratações e contatos profissionais
- Fortalecer a economia criativa local
- Valorizar memória e patrimônio cultural do território

## 5. Entidades principais

A solução inicial terá as seguintes entidades:

- `artista` — usuários / perfis profissionais (biografia, contatos, categoria, mídias, localização opcional).
- `espaco` — espaços físicos com atributos (cobertura, iluminação, energia, capacidade, permissões) e coordenadas para mapa.
- `oportunidade` — bolsas, residências, editais e chamadas.
- `evento` — eventos e agenda cultural, vinculados a `espaco` e `artista`.
- `portfolio` / `portfolio_media` — portfólios dos artistas e mídias associadas (imagens, vídeos).
- `requests` — solicitações de orçamento / contratações (fluxo PENDING → ACCEPTED/REJECTED → COMPLETED).
- `analytics` — registros simples para alimentar o painel do empreendedor (contagem de acessos, cliques, etc.).

## 6. Scripts de banco de dados MySQL

```sql
-- Criar banco de dados
CREATE DATABASE IF NOT EXISTS ocupa_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ocupa_db;

-- Tabela de artistas
CREATE TABLE IF NOT EXISTS artista (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(150) NOT NULL,
  categoria VARCHAR(100) NOT NULL,
  biografia TEXT,
  contato VARCHAR(150),
  cidade VARCHAR(100),
  redes_sociais VARCHAR(250),
  foto_url VARCHAR(250),
  latitude DECIMAL(10,7) NULL,
  longitude DECIMAL(10,7) NULL,
  criado_em DATETIME DEFAULT CURRENT_TIMESTAMP,
  atualizado_em DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabela de espaços culturais
CREATE TABLE IF NOT EXISTS espaco (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(150) NOT NULL,
  endereco VARCHAR(250),
  descricao TEXT,
  cobertura BOOLEAN DEFAULT FALSE,
  iluminacao BOOLEAN DEFAULT FALSE,
  energia BOOLEAN DEFAULT FALSE,
  banheiro BOOLEAN DEFAULT FALSE,
  capacidade INT,
  permite_grafite BOOLEAN DEFAULT FALSE,
  permite_batalha BOOLEAN DEFAULT FALSE,
  permite_danca BOOLEAN DEFAULT FALSE,
  latitude DECIMAL(10,7) NULL,
  longitude DECIMAL(10,7) NULL,
  criado_em DATETIME DEFAULT CURRENT_TIMESTAMP,
  atualizado_em DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabela de oportunidades
CREATE TABLE IF NOT EXISTS oportunidade (
  id INT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(180) NOT NULL,
  tipo VARCHAR(100),
  descricao TEXT,
  local VARCHAR(200),
  data_inicio DATE,
  data_fim DATE,
  inscricao_link VARCHAR(250),
  contato VARCHAR(150),
  criado_em DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de eventos
CREATE TABLE IF NOT EXISTS evento (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(180) NOT NULL,
  descricao TEXT,
  local VARCHAR(200),
  data_evento DATE,
  hora_evento TIME,
  publico_estimado INT,
  artista_id INT NULL,
  espaco_id INT NULL,
  latitude DECIMAL(10,7) NULL,
  longitude DECIMAL(10,7) NULL,
  criado_em DATETIME DEFAULT CURRENT_TIMESTAMP,
  atualizado_em DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_evento_artista FOREIGN KEY (artista_id) REFERENCES artista(id) ON DELETE SET NULL,
  CONSTRAINT fk_evento_espaco FOREIGN KEY (espaco_id) REFERENCES espaco(id) ON DELETE SET NULL
);

-- Portfólios e mídia (adicionais para perfis dos artistas)
CREATE TABLE IF NOT EXISTS portfolio (
  id INT AUTO_INCREMENT PRIMARY KEY,
  artista_id INT NOT NULL,
  headline VARCHAR(250),
  about TEXT,
  contacts JSON NULL,
  criado_em DATETIME DEFAULT CURRENT_TIMESTAMP,
  atualizado_em DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_portfolio_artista FOREIGN KEY (artista_id) REFERENCES artista(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS portfolio_media (
  id INT AUTO_INCREMENT PRIMARY KEY,
  portfolio_id INT NOT NULL,
  media_type ENUM('IMAGE','VIDEO','AUDIO','OTHER') DEFAULT 'IMAGE',
  url VARCHAR(500) NOT NULL,
  caption VARCHAR(255),
  criado_em DATETIME DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_media_portfolio FOREIGN KEY (portfolio_id) REFERENCES portfolio(id) ON DELETE CASCADE
);

-- Solicitações de orçamento / contratações
CREATE TABLE IF NOT EXISTS requests (
  id INT AUTO_INCREMENT PRIMARY KEY,
  requester_nome VARCHAR(150) NOT NULL,
  requester_contato VARCHAR(150),
  provider_artista_id INT NOT NULL,
  descricao TEXT,
  status ENUM('PENDING','ACCEPTED','REJECTED','COMPLETED') DEFAULT 'PENDING',
  criado_em DATETIME DEFAULT CURRENT_TIMESTAMP,
  atualizado_em DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_request_provider FOREIGN KEY (provider_artista_id) REFERENCES artista(id) ON DELETE CASCADE
);

-- Analytics simples para painel
CREATE TABLE IF NOT EXISTS analytics (
  id INT AUTO_INCREMENT PRIMARY KEY,
  artista_id INT NULL,
  page VARCHAR(255),
  event_type VARCHAR(100),
  value INT DEFAULT 1,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_analytics_artista FOREIGN KEY (artista_id) REFERENCES artista(id) ON DELETE SET NULL
);

-- Dados iniciais de exemplo
INSERT INTO artista (nome, categoria, biografia, contato, cidade, redes_sociais, foto_url)
VALUES
  ('Mariana Silva', 'Grafiteira', 'Grafiteira periférica com trabalhos voltados para memória urbana.', 'mariana@example.com', 'Cidade Baixa', '@mariana.graf', 'https://example.com/foto1.jpg'),
  ('DJ Preto', 'DJ', 'DJ que conecta batidas locais e eventos comunitários.', 'djpreto@example.com', 'Vila Novo', '@djpreto', 'https://example.com/foto2.jpg');

INSERT INTO espaco (nome, endereco, descricao, cobertura, iluminacao, energia, banheiro, capacidade, permite_grafite, permite_batalha, permite_danca, latitude, longitude)
VALUES
  ('Praça do Muquiço', 'Rua das Flores, 123', 'Espaço aberto com palco comunitário.', TRUE, TRUE, TRUE, TRUE, 120, TRUE, TRUE, TRUE, -23.55052, -46.633308),
  ('Galeria Comunidade', 'Avenida Brasil, 45', 'Espaço para exposições e oficinas culturais.', FALSE, TRUE, TRUE, TRUE, 60, FALSE, FALSE, TRUE, -23.55100, -46.632000);

INSERT INTO oportunidade (titulo, tipo, descricao, local, data_inicio, data_fim, inscricao_link, contato)
VALUES
  ('Residência artística comunitária', 'Residência', 'Residência para artistas e coletivos locais.', 'Centro Cultural', '2026-08-01', '2026-08-30', 'https://inscricao.example.com', 'contato@ocupa.com'),
  ('Chamada para mural coletivo', 'Edital', 'Edital para produção de mural participativo.', 'Praça do Muquiço', '2026-09-10', '2026-09-20', 'https://inscricao.example.com/mural', 'edital@ocupa.com');

INSERT INTO evento (nome, descricao, local, data_evento, hora_evento, publico_estimado, artista_id, espaco_id)
VALUES
  ('Sarau do Bairro', 'Encontro cultural com poesia, música e debate.', 'Praça do Muquiço', '2026-07-20', '18:00:00', 180, 1, 1),
  ('Noite de Grafite', 'Intervenção artística em muro comunitário.', 'Galeria Comunidade', '2026-08-05', '16:00:00', 90, 1, 2);
```

## 7. Passo a passo para iniciar

1. Confirmar este documento e executar os scripts no MySQL local.
2. Criar a pasta `ocupa-backend/` para o projeto Java Spring Boot.
3. Criar a pasta `ocupa-frontend/` para o projeto React + Tailwind.
4. Construir os CRUDs de `Artista`, `Espaço` e `Oportunidade` com API e interface.
5. Testar os endpoints e sincronizar o frontend com o backend.

## 8. Observação importante

- O backend e o frontend serão separados em projetos distintos.
- A solução será simples, funcional e alinhada aos requisitos do curso.

