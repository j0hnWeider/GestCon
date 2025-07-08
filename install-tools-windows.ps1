# Script PowerShell para instalar ferramentas necessárias para o projeto GestCon no Windows

# Instalar Chocolatey (gerenciador de pacotes)
Set-ExecutionPolicy Bypass -Scope Process -Force; `
[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; `
iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# Atualizar Chocolatey
choco upgrade chocolatey -y

# Instalar Java 17 (OpenJDK)
choco install openjdk17 -y

# Instalar Maven
choco install maven -y

# Instalar Node.js LTS (inclui npm)
choco install nodejs-lts -y

# Instalar Angular CLI globalmente
npm install -g @angular/cli

Write-Host "Instalação concluída. Por favor, reinicie o terminal para aplicar as alterações no PATH."
