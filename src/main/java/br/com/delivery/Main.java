package br.com.delivery;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        XmlPedidoRepository repository = new XmlPedidoRepository("src/main/resources/pedidos.xml");
        DeliveryService service = new DeliveryService(repository.carregar());

        Pedido pedido = service.criarPedido("Maria", "Rua das Flores, 123");
        pedido.adicionarItem(new ItemPedido(new Produto("Pizza Calabresa", 49.90), 1));
        pedido.adicionarItem(new ItemPedido(new Produto("Refrigerante 2L", 12.00), 2));
        pedido.setStatus("EM_PREPARO");

        repository.salvar(service.listarPedidos());

        System.out.println("=== Pedidos cadastrados ===");
        listar(service.listarPedidos());
        System.out.println("\nArquivo XML atualizado em: src/main/resources/pedidos.xml");
    }

    private static void listar(List<Pedido> pedidos) {
        for (Pedido pedido : pedidos) {
            System.out.printf("Pedido #%d | Cliente: %s | Status: %s | Total: R$ %.2f%n",
                    pedido.getId(), pedido.getCliente(), pedido.getStatus(), pedido.getTotal());
            for (ItemPedido item : pedido.getItens()) {
                System.out.printf("  - %dx %s (R$ %.2f)%n",
                        item.getQuantidade(), item.getProduto().getNome(), item.getSubtotal());
            }
        }
    }
}
