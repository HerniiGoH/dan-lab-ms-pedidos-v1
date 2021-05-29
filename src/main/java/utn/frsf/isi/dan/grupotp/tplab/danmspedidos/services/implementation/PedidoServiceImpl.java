package utn.frsf.isi.dan.grupotp.tplab.danmspedidos.services.implementation;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model.*;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.repository.DetallePedidoRepository;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.repository.PedidoRepository;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.services.PedidoService;

import javax.jms.ObjectMessage;
import java.util.*;

@Service
public class PedidoServiceImpl implements PedidoService {
    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final WebClient webClient1, webClient2, webClient3, webClient4, webClient5;

    @Autowired
    public PedidoServiceImpl(PedidoRepository pedidoRepository, DetallePedidoRepository detallePedidoRepository, @Qualifier("webClient1") WebClient webClient1, @Qualifier("webClient2") WebClient webClient2, @Qualifier("webClient3") WebClient webClient3, @Qualifier("webClient4") WebClient webClient4, @Qualifier("webClient5") WebClient webClient5) {
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.webClient1 = webClient1;
        this.webClient2 = webClient2;
        this.webClient3 = webClient3;
        this.webClient4 = webClient4;
        this.webClient5 = webClient5;
    }

    @Override
    public List<Pedido> buscarTodos() {
        List<Pedido> pedidosEncontrados = pedidoRepository.findAll();
        pedidosEncontrados.forEach(pedido -> {
            pedido.setObra(buscarObraPorId(pedido.getObraId()));
            pedido.getDetallePedido().forEach(dp -> {
                dp.setProducto(buscarProductoPorId(dp.getProductoId()));
            });
        });
        return pedidosEncontrados;
    }

    @Override
    public Optional<Pedido> buscarPedidoPorId(Integer id) {
        Optional<Pedido> pedidoEncontrado = pedidoRepository.findById(id);
        pedidoEncontrado.ifPresent(pedido -> {
            pedido.setObra(buscarObraPorId(pedido.getObraId()));
            pedido.getDetallePedido().forEach(dp -> {
                dp.setProducto(buscarProductoPorId(dp.getProductoId()));
            });
        });
        return pedidoEncontrado;
    }

    @Override
    public Optional<List<Pedido>> buscarPedidosPorIdObra(Integer id) {
        Optional<List<Pedido>> pedidosEncontrados = pedidoRepository.findByIdObra(id);
        pedidosEncontrados.ifPresent(pedidos -> pedidos.forEach(pedido -> {
            pedido.setObra(buscarObraPorId(pedido.getObraId()));
            pedido.getDetallePedido().forEach(dp -> {
                dp.setProducto(buscarProductoPorId(dp.getProductoId()));
            });
        }));
        return pedidosEncontrados;
    }

    @Override
    public Optional<List<Pedido>> buscarPedidosPorCliente(Integer id, String cuit) {
        JSONObject json = new JSONObject();
        JSONObject cliente = new JSONObject();

        if (cuit != null) {
            cliente.put("id", Objects.requireNonNullElse(id, -1));
            cliente.put("cuit", cuit);
            json.put("cliente", cliente);
        } else {
            if (id != null) {
                cliente.put("id", id);
                json.put("cliente", cliente);
            }
        }
        //WebClient webClient = WebClient.create("http://localhost:4040/api/obra");
        ResponseEntity<List<Obra>> response = webClient5.method(HttpMethod.GET)
                .uri("/obra")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(json))
                .retrieve()
                .toEntityList(Obra.class)
                .block();

        if (response != null && response.getStatusCode().equals(HttpStatus.OK)) {
            List<Obra> obras = response.getBody();
            HashSet<Pedido> pedidos = new HashSet<>();
            assert obras != null;
            for (Obra aux : obras) {
                pedidos.addAll(this.buscarPedidosPorIdObra(aux.getId()).orElseGet(ArrayList::new));
            }
            pedidos.forEach(pedido -> {
                pedido.setObra(buscarObraPorId(pedido.getObraId()));
                pedido.getDetallePedido().forEach(dp -> {
                    dp.setProducto(buscarProductoPorId(dp.getProductoId()));
                });
            });
            return Optional.of(new ArrayList<>(pedidos));
        }

        return Optional.empty();
    }

    @Override
    public Optional<List<Pedido>> buscarPedidosPorEstado(String estado) {
        if (pedidoRepository.buscarEstadoPedido(estado).isPresent()) {
            Optional<List<Pedido>> pedidosEncontrados = pedidoRepository.buscarPedidoPorEstado(estado);
            pedidosEncontrados.ifPresent(pedidos -> pedidos.forEach(pedido -> {
                pedido.setObra(buscarObraPorId(pedido.getObraId()));
                pedido.getDetallePedido().forEach(dp -> {
                    dp.setProducto(buscarProductoPorId(dp.getProductoId()));
                });
            }));
            return pedidosEncontrados;
        }
        return Optional.empty();
    }

    @Override
    public Optional<DetallePedido> buscarDetallePedidoPorId(Integer idPedido, Integer id) {
        Optional<DetallePedido> detalleEncontrado = detallePedidoRepository.findById(id);
        if (detalleEncontrado.isPresent() && detalleEncontrado.get().getPedido().getId().equals(idPedido)) {
            detalleEncontrado.get().setProducto(buscarProductoPorId(detalleEncontrado.get().getProductoId()));
        } else {
            return Optional.empty();
        }
        return detalleEncontrado;
    }

    @Override
    public Pedido crearPedido(Pedido nuevoPedido) {
        Pedido pedidoCreado = pedidoRepository.save(nuevoPedido);
        for (DetallePedido aux : nuevoPedido.getDetallePedido()) {
            aux.setPedido(pedidoCreado);
        }
        pedidoCreado.setDetallePedido(detallePedidoRepository.saveAll(nuevoPedido.getDetallePedido()));
        pedidoCreado.getDetallePedido().forEach(dp -> {
            dp.setProducto(buscarProductoPorId(dp.getProductoId()));
        });
        pedidoCreado.setObra(buscarObraPorId(pedidoCreado.getObraId()));
        return pedidoCreado;
    }

    @Override
    public DetallePedido crearDetallePedido(DetallePedido nuevoDetallePedido, Integer id) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isPresent() && pedidoOpt.get().getEstadoPedido().getId() == 1) {
            nuevoDetallePedido.setPedido(pedidoOpt.get());
            nuevoDetallePedido = detallePedidoRepository.save(nuevoDetallePedido);
            nuevoDetallePedido.setProducto(buscarProductoPorId(nuevoDetallePedido.getProductoId()));
            return nuevoDetallePedido;
        } else return null;
    }

    @Override
    public Optional<Pedido> actualizarPedido(Pedido nuevoPedido, Integer id) {
        Optional<Pedido> pedidoViejo = pedidoRepository.findById(id);
        if (pedidoViejo.isPresent()) {
            if (pedidoViejo.get().getEstadoPedido().getEstado().equals(nuevoPedido.getEstadoPedido().getEstado())) {
                Pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);
                pedidoGuardado.setObra(buscarObraPorId(pedidoGuardado.getObraId()));
                pedidoGuardado.getDetallePedido().forEach(dp -> {
                    dp.setProducto(buscarProductoPorId(dp.getProductoId()));
                });
                return Optional.of(pedidoGuardado);
            } else return Optional.empty();
        } else return Optional.empty();
    }

    @Override
    public Optional<DetallePedido> actualizarDetallePedido(DetallePedido nuevoDetalle, Integer idPedido, Integer id) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(idPedido);

        if (pedidoOpt.isPresent() && pedidoOpt.get().getEstadoPedido().getId() == 1 && detallePedidoRepository.existsById(id) && pedidoOpt.get().getDetallePedido().stream().anyMatch(dp -> dp.getId().equals(id))) {
            nuevoDetalle.setId(id);
            nuevoDetalle.setPedido(pedidoOpt.get());
            nuevoDetalle = detallePedidoRepository.save(nuevoDetalle);
            nuevoDetalle.setProducto(buscarProductoPorId(nuevoDetalle.getProductoId()));
            return Optional.of(nuevoDetalle);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Pedido> actualizarEstadoPedido(Integer id, String estado) {
        Optional<EstadoPedido> estadoPedidoOpt = pedidoRepository.buscarEstadoPedido(estado);
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isPresent() && estadoPedidoOpt.isPresent()) {
            //TODO hacer bien lo de los estados, por ahora solo pasa de NUEVO a CONFIRMADO, a ACEPTADO o PENDIENTE
            Pedido pedido = pedidoOpt.get();
            EstadoPedido estadoPedido = estadoPedidoOpt.get();
            //Si actualizo al estado que ya tengo, me voy nomas no hago nada
            if (pedido.getEstadoPedido().getId().equals(estadoPedido.getId())) {
                pedido.setObra(buscarObraPorId(pedido.getObraId()));
                return Optional.of(pedido);
            } else {
                switch (estadoPedido.getId()) {
                    case 1: {
                        return Optional.empty();
                    }
                    case 2: {
                        //TODO verificar saldo deudor
                        boolean hayStock = pedido.getDetallePedido().stream().allMatch(dp -> {
                            //WebClient webClient = WebClient.create("http://localhost:4042/api/producto/"+dp.getProductoId());
                            ResponseEntity<Producto> response = webClient3.method(HttpMethod.GET)
                                    .uri(dp.getProductoId().toString())
                                    .accept(MediaType.APPLICATION_JSON)
                                    .retrieve()
                                    .toEntity(Producto.class)
                                    .block();
                            if (response != null && response.getStatusCode().equals(HttpStatus.OK)) {
                                Producto p = response.getBody();
                                return p != null && p.getStockActual() >= dp.getCantidad();
                            }
                            return false;
                        });
                        if (hayStock) {
                            //TODO hacer movimiento de stock
                            //WebClient webClient = WebClient.create("http://localhost:4042/api/producto/mvms-stock");
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("detallePedido", pedido.getDetallePedido());
                            ResponseEntity<String> response = webClient4.method(HttpMethod.POST)
                                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                    .body(BodyInserters.fromValue(jsonObject))
                                    .retrieve()
                                    .toEntity(String.class)
                                    .block();
                            if (response != null && response.getStatusCode().equals(HttpStatus.OK)) {
                                pedido.setEstadoPedido(pedidoRepository.buscarEstadoPedido("ACEPTADO").orElse(null));
                            } else {
                                pedido.setEstadoPedido(pedidoRepository.buscarEstadoPedido("PENDIENTE").orElse(null));
                            }
                        } else {
                            pedido.setEstadoPedido(pedidoRepository.buscarEstadoPedido("PENDIENTE").orElse(null));
                        }
                        pedido = pedidoRepository.save(pedido);
                        pedido.setObra(buscarObraPorId(pedido.getObraId()));
                        pedido.getDetallePedido().forEach(dp -> {
                            dp.setProducto(buscarProductoPorId(dp.getProductoId()));
                        });
                        return Optional.of(pedido);
                    }
                    case 3: {
                        //TODO no se bien que hacer asique retorno vacio
                        return Optional.empty();
                    }
                    case 4: {
                        if (pedido.getEstadoPedido().getId() <= 3) {
                            pedido.setEstadoPedido(estadoPedido);
                            pedido = pedidoRepository.save(pedido);
                            pedido.setObra(buscarObraPorId(pedido.getObraId()));
                            pedido.getDetallePedido().forEach(dp -> {
                                dp.setProducto(buscarProductoPorId(dp.getProductoId()));
                            });
                            return Optional.of(pedido);
                        }
                        return Optional.empty();
                    }
                    case 5: {
                        //TODO no se bien que hacer asique retorno vacio
                        return Optional.empty();
                    }
                    case 6: {
                        //TODO no se bien que hacer asique retorno vacio
                        return Optional.empty();
                    }
                    case 7: {
                        //TODO no se bien que hacer asique retorno vacio
                        return Optional.empty();
                    }
                    case 8: {
                        //TODO no se bien que hacer asique retorno vacio
                        return Optional.empty();
                    }
                }
            }
            return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public Boolean borrarPedido(Integer id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Boolean borrarDetallePedido(Integer idPedido, Integer id) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(idPedido);
        if (pedidoOpt.isPresent() && pedidoOpt.get().getEstadoPedido().getId() == 1 && detallePedidoRepository.existsById(id) && pedidoOpt.get().getDetallePedido().stream().anyMatch(dp->dp.getId().equals(id))) {
            detallePedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Obra buscarObraPorId(Integer id) {
        //WebClient webClient = WebClient.create("http://localhost:4040/api/obra/"+id);
        ResponseEntity<Obra> response = webClient1.method(HttpMethod.GET)
                .uri(id.toString())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(Obra.class)
                .block();
        if (response != null && response.getStatusCode().equals(HttpStatus.OK)) {
            return response.getBody();
        }
        return null;
    }

    private Producto buscarProductoPorId(Integer id) {
        //WebClient webClient = WebClient.create("http://localhost:4042/api/producto/"+id);
        ResponseEntity<Producto> response = webClient2.method(HttpMethod.GET)
                .uri(id.toString())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(Producto.class)
                .block();
        if (response != null && response.getStatusCode().equals(HttpStatus.OK)) {
            return response.getBody();
        }
        return null;
    }
}
