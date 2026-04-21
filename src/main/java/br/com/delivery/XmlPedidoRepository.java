package br.com.delivery;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlPedidoRepository {
    private final File arquivo;

    public XmlPedidoRepository(String caminhoArquivo) {
        this.arquivo = new File(caminhoArquivo);
    }

    public List<Pedido> carregar() {
        try {
            garantirArquivo();
            Document document = criarBuilder().parse(arquivo);
            NodeList pedidosXml = document.getElementsByTagName("pedido");
            List<Pedido> pedidos = new ArrayList<>();

            for (int i = 0; i < pedidosXml.getLength(); i++) {
                Element pedidoEl = (Element) pedidosXml.item(i);
                int id = Integer.parseInt(pedidoEl.getAttribute("id"));
                String cliente = texto(pedidoEl, "cliente");
                String endereco = texto(pedidoEl, "endereco");
                String status = texto(pedidoEl, "status");

                Pedido pedido = new Pedido(id, cliente, endereco);
                pedido.setStatus(status);

                NodeList itens = pedidoEl.getElementsByTagName("item");
                for (int j = 0; j < itens.getLength(); j++) {
                    Element itemEl = (Element) itens.item(j);
                    String nome = texto(itemEl, "nome");
                    double preco = Double.parseDouble(texto(itemEl, "preco"));
                    int quantidade = Integer.parseInt(texto(itemEl, "quantidade"));
                    pedido.adicionarItem(new ItemPedido(new Produto(nome, preco), quantidade));
                }
                pedidos.add(pedido);
            }
            return pedidos;
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao carregar XML de pedidos", e);
        }
    }

    public void salvar(List<Pedido> pedidos) {
        try {
            Document document = criarBuilder().newDocument();
            Element raiz = document.createElement("pedidos");
            document.appendChild(raiz);

            for (Pedido pedido : pedidos) {
                Element pedidoEl = document.createElement("pedido");
                pedidoEl.setAttribute("id", String.valueOf(pedido.getId()));
                appendTexto(document, pedidoEl, "cliente", pedido.getCliente());
                appendTexto(document, pedidoEl, "endereco", pedido.getEndereco());
                appendTexto(document, pedidoEl, "status", pedido.getStatus());

                Element itensEl = document.createElement("itens");
                for (ItemPedido item : pedido.getItens()) {
                    Element itemEl = document.createElement("item");
                    appendTexto(document, itemEl, "nome", item.getProduto().getNome());
                    appendTexto(document, itemEl, "preco", String.format("%.2f", item.getProduto().getPreco()));
                    appendTexto(document, itemEl, "quantidade", String.valueOf(item.getQuantidade()));
                    itensEl.appendChild(itemEl);
                }
                pedidoEl.appendChild(itensEl);
                raiz.appendChild(pedidoEl);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(new DOMSource(document), new StreamResult(arquivo));
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao salvar XML de pedidos", e);
        }
    }

    private DocumentBuilder criarBuilder() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        return factory.newDocumentBuilder();
    }

    private String texto(Element parent, String tag) {
        return parent.getElementsByTagName(tag).item(0).getTextContent();
    }

    private void appendTexto(Document doc, Element pai, String tag, String valor) {
        Element el = doc.createElement(tag);
        el.appendChild(doc.createTextNode(valor));
        pai.appendChild(el);
    }

    private void garantirArquivo() {
        try {
            if (arquivo.exists()) {
                return;
            }
            File parent = arquivo.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            Document vazio = criarBuilder().newDocument();
            vazio.appendChild(vazio.createElement("pedidos"));
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(vazio), new StreamResult(arquivo));
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao criar arquivo XML inicial", e);
        }
    }
}
