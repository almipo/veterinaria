// frontend/js/mascotas.js
// CRUD de Mascotas - Bootstrap 5 + JavaScript vanilla + Fetch API

const URL_MASCOTAS = "http://localhost:8080/api/mascotas";
const URL_DUENIOS = "http://localhost:8080/api/duenio";

let mascotas = [];
let duenios = [];
let mascotaEditandoId = null;


// ===============================
// REFERENCIAS DEL DOM
// ===============================

const tablaMascotas = document.getElementById("tablaMascotas");
const sinMascotas = document.getElementById("sinMascotas");

const formMascota = document.getElementById("formMascota");
const errorFormMascota = document.getElementById("errorFormMascota");

const modalMascotaEl = document.getElementById("modalMascota");
const btnNuevaMascota = document.getElementById("btnNuevaMascota");
const buscarMascotaId =
    document.getElementById(
        "buscarMascotaId"
    );

const btnBuscarMascota =
    document.getElementById(
        "btnBuscarMascota"
    );

const btnMostrarTodasMascotas =
    document.getElementById(
        "btnMostrarTodasMascotas"
    );
const modalMascota = modalMascotaEl
    ? bootstrap.Modal.getOrCreateInstance(modalMascotaEl)
    : null;


// ===============================
// FUNCIONES AUXILIARES
// ===============================

function escapar(texto) {

    if (texto === null || texto === undefined) {
        return "";
    }

    return String(texto)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}


function mostrarAlerta(mensaje, tipo = "info") {

    const alerta = document.getElementById("alerta");

    if (!alerta) {
        console.log(mensaje);
        return;
    }

    alerta.textContent = mensaje;
    alerta.className = "alert alert-" + tipo;
}


function limpiarErrorFormulario() {

    if (!errorFormMascota) {
        return;
    }

    errorFormMascota.textContent = "";
    errorFormMascota.classList.add("d-none");
}


function mostrarErrorFormulario(mensaje) {

    if (!errorFormMascota) {
        return;
    }

    errorFormMascota.textContent = mensaje;
    errorFormMascota.classList.remove("d-none");
}


// ===============================
// EXTRAER ERROR DEL BACKEND
// ===============================

async function extraerMensajeError(respuesta) {

    const texto = await respuesta.text().catch(() => "");

    if (!texto) {
        return "El servidor respondió con estado " + respuesta.status;
    }

    try {

        const cuerpo = JSON.parse(texto);

        // ErrorResponse del backend
        if (cuerpo.mensaje) {
            return cuerpo.mensaje;
        }

        if (cuerpo.message) {
            return cuerpo.message;
        }

        // Errores de validación
        if (cuerpo.errors && typeof cuerpo.errors === "object") {

            return Object.entries(cuerpo.errors)
                .map(([campo, mensaje]) => campo + ": " + mensaje)
                .join(" | ");
        }

        return texto;

    } catch {

        return texto;
    }
}


// ===============================
// FETCH GENÉRICO
// ===============================

async function fetchAPI(url, opciones = {}) {

    const respuesta = await fetch(url, opciones);

    if (!respuesta.ok) {

        const mensaje = await extraerMensajeError(respuesta);

        throw new Error(mensaje);
    }

    // DELETE normalmente devuelve 204 No Content
    if (respuesta.status === 204) {
        return null;
    }

    const texto = await respuesta.text();

    return texto
        ? JSON.parse(texto)
        : null;
}


// ===============================
// GET - LISTAR MASCOTAS
// ===============================

async function cargarMascotas() {

    try {

        mascotas = await fetchAPI(URL_MASCOTAS) || [];

        pintarTablaMascotas();

    } catch (error) {

        mostrarAlerta(
            "No se pudieron cargar las mascotas: " + error.message,
            "danger"
        );
    }
}

// ===============================
// BUSCAR MASCOTA POR ID
// ===============================

async function buscarMascotaPorId() {

    const valor =
        buscarMascotaId
            ?.value
            .trim();


    if (!valor) {

        mostrarAlerta(
            "Ingresá un ID para buscar.",
            "warning"
        );

        return;
    }


    const id =
        Number(valor);


    if (
        !Number.isInteger(id) ||
        id <= 0
    ) {

        mostrarAlerta(
            "El ID debe ser un número válido.",
            "warning"
        );

        return;
    }


    try {

        const mascota =
            await fetchAPI(
                URL_MASCOTAS
                + "/"
                + id
            );


        mascotas =
            mascota
                ? [mascota]
                : [];


        pintarTablaMascotas();


        mostrarAlerta(
            "Mascota encontrada.",
            "success"
        );


    } catch (error) {

        mascotas = [];

        pintarTablaMascotas();


        mostrarAlerta(
            "No se encontró la mascota con ID "
            + id
            + ".",
            "warning"
        );
    }
}
async function mostrarTodasMascotas() {

    if (buscarMascotaId) {

        buscarMascotaId.value = "";
    }


    await cargarMascotas();
}
// ===============================
// GET - CARGAR DUEÑOS
// ===============================

async function cargarDuenios() {

    try {

        duenios = await fetchAPI(URL_DUENIOS) || [];

        cargarComboDuenios();

    } catch (error) {

        mostrarAlerta(
            "No se pudieron cargar los dueños: " + error.message,
            "danger"
        );
    }
}


// ===============================
// COMBO DE DUEÑOS
// ===============================

function cargarComboDuenios() {

    const selectDuenio =
        document.getElementById("mascotaDuenio");

    if (!selectDuenio) {
        return;
    }

    // Conservamos el valor seleccionado,
    // por ejemplo cuando estamos editando.
    const valorActual = selectDuenio.value;

    selectDuenio.innerHTML =
        '<option value="">Seleccione un dueño...</option>';


    duenios.forEach(duenio => {

        const opcion =
            document.createElement("option");

        opcion.value = duenio.id;

        opcion.textContent =
            `${duenio.nombre ?? ""} ${duenio.apellido ?? ""}`.trim();

        selectDuenio.appendChild(opcion);
    });


    selectDuenio.value = valorActual;
}


// ===============================
// OBTENER NOMBRE DEL DUEÑO
// ===============================

function obtenerNombreDuenio(mascota) {

    const duenio = duenios.find(
        d =>
            Number(d.id) ===
            Number(mascota.duenioId)
    );


    if (duenio) {

        const nombreCompleto =
            `${duenio.nombre ?? ""} ${duenio.apellido ?? ""}`
                .trim();


        return `${nombreCompleto} (ID: ${duenio.id})`;
    }


    // Respaldo por si no encontramos el dueño
    // en la lista cargada.

    if (mascota.duenioNombre) {

        return `${mascota.duenioNombre} (ID: ${mascota.duenioId})`;
    }


    return mascota.duenioId
        ? `Dueño ID: ${mascota.duenioId}`
        : "";
}

// ===============================
// PINTAR TABLA
// ===============================

function pintarTablaMascotas() {

    if (!tablaMascotas) {
        return;
    }

    tablaMascotas.innerHTML = "";

    if (mascotas.length === 0) {

        if (sinMascotas) {
            sinMascotas.classList.remove("d-none");
        }

        return;
    }


    if (sinMascotas) {
        sinMascotas.classList.add("d-none");
    }


    mascotas.forEach(mascota => {

        const fila =
            document.createElement("tr");

        fila.innerHTML = `
            <td>${escapar(mascota.id)}</td>

            <td>${escapar(mascota.nombre)}</td>

            <td>${escapar(mascota.especie)}</td>

            <td>${escapar(mascota.raza)}</td>

            <td>${escapar(mascota.fechaNacimiento)}</td>

            <td>${escapar(obtenerNombreDuenio(mascota))}</td>

            <td class="text-end text-nowrap">

                <button
                    type="button"
                    class="btn btn-sm btn-warning me-1"
                    onclick="editarMascota(${Number(mascota.id)})">

                    Editar

                </button>

                <button
                    type="button"
                    class="btn btn-sm btn-danger"
                    onclick="eliminarMascota(${Number(mascota.id)})">

                    Eliminar

                </button>

            </td>
        `;

        tablaMascotas.appendChild(fila);
    });
}


// ===============================
// NUEVA MASCOTA
// ===============================

function prepararNuevaMascota() {

    mascotaEditandoId = null;

    if (formMascota) {
        formMascota.reset();
    }

    limpiarErrorFormulario();

    cargarComboDuenios();


    const titulo =
        document.getElementById("tituloModalMascota");

    const btnGuardar =
        document.getElementById("btnGuardarMascota");


    if (titulo) {
        titulo.textContent =
            "Registrar nueva mascota";
    }


    if (btnGuardar) {
        btnGuardar.textContent =
            "Guardar";
    }


    if (modalMascota) {
        modalMascota.show();
    }
}


// ===============================
// EDITAR MASCOTA
// ===============================

function editarMascota(id) {

    const mascota = mascotas.find(
        m => Number(m.id) === Number(id)
    );


    if (!mascota) {

        mostrarAlerta(
            "No se encontró la mascota seleccionada.",
            "danger"
        );

        return;
    }


    mascotaEditandoId = Number(id);

    limpiarErrorFormulario();

    cargarComboDuenios();


    document.getElementById(
        "tituloModalMascota"
    ).textContent = "Editar mascota";


    document.getElementById(
        "btnGuardarMascota"
    ).textContent = "Guardar cambios";


    document.getElementById(
        "mascotaNombre"
    ).value = mascota.nombre ?? "";


    document.getElementById(
        "mascotaEspecie"
    ).value = mascota.especie ?? "";


    document.getElementById(
        "mascotaRaza"
    ).value = mascota.raza ?? "";


    document.getElementById(
        "mascotaFechaNacimiento"
    ).value = mascota.fechaNacimiento ?? "";


    document.getElementById(
        "mascotaDuenio"
    ).value = mascota.duenioId ?? "";


    if (modalMascota) {
        modalMascota.show();
    }
}


// ===============================
// POST / PUT
// ===============================

async function guardarMascota(evento) {

    evento.preventDefault();

    limpiarErrorFormulario();


    const duenioId =
        Number(
            document.getElementById(
                "mascotaDuenio"
            ).value
        );


    // DTO de Mascota según el contrato real
    // que vimos en Swagger.
    const mascotaDTO = {

        nombre:
            document.getElementById(
                "mascotaNombre"
            ).value.trim(),

        especie:
            document.getElementById(
                "mascotaEspecie"
            ).value.trim(),

        raza:
            document.getElementById(
                "mascotaRaza"
            ).value.trim(),

        fechaNacimiento:
            document.getElementById(
                "mascotaFechaNacimiento"
            ).value,

        duenioId:
            duenioId
    };


    const editando =
        mascotaEditandoId !== null;


   const url = editando
    ? URL_MASCOTAS + "/" + mascotaEditandoId
    : URL_MASCOTAS
        + "?duenioId="
        + encodeURIComponent(duenioId);

    const metodo = editando
        ? "PUT"
        : "POST";


    try {

        await fetchAPI(url, {

            method: metodo,

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(mascotaDTO)
        });


        if (modalMascota) {
            modalMascota.hide();
        }


        formMascota.reset();

        mascotaEditandoId = null;


        mostrarAlerta(
            editando
                ? "Mascota actualizada correctamente."
                : "Mascota creada correctamente.",
            "success"
        );


        await cargarMascotas();


    } catch (error) {

        mostrarErrorFormulario(
            error.message
        );
    }
}


// ===============================
// DELETE
// ===============================

async function eliminarMascota(id) {

    const mascota = mascotas.find(
        m => Number(m.id) === Number(id)
    );


    const nombre = mascota
        ? mascota.nombre
        : "esta mascota";


    const confirmar =
        window.confirm(
            `¿Seguro que querés eliminar a ${nombre}?`
        );


    if (!confirmar) {
        return;
    }


    try {

        await fetchAPI(
            URL_MASCOTAS + "/" + id,
            {
                method: "DELETE"
            }
        );


        mostrarAlerta(
            "Mascota eliminada correctamente.",
            "success"
        );


        await cargarMascotas();


    } catch (error) {

        mostrarAlerta(
            "No se pudo eliminar la mascota: " +
            error.message,
            "danger"
        );
    }
}


// ===============================
// EVENTOS
// ===============================
if (btnBuscarMascota) {

    btnBuscarMascota.addEventListener(
        "click",
        buscarMascotaPorId
    );
}


if (btnMostrarTodasMascotas) {

    btnMostrarTodasMascotas.addEventListener(
        "click",
        mostrarTodasMascotas
    );
}


if (buscarMascotaId) {

    buscarMascotaId.addEventListener(
        "keydown",
        event => {

            if (event.key === "Enter") {

                event.preventDefault();

                buscarMascotaPorId();
            }
        }
    );
}
if (formMascota) {

    formMascota.addEventListener(
        "submit",
        guardarMascota
    );
}


if (btnNuevaMascota) {

    btnNuevaMascota.addEventListener(
        "click",
        prepararNuevaMascota
    );
}


if (modalMascotaEl) {

    modalMascotaEl.addEventListener(
        "hidden.bs.modal",
        () => {

            limpiarErrorFormulario();

            mascotaEditandoId = null;
        }
    );
}


// Los botones creados dentro de la tabla
// necesitan acceso global.

window.editarMascota =
    editarMascota;

window.eliminarMascota =
    eliminarMascota;


// ===============================
// INICIO
// ===============================

document.addEventListener(
    "DOMContentLoaded",
    async () => {

        // Primero cargamos los dueños para el combo.
        await cargarDuenios();

        // Después cargamos las mascotas.
        await cargarMascotas();
    }
);