package utn.frsf.isi.dan.grupotp.tplab.danmspedidos.services.implementation;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model.DetallePedido;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model.Obra;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model.Pedido;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.repository.DetallePedidoRepository;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.repository.PedidoRepository;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.services.PedidoService;

import java.util.List;
import java.util.Optional;

@Service
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
        JSONObject json = new JSONObject();
        JSONObject cliente = new JSONObject();
        cliente.put("id", id);
        cliente.put("cuit", cuit);
        json.put("cliente", cliente);
        WebClient webClient = WebClient.create("http://localhost:4040/api/obra");
        ResponseEntity<List<Obra>> response = webClient.method(HttpMethod.GET)
                .uri("/obra")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(json))
                .retrieve()
                .toEntityList(Obra.class)
                .block();

        return Optional.empty();
    }

    @Override
    public Optional<DetallePedido> buscarDetallePedidoPorId(Integer idPedido, Integer id) {
        return detallePedidoRepository.findById(id);
    }

    @Override
    public Pedido crearPedido(Pedido nuevoPedido) {
        Pedido pedidoCreado = pedidoRepository.save(nuevoPedido);
        for(DetallePedido aux : nuevoPedido.getDetallePedido()){
            aux.setPedido(pedidoCreado);
        }
        pedidoCreado.setDetallePedido(detallePedidoRepository.saveAll(nuevoPedido.getDetallePedido()));
        return pedidoCreado;
    }

    @Override
    public DetallePedido crearDetallePedido(DetallePedido nuevoDetallePedido, Integer id) {
        return detallePedidoRepository.save(nuevoDetallePedido);
    }

    @Override
    public Optional<Pedido> actualizarPedido(Pedido nuevoPedido, Integer id) {
        if(pedidoRepository.existsById(id)){
            return Optional.of(pedidoRepository.save(nuevoPedido));
        }
        return Optional.empty();
    }

    @Override
    public Boolean borrarPedido(Integer id) {
        if(pedidoRepository.existsById(id)){
            pedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
