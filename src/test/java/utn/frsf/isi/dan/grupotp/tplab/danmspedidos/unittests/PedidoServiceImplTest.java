package utn.frsf.isi.dan.grupotp.tplab.danmspedidos.unittests;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model.*;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.repository.DetallePedidoRepository;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.repository.PedidoRepository;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.services.implementation.PedidoServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PedidoServiceImplTest {
    @Autowired
    PedidoServiceImpl pedidoService;
    @MockBean
    PedidoRepository pedidoRepository;
    @MockBean
    DetallePedidoRepository detallePedidoRepository;
    @MockBean(name = "webClient1")
    WebClient webClient1;
    @MockBean(name = "webClient2")
    WebClient webClient2;
    @MockBean(name = "webClient3")
    WebClient webClient3;
    @MockBean(name = "webClient4")
    WebClient webClient4;
    @MockBean(name = "webClient5")
    WebClient webClient5;

    @Mock
    WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    WebClient.RequestBodySpec requestBodySpec;
    @SuppressWarnings("rawtypes")
    @Mock
    WebClient.RequestHeadersSpec requestHeadersSpec;
    @SuppressWarnings("rawtypes")
    @Mock
    WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    WebClient.ResponseSpec responseSpec;

    static List<Pedido> pedidos;
    static List<Obra> obras;

    @BeforeAll
    static void setUp() throws Exception{
        pedidos = new ArrayList<>();
        obras = new ArrayList<>();

        Pedido pedido = new Pedido();
        pedido.setId(1);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setObra(new Obra(1, "Descripcion Obra"));
        pedido.setObraId(1);

        pedido.setDetallePedido(new ArrayList<>());
        DetallePedido detallePedido = new DetallePedido();
        detallePedido.setId(1);
        detallePedido.setPedido(pedido);
        detallePedido.setProductoId(1);
        detallePedido.setProducto(new Producto(1, "A veces explota", 50.0, 650));
        detallePedido.setCantidad(50);
        detallePedido.setPrecio(2500.0);
        pedido.getDetallePedido().add(detallePedido);

        pedido.setEstadoPedido(new EstadoPedido(1, "NUEVO"));

        pedidos.add(pedido);
        obras.add(pedidos.get(0).getObra());
    }

    @BeforeEach
    void setEstados(){
        pedidos.get(0).setEstadoPedido(new EstadoPedido(1, "NUEVO"));
    }

    @Test
    void testBuscarTodos(){
        when(pedidoRepository.findAll()).thenReturn(pedidos);
        {
            when(webClient1.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(webClient2.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
            when(requestBodySpec.accept(any())).thenReturn(requestBodySpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.toEntity(Obra.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(pedidos.get(0).getObra()))));
            when(responseSpec.toEntity(Producto.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(pedidos.get(0).getDetallePedido().get(0).getProducto()))));
        }

        List<Pedido> pedidosEncontrados = pedidoService.buscarTodos();

        Assertions.assertEquals(pedidosEncontrados.size(),1);

        Mockito.verify(pedidoRepository, times(1)).findAll();

    }

    @Test
    void testBuscarPedidoPorIdExistenteyNoExistente(){
        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedidos.get(0)));
        when(pedidoRepository.findById(2)).thenReturn(Optional.empty());
        {
            when(webClient1.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(webClient2.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
            when(requestBodySpec.accept(any())).thenReturn(requestBodySpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.toEntity(Obra.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(pedidos.get(0).getObra()))));
            when(responseSpec.toEntity(Producto.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(pedidos.get(0).getDetallePedido().get(0).getProducto()))));
        }

        Optional<Pedido> pedido1 = pedidoService.buscarPedidoPorId(1);
        Optional<Pedido> pedido2 = pedidoService.buscarPedidoPorId(2);

        Assertions.assertTrue(pedido1.isPresent());
        Assertions.assertFalse(pedido2.isPresent());

        Mockito.verify(pedidoRepository, times(2)).findById(any(Integer.class));

    }

    @Test
    void testBuscarPedidoPorIdObraExistenteyNoExistente(){
        when(pedidoRepository.findByIdObra(1)).thenReturn(Optional.of(pedidos));
        when(pedidoRepository.findByIdObra(2)).thenReturn(Optional.empty());
        {
            when(webClient1.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(webClient2.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
            when(requestBodySpec.accept(any())).thenReturn(requestBodySpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.toEntity(Obra.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(pedidos.get(0).getObra()))));
            when(responseSpec.toEntity(Producto.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(pedidos.get(0).getDetallePedido().get(0).getProducto()))));
        }

        Optional<List<Pedido>> pedidos1 = pedidoService.buscarPedidosPorIdObra(1);
        Optional<List<Pedido>> pedidos2 = pedidoService.buscarPedidosPorIdObra(2);

        Assertions.assertTrue(pedidos1.isPresent());
        Assertions.assertFalse(pedidos2.isPresent());

        Mockito.verify(pedidoRepository, times(2)).findByIdObra(any(Integer.class));

    }

    @Test
    void testBuscarPedidoPorCliente(){
        when(pedidoRepository.findByIdObra(1)).thenReturn(Optional.of(pedidos));
        when(pedidoRepository.findByIdObra(2)).thenReturn(Optional.empty());
        {
            when(webClient1.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(webClient2.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(webClient5.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
            when(requestBodySpec.accept(any())).thenReturn(requestBodySpec);
            when(requestBodySpec.header(any(), any())).thenReturn(requestBodySpec);
            when(requestBodySpec.body(any())).thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.toEntityList(Obra.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(obras))));
            when(responseSpec.toEntity(Obra.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(pedidos.get(0).getObra()))));
            when(responseSpec.toEntity(Producto.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(pedidos.get(0).getDetallePedido().get(0).getProducto()))));
        }

        Optional<List<Pedido>>pedidos = pedidoService.buscarPedidosPorCliente(1,null);

        Assertions.assertTrue(pedidos.isPresent());

        Mockito.verify(pedidoRepository, times(1)).findByIdObra(any(Integer.class));
    }

    @Test
    void testBuscarPedidoPorEstado(){
        when(pedidoRepository.buscarEstadoPedido("nuevo")).thenReturn(Optional.of(pedidos.get(0).getEstadoPedido()));
        when(pedidoRepository.buscarEstadoPedido("aceptado")).thenReturn(Optional.of(new EstadoPedido(2, "CONFIRMADO")));
        when(pedidoRepository.buscarPedidoPorEstado("nuevo")).thenReturn(Optional.of(pedidos));
        when(pedidoRepository.buscarPedidoPorEstado("aceptado")).thenReturn(Optional.empty());
        {
            when(webClient1.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(webClient2.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
            when(requestBodySpec.accept(any())).thenReturn(requestBodySpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.toEntity(Obra.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(pedidos.get(0).getObra()))));
            when(responseSpec.toEntity(Producto.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(pedidos.get(0).getDetallePedido().get(0).getProducto()))));
        }

        Optional<List<Pedido>>pedidos1 = pedidoService.buscarPedidosPorEstado("nuevo");
        Optional<List<Pedido>>pedidos2 = pedidoService.buscarPedidosPorEstado("aceptado");

        Assertions.assertTrue(pedidos1.isPresent());
        Assertions.assertFalse(pedidos2.isPresent());

        Mockito.verify(pedidoRepository, times(2)).buscarEstadoPedido(any(String.class));
        Mockito.verify(pedidoRepository, times(2)).buscarPedidoPorEstado(any(String.class));
    }

    @Test
    void testBuscarDetallePedidoPorIdExistenteyNoExistente(){
        when(detallePedidoRepository.findById(1)).thenReturn(Optional.of(pedidos.get(0).getDetallePedido().get(0)));
        when(detallePedidoRepository.findById(2)).thenReturn(Optional.empty());
        {
            when(webClient2.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
            when(requestBodySpec.accept(any())).thenReturn(requestBodySpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.toEntity(Producto.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(pedidos.get(0).getDetallePedido().get(0).getProducto()))));
        }

        Optional<DetallePedido> detallePedido1 = pedidoService.buscarDetallePedidoPorId(1,1);
        Optional<DetallePedido> detallePedido2 = pedidoService.buscarDetallePedidoPorId(1,2);

        Assertions.assertTrue(detallePedido1.isPresent());
        Assertions.assertFalse(detallePedido2.isPresent());

        Mockito.verify(detallePedidoRepository, times(2)).findById(any(Integer.class));
    }

    @Test
    void testCrearPedido(){
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidos.get(0));
        when(detallePedidoRepository.saveAll((any()))).thenReturn(pedidos.get(0).getDetallePedido());
        {
            when(webClient2.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
            when(requestBodySpec.accept(any())).thenReturn(requestBodySpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.toEntity(Producto.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(pedidos.get(0).getDetallePedido().get(0).getProducto()))));
        }

        Pedido pedido = pedidoService.crearPedido(pedidos.get(0));

        Assertions.assertEquals(pedido.getEstadoPedido().getId(),1);

        Mockito.verify(pedidoRepository, times(1)).save(any(Pedido.class));
        Mockito.verify(detallePedidoRepository, times(1)).saveAll(any());
    }

    @Test
    void testCrearDetallePedido(){
        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedidos.get(0)));
        when(pedidoRepository.findById(2)).thenReturn(Optional.empty());
        when(detallePedidoRepository.save(any(DetallePedido.class))).thenReturn(pedidos.get(0).getDetallePedido().get(0));
        {
            when(webClient2.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
            when(requestBodySpec.accept(any())).thenReturn(requestBodySpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.toEntity(Producto.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(pedidos.get(0).getDetallePedido().get(0).getProducto()))));
        }

        DetallePedido detallePedido1 = pedidoService.crearDetallePedido(pedidos.get(0).getDetallePedido().get(0),1);
        DetallePedido detallePedido2 = pedidoService.crearDetallePedido(pedidos.get(0).getDetallePedido().get(0),2);

        Assertions.assertNotNull(detallePedido1);
        Assertions.assertNull(detallePedido2);

        Mockito.verify(pedidoRepository, times(2)).findById(any(Integer.class));
        Mockito.verify(detallePedidoRepository, times(1)).save(any(DetallePedido.class));
    }

    @Test
    void testActualizarPedidoExistenteyNoExistente(){
        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedidos.get(0)));
        when(pedidoRepository.findById(2)).thenReturn(Optional.empty());
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidos.get(0));
        {
            when(webClient1.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(webClient2.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
            when(requestBodySpec.accept(any())).thenReturn(requestBodySpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.toEntity(Obra.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(pedidos.get(0).getObra()))));
            when(responseSpec.toEntity(Producto.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(pedidos.get(0).getDetallePedido().get(0).getProducto()))));
        }

        Optional<Pedido> pedido1 = pedidoService.actualizarPedido(pedidos.get(0), 1);
        Optional<Pedido> pedido2 = pedidoService.actualizarPedido(pedidos.get(0), 2);

        Assertions.assertTrue(pedido1.isPresent());
        Assertions.assertFalse(pedido2.isPresent());

        Mockito.verify(pedidoRepository, times(2)).findById(any(Integer.class));
        Mockito.verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void testActualizarDetallePedidoExistenteyNoExistente(){
        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedidos.get(0)));
        when(pedidoRepository.findById(2)).thenReturn(Optional.empty());
        when(detallePedidoRepository.existsById(1)).thenReturn(true);
        when(detallePedidoRepository.existsById(2)).thenReturn(false);
        when(detallePedidoRepository.save(any(DetallePedido.class))).thenReturn(pedidos.get(0).getDetallePedido().get(0));
        {
            when(webClient2.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
            when(requestBodySpec.accept(any())).thenReturn(requestBodySpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.toEntity(Producto.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(pedidos.get(0).getDetallePedido().get(0).getProducto()))));
        }

        Optional<DetallePedido> detallePedido1 = pedidoService.actualizarDetallePedido(pedidos.get(0).getDetallePedido().get(0),1,1 );
        Optional<DetallePedido> detallePedido2 = pedidoService.actualizarDetallePedido(pedidos.get(0).getDetallePedido().get(0),1,2 );
        Optional<DetallePedido> detallePedido3 = pedidoService.actualizarDetallePedido(pedidos.get(0).getDetallePedido().get(0),2,1 );
        Optional<DetallePedido> detallePedido4 = pedidoService.actualizarDetallePedido(pedidos.get(0).getDetallePedido().get(0),2,2 );

        Assertions.assertTrue(detallePedido1.isPresent());
        Assertions.assertFalse(detallePedido2.isPresent());
        Assertions.assertFalse(detallePedido3.isPresent());
        Assertions.assertFalse(detallePedido4.isPresent());

        Mockito.verify(pedidoRepository, times(4)).findById(any(Integer.class));
        Mockito.verify(detallePedidoRepository, times(2)).existsById(any(Integer.class));
        Mockito.verify(detallePedidoRepository, times(1)).save(any(DetallePedido.class));
    }

    @Test
    void testActualizarEstadoPedidoaNuevoExistenteyNoExistente(){
        when(pedidoRepository.buscarEstadoPedido("nuevo")).thenReturn(Optional.of(new EstadoPedido(1, "NUEVO")));
        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedidos.get(0)));
        when(pedidoRepository.findById(2)).thenReturn(Optional.empty());
        {
            when(webClient1.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(webClient2.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(webClient3.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(webClient4.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
            when(requestBodySpec.accept(any())).thenReturn(requestBodySpec);
            when(requestBodySpec.body(any())).thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.toEntity(String.class)).thenReturn(Mono.just(ResponseEntity.ok("Exito")));
            when(responseSpec.toEntity(Obra.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(pedidos.get(0).getObra()))));
            when(responseSpec.toEntity(Producto.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(pedidos.get(0).getDetallePedido().get(0).getProducto()))));
        }

        Optional<Pedido>pedido1 = pedidoService.actualizarEstadoPedido(1, "nuevo");
        Optional<Pedido>pedido2 = pedidoService.actualizarEstadoPedido(2,"nuevo");

        Assertions.assertTrue(pedido1.isPresent());
        Assertions.assertEquals(pedido1.get().getEstadoPedido().getEstado(), "NUEVO");
        Assertions.assertFalse(pedido2.isPresent());

        Mockito.verify(pedidoRepository, times(2)).buscarEstadoPedido(any(String.class));
        Mockito.verify(pedidoRepository, times(2)).findById(any(Integer.class));
        Mockito.verify(pedidoRepository, times(0)).save(any(Pedido.class));
    }

    @Test
    void testConfirmarPedidoExistenteyNoExistenteConStock(){
        when(pedidoRepository.buscarEstadoPedido("nuevo")).thenReturn(Optional.of(new EstadoPedido(1, "NUEVO")));
        when(pedidoRepository.buscarEstadoPedido("confirmado")).thenReturn(Optional.of(new EstadoPedido(2, "CONFIRMADO")));
        when(pedidoRepository.buscarEstadoPedido("ACEPTADO")).thenReturn(Optional.of(new EstadoPedido(5, "ACEPTADO")));
        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedidos.get(0)));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidos.get(0));
        when(pedidoRepository.findById(2)).thenReturn(Optional.empty());
        {
            when(webClient1.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(webClient2.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(webClient3.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(webClient4.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
            when(requestBodyUriSpec.header(any(), any())).thenReturn(requestBodySpec);
            when(requestBodySpec.accept(any())).thenReturn(requestBodySpec);
            when(requestBodySpec.body(any())).thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.toEntity(String.class)).thenReturn(Mono.just(ResponseEntity.ok("Exito")));
            when(responseSpec.toEntity(Obra.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(pedidos.get(0).getObra()))));
            when(responseSpec.toEntity(Producto.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(pedidos.get(0).getDetallePedido().get(0).getProducto()))));
        }

        Optional<Pedido>pedido1 = pedidoService.actualizarEstadoPedido(1, "confirmado");
        Optional<Pedido>pedido2 = pedidoService.actualizarEstadoPedido(2,"confirmado");

        Assertions.assertTrue(pedido1.isPresent());
        Assertions.assertEquals(pedido1.get().getEstadoPedido().getEstado(), "ACEPTADO");
        Assertions.assertFalse(pedido2.isPresent());

        Mockito.verify(pedidoRepository, times(3)).buscarEstadoPedido(any(String.class));
        Mockito.verify(pedidoRepository, times(2)).findById(any(Integer.class));
        Mockito.verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void testConfirmarPedidoExistenteyNoExistenteSinStock(){
        when(pedidoRepository.buscarEstadoPedido("nuevo")).thenReturn(Optional.of(new EstadoPedido(1, "NUEVO")));
        when(pedidoRepository.buscarEstadoPedido("confirmado")).thenReturn(Optional.of(new EstadoPedido(2, "CONFIRMADO")));
        when(pedidoRepository.buscarEstadoPedido("PENDIENTE")).thenReturn(Optional.of(new EstadoPedido(3, "PENDIENTE")));
        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedidos.get(0)));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidos.get(0));
        when(pedidoRepository.findById(2)).thenReturn(Optional.empty());
        {
            when(webClient1.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(webClient2.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(webClient3.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
            when(requestBodySpec.accept(any())).thenReturn(requestBodySpec);
            when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.toEntity(Obra.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(pedidos.get(0).getObra()))));
            when(responseSpec.toEntity(Producto.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(new Producto(1, "A veces explota",50.0, 0)))));
        }

        Optional<Pedido>pedido1 = pedidoService.actualizarEstadoPedido(1, "confirmado");
        Optional<Pedido>pedido2 = pedidoService.actualizarEstadoPedido(2,"confirmado");

        Assertions.assertTrue(pedido1.isPresent());
        Assertions.assertEquals(pedido1.get().getEstadoPedido().getEstado(), "PENDIENTE");
        Assertions.assertFalse(pedido2.isPresent());

        Mockito.verify(pedidoRepository, times(3)).buscarEstadoPedido(any(String.class));
        Mockito.verify(pedidoRepository, times(2)).findById(any(Integer.class));
        Mockito.verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void testCancelarPedidoExistenteyNoExistente(){
        when(pedidoRepository.buscarEstadoPedido("nuevo")).thenReturn(Optional.of(new EstadoPedido(1, "NUEVO")));
        when(pedidoRepository.buscarEstadoPedido("cancelado")).thenReturn(Optional.of(new EstadoPedido(4, "CANCELADO")));
        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedidos.get(0)));
        when(pedidoRepository.findById(2)).thenReturn(Optional.empty());
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidos.get(0));
        {
            when(webClient1.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(webClient2.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
            when(requestBodySpec.accept(any())).thenReturn(requestBodySpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.toEntity(Obra.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(pedidos.get(0).getObra()))));
            when(responseSpec.toEntity(Producto.class)).thenReturn(Mono.just(ResponseEntity.of(Optional.of(pedidos.get(0).getDetallePedido().get(0).getProducto()))));
        }

        Optional<Pedido>pedido1 = pedidoService.actualizarEstadoPedido(1, "cancelado");
        Optional<Pedido>pedido2 = pedidoService.actualizarEstadoPedido(2, "cancelado");

        Assertions.assertTrue(pedido1.isPresent());
        Assertions.assertEquals(pedido1.get().getEstadoPedido().getEstado(), "CANCELADO");
        Assertions.assertFalse(pedido2.isPresent());

        Mockito.verify(pedidoRepository,times(2)).findById(any(Integer.class));
        Mockito.verify(pedidoRepository, times(2)).buscarEstadoPedido(any(String.class));
        Mockito.verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void testBorrarPedidoExistenteyNoExistente(){
        when(pedidoRepository.existsById(1)).thenReturn(true);
        when(pedidoRepository.existsById(2)).thenReturn(false);
        doNothing().when(pedidoRepository).deleteById(1);

        Assertions.assertTrue(pedidoService.borrarPedido(1));
        Assertions.assertFalse(pedidoService.borrarPedido(2));

        Mockito.verify(pedidoRepository,times(2)).existsById(any(Integer.class));
        Mockito.verify(pedidoRepository, times(1)).deleteById(any(Integer.class));
    }

    @Test
    void testBorrarDetallePedidoExistenteyNoExistente(){
        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedidos.get(0)));
        when(pedidoRepository.findById(2)).thenReturn(Optional.empty());
        when(detallePedidoRepository.existsById(1)).thenReturn(true);
        when(detallePedidoRepository.existsById(2)).thenReturn(false);
        doNothing().when(detallePedidoRepository).deleteById(1);

        Assertions.assertTrue(pedidoService.borrarDetallePedido(1,1));
        Assertions.assertFalse(pedidoService.borrarDetallePedido(1, 2));
        Assertions.assertFalse(pedidoService.borrarDetallePedido(2, 1));
        Assertions.assertFalse(pedidoService.borrarDetallePedido(2, 2));

        Mockito.verify(pedidoRepository, times(4)).findById(any(Integer.class));
        Mockito.verify(detallePedidoRepository,times(2)).existsById(any(Integer.class));
        Mockito.verify(detallePedidoRepository, times(1)).deleteById(any(Integer.class));

    }
}
