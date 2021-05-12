package utn.frsf.isi.dan.grupotp.tplab.danmspedidos.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model.DetallePedido;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model.Pedido;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.repository.DetallePedidoRepository;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.repository.PedidoRepository;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.services.PedidoService;

import java.util.List;
import java.util.Optional;

public class PedidoServiceImpl implements PedidoService {
    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;

    @Autowired
    public PedidoServiceImpl(PedidoRepository pedidoRepository, DetallePedidoRepository detallePedidoRepository){
        this.pedidoRepository=pedidoRepository;
        this.detallePedidoRepository=detallePedidoRepository;
    }

    @Override
    public List<Pedido> buscarTodos() {
        return pedidoRepository.findAll();
    }

    @Override
    public Optional<Pedido> buscarPedidoPorId(Integer id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public Optional<List<Pedido>> buscarPedidosPorIdObra(Integer id) {
        return pedidoRepository.findByIdObra(id);
    }

    @Override
    public Optional<List<Pedido>> buscarPedidosPorCliente(Integer id, String cuit) {
        //TODO hacer bien esto
        return Optional.empty();
    }

    @Override
    public Optional<DetallePedido> buscarDetallePedidoPorId(Integer idPedido, Integer id) {
        return detallePedidoRepository.findById(id);
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
