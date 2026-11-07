# Trabalho de Estrutura de Dados I - Aplicações de Listas em Java

**Domínio 9 - Finanças: banco digital** (Banco -> Cliente -> Conta -> Transação)

Equipe: Miguel Damasceno, Isaac Daniel, Mateus Torquato, Diogo Lemos, Sandro Nascimento
Disciplina: Estrutura de Dados I - SI-2026 - UNIFESSPA

## Versão do JDK

Desenvolvido e testado com **JDK 21**. Java puro, sem bibliotecas externas.

## Compilar e executar

Na pasta raiz do projeto:

```bash
javac -encoding UTF-8 -d out src/*.java
java -cp out Main
```

Por padrão o programa carrega (e o menu "Salvar" grava) `dados/banco.txt`.
Para usar outro arquivo, informe o caminho como argumento:

```bash
java -cp out Main dados/grande.txt
```

## Estrutura do repositório

```
src/      código-fonte Java
dados/    banco.txt (exemplo do enunciado) e grande.txt (massa de dados: 20 clientes, 40 contas, 120 transações)
saidas/   relatórios gerados a partir de dados/banco.txt
docs/     documentacao.pdf
slides/   apresentacao.pdf
```

## Relatórios (gravados em `saidas/` e exibidos no terminal)

- `clientes.txt` - CPF, nome, número de contas e saldo total, ordenado pelo nome
- `extrato_NUMERO_MM_yyyy.txt` - saldo anterior, transações do mês com o saldo após cada uma e saldo final
