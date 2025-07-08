# GestCon - Sistema de Gestão de Contratos

Sistema completo para gestão de contratos terceirizados em órgãos públicos, desenvolvido com Spring Boot e Angular.

## 📋 Descrição

O GestCon é uma solução robusta para gerenciamento de contratos públicos que oferece:

- **Gestão Completa de Contratos**: Criação, acompanhamento e renovação
- **Controle de Pagamentos**: Gestão de parcelas e comprovantes
- **Notas Fiscais**: Upload, validação e aprovação
- **Workflow Automatizado**: Processos de aprovação estruturados
- **Notificações**: Alertas por email e sistema
- **Relatórios**: Dashboards e relatórios gerenciais

## 🏗️ Arquitetura do Projeto

```
gestcon/
├── backend/                    # API REST Spring Boot
│   ├── src/main/java/com/gestcon/
│   │   ├── controller/         # Controllers REST
│   │   ├── model/             # Entidades JPA
│   │   ├── repository/        # Repositórios de dados
│   │   ├── service/           # Lógica de negócio
│   │   └── GestConApplication.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
├── frontend/                   # Aplicação Angular
│   └── gestcon-ui/
├── database/                   # Scripts SQL
└── docs/                      # Documentação
```

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Security**
- **PostgreSQL**
- **Maven**
- **JasperReports**
- **JavaMail**

### Frontend
- **Angular**
- **TypeScript**
- **Bootstrap**
- **RxJS**

## 📦 Funcionalidades Principais

### 🏢 Gestão de Empresas
- Cadastro e manutenção de empresas contratadas
- Validação de CNPJ
- Histórico de contratos

### 📄 Gestão de Contratos
- Criação e edição de contratos
- Controle de vigência
- Status de acompanhamento
- Renovação automática

### 💰 Gestão Financeira
- Controle de pagamentos por parcela
- Acompanhamento de valores
- Relatórios financeiros
- Integração com notas fiscais

### 📋 Notas Fiscais
- Upload de documentos (PDF/XML)
- Validação automática
- Processo de aprovação
- Controle de vencimentos

### 👥 Gestão de Usuários
- Controle de acesso por perfil
- Autenticação segura
- Auditoria de ações

### 🔔 Notificações
- Alertas de vencimento
- Notificações por email
- Dashboard de pendências

## ⚙️ Configuração e Instalação

### Pré-requisitos
- Java 17+
- Node.js 16+
- PostgreSQL 12+
- Maven 3.6+

### 1. Configuração do Banco de Dados

```sql
-- Criar banco de dados
CREATE DATABASE gestcon;

-- Criar usuário (opcional)
CREATE USER gestcon_user WITH PASSWORD 'sua_senha';
GRANT ALL PRIVILEGES ON DATABASE gestcon TO gestcon_user;
```

### 2. Configuração do Backend

```bash
# Navegar para o diretório backend
cd backend

# Configurar application.properties
cp src/main/resources/application.properties.example src/main/resources/application.properties

# Editar configurações do banco
spring.datasource.url=jdbc:postgresql://localhost:5432/gestcon
spring.datasource.username=gestcon_user
spring.datasource.password=sua_senha

# Instalar dependências e executar
mvn clean install
mvn spring-boot:run
```

### 3. Configuração do Frontend

```bash
# Navegar para o diretório frontend
cd frontend/gestcon-ui

# Instalar dependências
npm install

# Executar em modo desenvolvimento
ng serve

# Aplicação estará disponível em http://localhost:4200
```

## 🔧 Scripts Disponíveis

### Backend
```bash
# Executar testes
mvn test

# Gerar build de produção
mvn clean package

# Executar aplicação
mvn spring-boot:run
```

### Frontend
```bash
# Executar testes unitários
npm test

# Executar testes e2e
npm run e2e

# Build de produção
npm run build
```

## 📊 API Endpoints

### Contratos
- `GET /api/contratos` - Listar contratos
- `POST /api/contratos` - Criar contrato
- `PUT /api/contratos/{id}` - Atualizar contrato
- `DELETE /api/contratos/{id}` - Remover contrato

### Empresas
- `GET /api/empresas` - Listar empresas
- `POST /api/empresas` - Criar empresa
- `PUT /api/empresas/{id}` - Atualizar empresa

### Pagamentos
- `GET /api/pagamentos` - Listar pagamentos
- `GET /api/pagamentos/atrasados` - Pagamentos atrasados
- `PUT /api/pagamentos/{id}/marcar-pago` - Marcar como pago

### Notas Fiscais
- `GET /api/notas-fiscais` - Listar notas fiscais
- `POST /api/notas-fiscais` - Upload de nota fiscal
- `PUT /api/notas-fiscais/{id}/aprovar` - Aprovar nota fiscal

## 🔒 Segurança

- Autenticação JWT
- Controle de acesso baseado em roles
- Validação de entrada de dados
- Proteção contra CSRF
- Criptografia de senhas

## 📈 Monitoramento

- Logs estruturados
- Métricas de performance
- Health checks
- Auditoria de ações

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 📞 Suporte

Para suporte técnico ou dúvidas:

- Email: johnweider.tj@gmail.com
- Documentação: [Wiki do Projeto](wiki)
- Issues: [GitHub Issues](issues)

## 🏛️ Órgão Responsável

Desenvolvido para órgãos públicos brasileiros em conformidade com:
- Lei de Licitações (Lei 14.133/2021)
- Lei de Acesso à Informação
- LGPD (Lei Geral de Proteção de Dados)

---

**GestCon** - Gestão Inteligente de Contratos Públicos
