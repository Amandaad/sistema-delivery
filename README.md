# Sistema de Delivery (Java + XML)

Exemplo simples de sistema de delivery em Java com persistência em XML.

## Funcionalidades
- Cadastro de pedidos
- Itens por pedido
- Cálculo de total
- Persistência em `src/main/resources/pedidos.xml`

## Executar
```bash
mvn -q compile
mvn -q exec:java -Dexec.mainClass=br.com.delivery.Main
```

Ou, sem plugin Maven:
```bash
javac -d out $(find src/main/java -name "*.java")
java -cp out br.com.delivery.Main
```
