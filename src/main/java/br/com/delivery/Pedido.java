package br.com.delivery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pedido {
    private final int id;
    private final String cliente;
    private final String endereco;
    private final List<ItemPedido> itens = new ArrayList<>();
    private String status;

    public Pedido(int id, String cliente, String endereco) {
        this.id = id;
        this.cliente = cliente;
        this.endereco = endereco;
        this.status = "ABERTO";
    }

    public int getId() {
        return id;
    }

    public String getCliente() {
        return cliente;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public void adicionarItem(ItemPedido item) {
        itens.add(item);
    }

    public double getTotal() {
        return itens.stream().mapToDouble(ItemPedido::getSubtotal).sum();
    }
}
