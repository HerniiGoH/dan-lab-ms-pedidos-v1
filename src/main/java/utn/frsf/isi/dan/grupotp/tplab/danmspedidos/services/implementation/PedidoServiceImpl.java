package utn.frsf.isi.dan.grupotp.tplab.danmspedidos.services.implementation;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model.DetallePedido;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model.Obra;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model.Pedido;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.repository.DetallePedidoRepository;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.repository.PedidoRepository;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.services.PedidoService;

import java.util.*;

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
        List<Pedido> pedidosEncontrados = pedidoRepository.findAll();
        pedidosEncontrados.forEach(pedido -> pedido.setObra(buscarObraPorId(pedido.getObraId())));
        return pedidosEncontrados;
    }

    @Override
    public Optional<Pedido> buscarPedidoPorId(Integer id) {
        Optional<Pedido> pedidoEncontrado = pedidoRepository.findById(id);
        pedidoEncontrado.ifPresent(pedido -> pedido.setObra(buscarObraPorId(pedido.getObraId())));
        return pedidoEncontrado;
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

        if(cuit!=null){
            cliente.put("id", Objects.requireNonNullElse(id, -1));
            cliente.put("cuit", cuit);
            json.put("cliente", cliente);
        } else {
            if(id!=null){
                cliente.put("id",id);
                json.put("cliente", cliente);
            }
        }
        WebClient webClient = WebClient.create("http://localhost:4040/api/obra");
        ResponseEntity<List<Obra>> response = webClient.method(HttpMethod.GET)
                .uri("/obra")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(json))
                .retrieve()
                .toEntityList(Obra.class)
                .block();

        if(response!=null && response.getStatusCode().equals(HttpStatus.OK)){
           List<Obra> obras = response.getBody();
            HashSet<Pedido> pedidos = new HashSet<>();
            assert obras != null;
            for(Obra aux : obras){
                pedidos.addAll(this.buscarPedidosPorIdObra(aux.getId()).orElseGet(ArrayList::new));
            }
            pedidos.forEach(pedido -> pedido.setObra(buscarObraPorId(pedido.getObraId())));
            return Optional.of(new ArrayList<>(pedidos));
        }

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
            Pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);
            pedidoGuardado.setObra(buscarObraPorId(pedidoGuardado.getObraId()));
            return Optional.of(pedidoGuardado);
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

    @Override
    public Boolean borrarDetallePedido(Integer idPedido, Integer id) {
        if(detallePedidoRepository.existsById(id)){
            detallePedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Obra buscarObraPorId(Integer id){
        WebClient webClient = WebClient.create("http://localhost:4040/api/obra/"+id);
        ResponseEntity<Obra> response = webClient.method(HttpMethod.GET)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(Obra.class)
                .block();
        if(response!=null && response.getStatusCode().equals(HttpStatus.OK)){
            return response.getBody();
        }
        return null;
    }
}
