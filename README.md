# ⚡ Kinetic | High-Performance Knockback Engine

![Version](https://img.shields.io/badge/version-1.0.0-blue)
![Platform](https://img.shields.io/badge/platform-Spigot-gold)
![Java](https://img.shields.io/badge/java-8-orange)

**Kinetic** é um motor de manipulação de Knockback (KB) de baixa latência, desenvolvido especificamente para cenários de PvP competitivo nas versões **1.7.10** e **1.8.9**. O foco principal do projeto é eliminar a inconsistência da física padrão do Minecraft, proporcionando um combate fluido e altamente personalizável.

---

## 🚀 Diferenciais Técnicos

* **NMS Optimized:** Implementação direta via pacotes (*Network Management Service*) para garantir que a velocidade seja aplicada instantaneamente, sem o overhead da API padrão do Bukkit.
* **Lag Compensation (TPS/Ping):** Algoritmo dinâmico que ajusta os multiplicadores de vetor com base na saúde do servidor (TPS) e na latência do jogador (Ping).
* **Cross-Version Support:** Arquitetura baseada em interfaces que detecta e carrega automaticamente o suporte para `v1_7_R4` até `v1_8_R3`.
* **Thread Safety:** Cálculos de vetores otimizados para evitar impacto na main thread do servidor.

---

## 🛠 Stack Tecnológica

O projeto foi construído utilizando as seguintes dependências e ferramentas:

* **Build Tool:** Maven 4.0.0
* **Core API:** Spigot 1.8.8-R0.1-SNAPSHOT
* **Legacy Support:** Spigot 1.7.10-R0.1-SNAPSHOT
* **Language:** Java 8 (focado em máxima compatibilidade com servidores antigos)

---

## 📁 Estrutura do Projeto

* `me.r1ver.kinetic.versions`: Abstração de NMS para diferentes versões do servidor.
* `me.r1ver.kinetic.commands`: Sistema de comandos para gerenciamento em tempo real.
* `me.r1ver.kinetic.KineticListener`: O "coração" do motor, onde o cálculo de vetores acontece.

---

## ⚙️ Configuração Rápida

O plugin gera automaticamente um arquivo `config.yml` onde é possível ajustar:
```yaml
knockback:
  horizontal: 0.5  # Eixos X e Z
  vertical: 0.5    # Eixo Y
settings:
  compensate-lag: true  # Ativa ajuste por TPS
  compensate-ping: true # Ativa ajuste por Latência
