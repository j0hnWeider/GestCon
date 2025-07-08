# Script de configuração do projeto GestCon
# Execute este script para configurar o ambiente de desenvolvimento

Write-Host "=== Configuração do Projeto GestCon ===" -ForegroundColor Green

# Verificar se Java está instalado
Write-Host "Verificando Java..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1 | Select-String "version"
    Write-Host "✓ Java encontrado: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "✗ Java não encontrado. Instale Java 17 ou superior." -ForegroundColor Red
    exit 1
}

# Verificar se Maven está instalado
Write-Host "Verificando Maven..." -ForegroundColor Yellow
try {
    $mavenVersion = mvn -version 2>&1 | Select-String "Apache Maven"
    Write-Host "✓ Maven encontrado: $mavenVersion" -ForegroundColor Green
} catch {
    Write-Host "✗ Maven não encontrado. Instale Maven 3.6 ou superior." -ForegroundColor Red
    exit 1
}

# Verificar se Node.js está instalado
Write-Host "Verificando Node.js..." -ForegroundColor Yellow
try {
    $nodeVersion = node --version
    Write-Host "✓ Node.js encontrado: $nodeVersion" -ForegroundColor Green
} catch {
    Write-Host "✗ Node.js não encontrado. Instale Node.js 16 ou superior." -ForegroundColor Red
    exit 1
}

# Verificar se PostgreSQL está instalado
Write-Host "Verificando PostgreSQL..." -ForegroundColor Yellow
try {
    $pgVersion = psql --version
    Write-Host "✓ PostgreSQL encontrado: $pgVersion" -ForegroundColor Green
} catch {
    Write-Host "⚠ PostgreSQL não encontrado. Certifique-se de ter PostgreSQL instalado." -ForegroundColor Yellow
}

Write-Host "`n=== Configurando Backend ===" -ForegroundColor Green

# Navegar para o diretório backend
Set-Location backend

# Limpar e instalar dependências
Write-Host "Instalando dependências do backend..." -ForegroundColor Yellow
mvn clean install -DskipTests

if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ Dependências do backend instaladas com sucesso!" -ForegroundColor Green
} else {
    Write-Host "✗ Erro ao instalar dependências do backend." -ForegroundColor Red
    exit 1
}

# Voltar ao diretório raiz
Set-Location ..

Write-Host "`n=== Configurando Frontend ===" -ForegroundColor Green

# Verificar se o diretório frontend existe
if (Test-Path "frontend/gestcon-ui") {
    # Navegar para o diretório frontend
    Set-Location frontend/gestcon-ui
    
    # Instalar dependências do frontend
    Write-Host "Instalando dependências do frontend..." -ForegroundColor Yellow
    npm install
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✓ Dependências do frontend instaladas com sucesso!" -ForegroundColor Green
    } else {
        Write-Host "✗ Erro ao instalar dependências do frontend." -ForegroundColor Red
    }
    
    # Voltar ao diretório raiz
    Set-Location ../..
} else {
    Write-Host "⚠ Diretório frontend/gestcon-ui não encontrado." -ForegroundColor Yellow
}

Write-Host "`n=== Configuração Concluída ===" -ForegroundColor Green
Write-Host "Para executar o projeto:" -ForegroundColor White
Write-Host "1. Backend: cd backend && mvn spring-boot:run" -ForegroundColor Cyan
Write-Host "2. Frontend: cd frontend/gestcon-ui && ng serve" -ForegroundColor Cyan
Write-Host "3. Ou use Docker: docker-compose up" -ForegroundColor Cyan

Write-Host "`n=== URLs da Aplicação ===" -ForegroundColor Green
Write-Host "Backend API: http://localhost:8080" -ForegroundColor Cyan
Write-Host "Frontend: http://localhost:4200" -ForegroundColor Cyan
Write-Host "Swagger UI: http://localhost:8080/swagger-ui.html" -ForegroundColor Cyan

Write-Host "`nConfiguração concluída com sucesso! 🚀" -ForegroundColor Green
