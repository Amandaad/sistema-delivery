package br.com.delivery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeliveryService {
    private final List<Pedido> pedidos;

    public DeliveryService(List<Pedido> pedidos) {
        this.pedidos = new ArrayList<>(pedidos);
    }

    public Pedido criarPedido(String cliente, String endereco) {
        int proximoId = pedidos.stream().mapToInt(Pedido::getId).max().orElse(0) + 1;
        Pedido pedido = new Pedido(proximoId, cliente, endereco);
        pedidos.add(pedido);
        return pedido;
    }

    public Optional<Pedido> buscarPedido(int id) {
        return pedidos.stream().filter(p -> p.getId() == id).findFirst();
    }

    public List<Pedido> listarPedidos() {
        return List.copyOf(pedidos);
    }
}
