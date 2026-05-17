document.addEventListener("DOMContentLoaded", function () {
  const menuToggle = document.getElementById("menu-toggle");
  const mobileMenu = document.getElementById("mobile-menu");

  if (menuToggle && mobileMenu) {
    menuToggle.addEventListener("click", function () {
      mobileMenu.classList.toggle("hidden");
    });
  }

  const selectProducto = document.getElementById("producto");
  if (selectProducto) {
    actualizarResumen();
  }
});

function filtrarPorCategoria(categoriaId) {
  categoriaId = parseInt(categoriaId);

  document.querySelectorAll(".filter-btn").forEach((btn) => {
    btn.classList.remove("active");
    if (parseInt(btn.dataset.category) === categoriaId) {
      btn.classList.add("active");
    }
  });

  const productos = document.querySelectorAll(".product-card");
  let visibles = 0;

  productos.forEach((prod) => {
    const catId = parseInt(prod.dataset.categoria);
    if (categoriaId === 0 || catId === categoriaId) {
      prod.style.display = "block";
      visibles++;
    } else {
      prod.style.display = "none";
    }
  });

  const mensajeVacio = document.getElementById("mensaje-vacio");
  if (mensajeVacio) {
    mensajeVacio.style.display = visibles === 0 ? "block" : "none";
  }
}

function aumentar() {
  const input = document.getElementById("cantidad");
  if (input && parseInt(input.value) < parseInt(input.max)) {
    input.value = parseInt(input.value) + 1;
  }
}

function disminuir() {
  const input = document.getElementById("cantidad");
  if (input && parseInt(input.value) > 1) {
    input.value = parseInt(input.value) - 1;
  }
}

function actualizarResumen() {
  const select = document.getElementById("producto");
  const inputCantidad = document.getElementById("cantidad");

  if (!select || !inputCantidad) return;

  const cantidad = parseInt(inputCantidad.value) || 1;

  if (select.value) {
    const option = select.options[select.selectedIndex];
    const precio = parseFloat(option.dataset.precio);
    const nombre = option.dataset.nombre;
    const subtotal = precio * cantidad;

    document.getElementById("resumen-nombre").textContent = nombre;
    document.getElementById("resumen-cantidad").textContent =
      "Cantidad: " + cantidad;
    document.getElementById("resumen-precio").textContent =
      "$" + subtotal.toFixed(2);
    document.getElementById("total-precio").textContent =
      "$" + subtotal.toFixed(2);
  } else {
    document.getElementById("resumen-nombre").textContent = "No seleccionado";
    document.getElementById("resumen-cantidad").textContent = "Cantidad: 0";
    document.getElementById("resumen-precio").textContent = "$0.00";
    document.getElementById("total-precio").textContent = "$0.00";
  }
}

function validarFormulario() {
  const inputNombre = document.getElementById("nombre");
  const inputCorreo = document.getElementById("correo");

  if (!inputNombre || !inputCorreo) return true;

  const nombre = inputNombre.value.trim();
  const correo = inputCorreo.value.trim();
  let valido = true;

  if (nombre.length < 3) {
    document.getElementById("error-nombre").textContent = "Mínimo 3 caracteres";
    inputNombre.classList.add("error");
    valido = false;
  } else {
    document.getElementById("error-nombre").textContent = "";
    inputNombre.classList.remove("error");
  }

  if (!correo.includes("@")) {
    document.getElementById("error-correo").textContent = "Correo inválido";
    inputCorreo.classList.add("error");
    valido = false;
  } else {
    document.getElementById("error-correo").textContent = "";
    inputCorreo.classList.remove("error");
  }

  return valido;
}

function irAlFormularioConDatos() {
  const prodId = document.querySelector('input[name="productoId"]').value;
  const cant = document.getElementById("cantidad").value;
  window.location.href = `/pedido/formulario?productoId=${prodId}&cantidad=${cant}`;
}
