package utn.frsf.isi.dan.grupotp.tplab.danmspedidos.integrationtests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.MSPedidosApplication;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model.*;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.services.implementation.PedidoServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MSPedidosApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PedidoRestIntegrationTest {
    private final RestTemplate restTemplate = new RestTemplate();
    @LocalServerPort String port;
    static List<Pedido> pedidos;
    static List<Obra> obras;
    @MockBean
    PedidoServiceImpl pedidoService;

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

    @Test
    @SuppressWarnings("unchecked")
    void deberiaBuscarTodosLosPedidos(){
        when(pedidoService.buscarTodos()).thenReturn(pedidos);

        String server = "http://localhost:"+port+"/api/pedido";
        ResponseEntity<List<Pedido>> response = restTemplate.exchange(server, HttpMethod.GET, null, ((Class<List<Pedido>>)(Class<?>)List.class));

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().size(), 1);

        Mockito.verify(pedidoService, times(1)).buscarTodos();
    }

    @Test
    @SuppressWarnings("unchecked")
    void deberiaEncontrarListaVacia(){
        when(pedidoService.buscarTodos()).thenReturn(new ArrayList<>());

        String server = "http://localhost:"+port+"/api/pedido";
        ResponseEntity<List<Pedido>> response = restTemplate.exchange(server, HttpMethod.GET, null, ((Class<List<Pedido>>)(Class<?>)List.class));

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().size(), 0);

        Mockito.verify(pedidoService, times(1)).buscarTodos();
    }

    @Test
    void deberiaEncontrarUnPedidoPorId(){
        when(pedidoService.buscarPedidoPorId(1)).thenReturn(Optional.of(pedidos.get(0)));

        String server = "http://localhost:"+port+"/api/pedido/1";
        ResponseEntity<Pedido> response = restTemplate.exchange(server, HttpMethod.GET, null, Pedido.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), 1);

        Mockito.verify(pedidoService, times(1)).buscarPedidoPorId(any(Integer.class));
    }

    @Test
    void deberiaTirarUnaExcepcionPorPedidoNoExistente(){
        when(pedidoService.buscarPedidoPorId(2)).thenReturn(Optional.empty());

        String server = "http://localhost:"+port+"/api/pedido/2";

        Assertions.assertThrows(HttpClientErrorException.class, ()->{
            restTemplate.exchange(server, HttpMethod.GET, null, Pedido.class);
        });

        Mockito.verify(pedidoService, times(1)).buscarPedidoPorId(any(Integer.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void deberiaEncontrarPedidosPorIdObra(){
        when(pedidoService.buscarPedidosPorIdObra(1)).thenReturn(Optional.of(pedidos));

        String server = "http://localhost:"+port+"/api/pedido/obra/1";
        ResponseEntity<List<Pedido>> response = restTemplate.exchange(server, HttpMethod.GET, null, ((Class<List<Pedido>>)(Class<?>)List.class));

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().size(), 1);

        Mockito.verify(pedidoService, times(1)).buscarPedidosPorIdObra(any(Integer.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void deberiaTirarUnaExcepcionPorIdObraNoExistente(){
        when(pedidoService.buscarPedidosPorIdObra(2)).thenReturn(Optional.empty());

        String server = "http://localhost:"+port+"/api/pedido/obra/2";

        Assertions.assertThrows(HttpClientErrorException.class, ()->{
            restTemplate.exchange(server, HttpMethod.GET, null, ((Class<List<Pedido>>)(Class<?>)List.class));
        });

        Mockito.verify(pedidoService, times(1)).buscarPedidosPorIdObra(any(Integer.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void deberiaEncontrarPedidosPorCliente(){
        when(pedidoService.buscarPedidosPorCliente(eq(1), any())).thenReturn(Optional.of(pedidos));

        String server = "http://localhost:"+port+"/api/pedido/cliente?id=1";
        ResponseEntity<List<Pedido>> response = restTemplate.exchange(server, HttpMethod.GET, null, ((Class<List<Pedido>>)(Class<?>)List.class));

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().size(), 1);

        Mockito.verify(pedidoService, times(1)).buscarPedidosPorCliente(any(Integer.class), any());
    }

    @Test
    @SuppressWarnings("unchecked")
    void deberiaTirarUnaExcepcionPorClienteNoExistente(){
        when(pedidoService.buscarPedidosPorCliente(eq(2), any())).thenReturn(Optional.empty());

        String server = "http://localhost:"+port+"/api/pedido/cliente?id=2";

        Assertions.assertThrows(HttpClientErrorException.class, ()->{
            restTemplate.exchange(server, HttpMethod.GET, null, ((Class<List<Pedido>>)(Class<?>)List.class));
        });

        Mockito.verify(pedidoService, times(1)).buscarPedidosPorCliente(any(Integer.class), any());
    }

    @Test
    @SuppressWarnings("unchecked")
    void deberiaEncontrarPedidosPorEstado(){
        when(pedidoService.buscarPedidosPorEstado("nuevo")).thenReturn(Optional.of(pedidos));

        String server = "http://localhost:"+port+"/api/pedido/estado?estado=nuevo";
        ResponseEntity<List<Pedido>> response = restTemplate.exchange(server, HttpMethod.GET, null, ((Class<List<Pedido>>)(Class<?>)List.class));

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().size(), 1);

        Mockito.verify(pedidoService, times(1)).buscarPedidosPorEstado(any(String.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void deberiaTirarUnaExcepcionPorEstadoNoUsado(){
        when(pedidoService.buscarPedidosPorEstado("confirmado")).thenReturn(Optional.empty());

        String server = "http://localhost:"+port+"/api/pedido/estado?estado=confirmado";

        Assertions.assertThrows(HttpClientErrorException.class, ()->{
            restTemplate.exchange(server, HttpMethod.GET, null, ((Class<List<Pedido>>)(Class<?>)List.class));
        });

        Mockito.verify(pedidoService, times(1)).buscarPedidosPorEstado(any(String.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void deberiaTirarUnaExcepcionPorEstadoNoExistente(){
        when(pedidoService.buscarPedidosPorEstado("asdf")).thenReturn(Optional.empty());

        String server = "http://localhost:"+port+"/api/pedido/estado?estado=asdf";

        Assertions.assertThrows(HttpClientErrorException.class, ()->{
            restTemplate.exchange(server, HttpMethod.GET, null, ((Class<List<Pedido>>)(Class<?>)List.class));
        });

        Mockito.verify(pedidoService, times(1)).buscarPedidosPorEstado(any(String.class));
    }

    @Test
    @Disabled("Problemas con el mapeo de Jackson entre DetallePedido y Pedido. Con Insomnia/Postman anda")
    void deberiaEncontrarUnDetallePedidoPorId(){
        when(pedidoService.buscarDetallePedidoPorId(1,1)).thenReturn(Optional.of(pedidos.get(0).getDetallePedido().get(0)));

        String server = "http://localhost:"+port+"/api/pedido/1/detalle/1";
        ResponseEntity<DetallePedido> response = restTemplate.exchange(server, HttpMethod.GET, null, DetallePedido.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), 1);

        Mockito.verify(pedidoService, times(1)).buscarDetallePedidoPorId(any(Integer.class), any(Integer.class));
    }

    @Test
    void deberiaTirarUnaExcepcionPorDetallePedidoNoExistente(){
        when(pedidoService.buscarDetallePedidoPorId(1,2)).thenReturn(Optional.empty());

        String server = "http://localhost:"+port+"/api/pedido/1/detalle/2";

        Assertions.assertThrows(HttpClientErrorException.class, ()->{
            restTemplate.exchange(server, HttpMethod.GET, null, Pedido.class);
        });

        Mockito.verify(pedidoService, times(1)).buscarDetallePedidoPorId(any(Integer.class), any(Integer.class));
    }

    @Test
    void deberiaCrearElPedido(){
        when(pedidoService.crearPedido(any(Pedido.class))).thenReturn(pedidos.get(0));

        String server = "http://localhost:"+port+"/api/pedido";
        HttpEntity<Pedido> pedidoRequest = new HttpEntity<>(pedidos.get(0));

        ResponseEntity<Pedido> response = restTemplate.exchange(server, HttpMethod.POST, pedidoRequest, Pedido.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), 1);

        Mockito.verify(pedidoService, times(1)).crearPedido(any(Pedido.class));
    }

    @Test
    void deberiaTirarExcepcionPorObraNula(){
        String server = "http://localhost:"+port+"/api/pedido";
        Pedido aux = new Pedido();
        HttpEntity<Pedido> pedidoRequest = new HttpEntity<>(aux);

        Assertions.assertThrows(HttpClientErrorException.class, ()->{
            restTemplate.exchange(server, HttpMethod.POST, pedidoRequest, Pedido.class);
        });

        Mockito.verify(pedidoService, times(0)).crearPedido(any(Pedido.class));
    }

    @Test
    void deberiaTirarExcepcionPorIdObraNulo(){
        String server = "http://localhost:"+port+"/api/pedido";
        Pedido aux = new Pedido();
        aux.setObra(new Obra(1, "Descripcion Obra"));
        HttpEntity<Pedido> pedidoRequest = new HttpEntity<>(aux);

        Assertions.assertThrows(HttpClientErrorException.class, ()->{
            restTemplate.exchange(server, HttpMethod.POST, pedidoRequest, Pedido.class);
        });

        Mockito.verify(pedidoService, times(0)).crearPedido(any(Pedido.class));
    }

    @Test
    void deberiaTirarExcepcionPorDetallePedidoNulo(){
        String server = "http://localhost:"+port+"/api/pedido";
        Pedido aux = new Pedido();
        aux.setObra(new Obra(1, "Descripcion Obra"));
        aux.setObraId(1);
        HttpEntity<Pedido> pedidoRequest = new HttpEntity<>(aux);

        Assertions.assertThrows(HttpClientErrorException.class, ()->{
            restTemplate.exchange(server, HttpMethod.POST, pedidoRequest, Pedido.class);
        });

        Mockito.verify(pedidoService, times(0)).crearPedido(any(Pedido.class));
    }

    @Test
    void deberiaTirarExcepcionPorDetallePedidoVacio(){
        String server = "http://localhost:"+port+"/api/pedido";
        Pedido aux = new Pedido();
        aux.setObra(new Obra(1, "Descripcion Obra"));
        aux.setObraId(1);
        aux.setDetallePedido(new ArrayList<>());
        HttpEntity<Pedido> pedidoRequest = new HttpEntity<>(aux);

        Assertions.assertThrows(HttpClientErrorException.class, ()->{
            restTemplate.exchange(server, HttpMethod.POST, pedidoRequest, Pedido.class);
        });

        Mockito.verify(pedidoService, times(0)).crearPedido(any(Pedido.class));
    }

    @Test
    void deberiaTirarExcepcionPorProductoIdNulo(){
        String server = "http://localhost:"+port+"/api/pedido";
        Pedido aux = new Pedido();
        aux.setObra(new Obra(1, "Descripcion Obra"));
        aux.setObraId(1);
        aux.setDetallePedido(new ArrayList<>());
        DetallePedido aux2 = new DetallePedido();
        aux.getDetallePedido().add(aux2);
        HttpEntity<Pedido> pedidoRequest = new HttpEntity<>(aux);

        Assertions.assertThrows(HttpClientErrorException.class, ()->{
            restTemplate.exchange(server, HttpMethod.POST, pedidoRequest, Pedido.class);
        });

        Mockito.verify(pedidoService, times(0)).crearPedido(any(Pedido.class));
    }

    @Test
    void deberiaTirarExcepcionPorProductoNulo(){
        String server = "http://localhost:"+port+"/api/pedido";
        Pedido aux = new Pedido();
        aux.setObra(new Obra(1, "Descripcion Obra"));
        aux.setObraId(1);
        aux.setDetallePedido(new ArrayList<>());

        DetallePedido aux2 = new DetallePedido();
        aux2.setProductoId(1);

        aux.getDetallePedido().add(aux2);
        HttpEntity<Pedido> pedidoRequest = new HttpEntity<>(aux);

        Assertions.assertThrows(HttpClientErrorException.class, ()->{
            restTemplate.exchange(server, HttpMethod.POST, pedidoRequest, Pedido.class);
        });

        Mockito.verify(pedidoService, times(0)).crearPedido(any(Pedido.class));
    }

    @Test
    void deberiaTirarExcepcionPorCantidadNula(){
        String server = "http://localhost:"+port+"/api/pedido";
        Pedido aux = new Pedido();
        aux.setObra(new Obra(1, "Descripcion Obra"));
        aux.setObraId(1);
        aux.setDetallePedido(new ArrayList<>());

        DetallePedido aux2 = new DetallePedido();
        aux2.setProductoId(1);
        aux2.setProducto(new Producto(1, "A veces explota", 50.0, 100));

        aux.getDetallePedido().add(aux2);
        HttpEntity<Pedido> pedidoRequest = new HttpEntity<>(aux);

        Assertions.assertThrows(HttpClientErrorException.class, ()->{
            restTemplate.exchange(server, HttpMethod.POST, pedidoRequest, Pedido.class);
        });

        Mockito.verify(pedidoService, times(0)).crearPedido(any(Pedido.class));
    }

    @Test
    @Disabled("Denuevo problemas al mapear el DetallePedido a Json. Probar con Insomnia/Postman anda bien el metodo.")
    void deberiaCrearElDetallePedido(){
        when(pedidoService.crearDetallePedido(any(DetallePedido.class), eq(1))).thenReturn(pedidos.get(0).getDetallePedido().get(0));

        String server = "http://localhost:"+port+"/api/pedido/1/detalle";
        HttpEntity<DetallePedido> pedidoRequest = new HttpEntity<>(pedidos.get(0).getDetallePedido().get(0));

        ResponseEntity<DetallePedido> response = restTemplate.exchange(server, HttpMethod.POST, pedidoRequest, DetallePedido.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), 1);

        Mockito.verify(pedidoService, times(1)).crearDetallePedido(any(DetallePedido.class), any(Integer.class));
    }

}
