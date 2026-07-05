package com.minimarket.security.config;

import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.CategoriaRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.repository.RolRepository;
import com.minimarket.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public DataLoader(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder,
            CategoriaRepository categoriaRepository,
            ProductoRepository productoRepository) {

        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (rolRepository.count() == 0) {

            Rol administrador = new Rol();
            administrador.setNombre("ADMINISTRADOR");

            Rol empleado = new Rol();
            empleado.setNombre("EMPLEADO");

            Rol cliente = new Rol();
            cliente.setNombre("CLIENTE");

            rolRepository.save(administrador);
            rolRepository.save(empleado);
            rolRepository.save(cliente);

            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(
                    passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(administrador));

            Usuario vendedor = new Usuario();
            vendedor.setUsername("empleado");
            vendedor.setPassword(
                    passwordEncoder.encode("empleado123"));
            vendedor.setRoles(Set.of(empleado));

            Usuario clientePrueba = new Usuario();
            clientePrueba.setUsername("cliente");
            clientePrueba.setPassword(
                    passwordEncoder.encode("cliente123"));
            clientePrueba.setRoles(Set.of(cliente));

            usuarioRepository.save(admin);
            usuarioRepository.save(vendedor);
            usuarioRepository.save(clientePrueba);

            if (productoRepository.count() == 0) {
                Categoria bebidas = new Categoria();
                bebidas.setNombre("Bebidas");

                Categoria snacks = new Categoria();
                snacks.setNombre("Snacks");

                categoriaRepository.save(bebidas);
                categoriaRepository.save(snacks);

                Producto p1 = new Producto();
                p1.setNombre("Coca Cola 1.5L");
                p1.setPrecio(2490.0);
                p1.setStock(50);
                p1.setCategoria(bebidas);

                Producto p2 = new Producto();
                p2.setNombre("Agua Mineral 1L");
                p2.setPrecio(990.0);
                p2.setStock(80);
                p2.setCategoria(bebidas);

                Producto p3 = new Producto();
                p3.setNombre("Papas Fritas 150g");
                p3.setPrecio(1790.0);
                p3.setStock(35);
                p3.setCategoria(snacks);

                Producto p4 = new Producto();
                p4.setNombre("Chocolate");
                p4.setPrecio(1290.0);
                p4.setStock(40);
                p4.setCategoria(snacks);

                productoRepository.save(p1);
                productoRepository.save(p2);
                productoRepository.save(p3);
                productoRepository.save(p4);
            }

        }
    }
}