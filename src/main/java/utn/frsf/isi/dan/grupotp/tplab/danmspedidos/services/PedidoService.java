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
    Optional<List<Pedido>> buscarPedidosPorEstado(String estado);
    Optional<DetallePedido> buscarDetallePedidoPorId(Integer idPedido, Integer id);
    Pedido crearPedido(Pedido nuevoPedido);
    DetallePedido crearDetallePedido(DetallePedido nuevoDetallePedido, Integer id);
    Optional<Pedido> actualizarPedido(Pedido nuevoPedido, Integer id);
    Optional<DetallePedido> actualizarDetallePedido(DetallePedido nuevoDetalle, Integer idPedido, Integer id);
    Optional<Pedido> actualizarEstadoPedido(Integer id, String estado);
    Boolean borrarPedido(Integer id);
    Boolean borrarDetallePedido(Integer idPedido, Integer id);
}
