package utn.frsf.isi.dan.grupotp.tplab.danmspedidos.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model.DetallePedido;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.model.Pedido;
import utn.frsf.isi.dan.grupotp.tplab.danmspedidos.services.PedidoService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedido")
public class PedidoRest {

    private final PedidoService pedidoService;

    @Autowired
    public PedidoRest(PedidoService pedidoService){
        this.pedidoService=pedidoService;
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> buscarTodos() {
        return ResponseEntity.ok(pedidoService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPedidoPorId(@PathVariable Integer id) {
        return ResponseEntity.of(pedidoService.buscarPedidoPorId(id));
    }

    @GetMapping("/obra/{id}")
    public ResponseEntity<List<Pedido>> buscarPedidosPorIdObra(@PathVariable Integer id){
        return ResponseEntity.of(pedidoService.buscarPedidosPorIdObra(id));
    }

    @GetMapping("/cliente")
    public ResponseEntity<List<Pedido>> buscarPedidosPorCliente(@RequestParam(required = false, name = "id") Integer id, @RequestParam(required = false, name = "cuit") String cuit){
        return ResponseEntity.of(pedidoService.buscarPedidosPorCliente(id, cuit));
    }

    @GetMapping("/{idPedido}/detalle/{id}")
    public ResponseEntity<DetallePedido> buscarDetallePedidoPorId(@PathVariable Integer idPedido, @PathVariable Integer id){
        return ResponseEntity.of(pedidoService.buscarDetallePedidoPorId(idPedido, id));
    }

    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@RequestBody Pedido nuevoPedido){
        return ResponseEntity.of(Optional.of(pedidoService.crearPedido(nuevoPedido)));
    }

    @PostMapping("/{id}/detalle")
    public ResponseEntity<DetallePedido> crearDetallePedido(@RequestBody DetallePedido nuevoDetallePedido, @PathVariable Integer id){
        return ResponseEntity.of(Optional.of(pedidoService.crearDetallePedido(nuevoDetallePedido, id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizarPedido(@RequestBody Pedido nuevoPedido, @PathVariable Integer id){
        return ResponseEntity.of(pedidoService.actualizarPedido(nuevoPedido, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Pedido> borrarPedido(@PathVariable Integer id){
        if(pedidoService.borrarPedido(id)){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{idPedido}/detalle/{id}")
    public ResponseEntity<DetallePedido> borrarDetallePedido(@PathVariable Integer idPedido, @PathVariable Integer id){
        if(pedidoService.borrarDetallePedido(idPedido,id)){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
