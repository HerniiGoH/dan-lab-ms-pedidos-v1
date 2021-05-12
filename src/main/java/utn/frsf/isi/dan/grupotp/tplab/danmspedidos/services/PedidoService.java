package utn.frsf.isi.dan.grupotp.tplab.danmspedidos.services;

import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model.DetallePedido;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoService {
    List<Pedido> buscarTodos();
    Optional<Pedido> buscarPedidoPorId(Integer id);
    Optional<List<Pedido>> buscarPedidosPorIdObra(Integer id);
    Optional<List<Pedido>> buscarPedidosPorCliente(Integer id, String cuit);
    Optional<DetallePedido> buscarDetallePedidoPorId(Integer idPedido, Integer id);
    Pedido crearPedido(Pedido nuevoPedido);
    DetallePedido crearDetallePedido(DetallePedido nuevoDetallePedido, Integer id);
    Optional<Pedido> actualizarPedido(Pedido nuevoPedido, Integer id);
    Boolean borrarPedido(Integer id);
}
