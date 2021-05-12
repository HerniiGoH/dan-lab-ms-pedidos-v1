package utn.frsf.isi.dan.grupotp.tplab.danmspedidos.services.implementation;

import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model.DetallePedido;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model.Pedido;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.services.PedidoService;

import java.util.List;
import java.util.Optional;

public class PedidoServiceImpl implements PedidoService {
    @Override
    public List<Pedido> buscarTodos() {
        return null;
    }

    @Override
    public Optional<Pedido> buscarPedidoPorId(Integer id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Pedido>> buscarPedidosPorIdObra(Integer id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Pedido>> buscarPedidosPorCliente(Integer id, String cuit) {
        return Optional.empty();
    }

    @Override
    public Optional<DetallePedido> buscarDetallePedidoPorId(Integer idPedido, Integer id) {
        return Optional.empty();
    }

    @Override
    public Pedido crearPedido(Pedido nuevoPedido) {
        return null;
    }

    @Override
    public DetallePedido crearDetallePedido(DetallePedido nuevoDetallePedido, Integer id) {
        return null;
    }

    @Override
    public Optional<Pedido> actualizarPedido(Pedido nuevoPedido, Integer id) {
        return Optional.empty();
    }

    @Override
    public Boolean borrarPedido(Integer id) {
        return null;
    }
}
