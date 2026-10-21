// ======================================================
// DUEÑOS
// CRUD + búsquedas + gestión de mascotas
// ======================================================

const API_BASE = "http://localhost:8080";

const URL_DUENIOS =
    API_BASE + "/api/duenio";

const URL_MASCOTAS =
    API_BASE + "/api/mascotas";


// ======================================================
// ESTADO
// ======================================================

let duenios = [];

let duenioEditandoId = null;

let duenioMascotasActualId = null;

let duenioMascotasActualNombre = "";

let mascotasDuenioActuales = [];

let mascotaEditandoId = null;


// ======================================================
// DOM - DUEÑOS
// ======================================================

const tablaDuenios =
    document.getElementById("tablaDuenios");

const sinDuenios =
    document.getElementById("sinDuenios");

const formDuenio =
    document.getElementById("formDuenio");

const errorFormDuenio =
    document.getElementById("errorFormDuenio");

const modalDuenioEl =
    document.getElementById("modalDuenio");

const btnNuevoDuenio =
    document.getElementById("btnNuevoDuenio");

const modalDuenio = modalDuenioEl
    ? bootstrap.Modal.getOrCreateInstance(modalDuenioEl)
    : null;


// ======================================================
// DOM - BUSCADOR
// ======================================================

const tipoBusquedaDuenio =
    document.getElementById("tipoBusquedaDuenio");

const valorBusquedaDuenio =
    document.getElementById("valorBusquedaDuenio");

const valorBusquedaApellido =
    document.getElementById("valorBusquedaApellido");

const grupoApellidoBusqueda =
    document.getElementById("grupoApellidoBusqueda");

const btnBuscarDuenio =
    document.getElementById("btnBuscarDuenio");

const btnMostrarTodosDuenios =
    document.getElementById("btnMostrarTodosDuenios");


// ======================================================
// DOM - MASCOTAS
// ======================================================

const modalMascotasDuenioEl =
    document.getElementById("modalMascotasDuenio");

const tituloMascotasDuenio =
    document.getElementById("tituloMascotasDuenio");

const alertaMascotasDuenio =
    document.getElementById("alertaMascotasDuenio");

const listaMascotasDuenio =
    document.getElementById("listaMascotasDuenio");

const sinMascotasDuenio =
    document.getElementById("sinMascotasDuenio");

const btnNuevaMascotaDuenio =
    document.getElementById("btnNuevaMascotaDuenio");

const panelFormMascotaDuenio =
    document.getElementById("panelFormMascotaDuenio");

const tituloFormMascotaDuenio =
    document.getElementById("tituloFormMascotaDuenio");

const formMascotaDuenio =
    document.getElementById("formMascotaDuenio");

const errorMascotaDuenio =
    document.getElementById("errorMascotaDuenio");

const btnGuardarMascotaDuenio =
    document.getElementById("btnGuardarMascotaDuenio");

const btnCancelarMascotaDuenio =
    document.getElementById("btnCancelarMascotaDuenio");


// ======================================================
// AUXILIARES
// ======================================================

function escapar(texto) {

    if (
        texto === null ||
        texto === undefined
    ) {
        return "";
    }

    return String(texto)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}


function mostrarAlerta(
    mensaje,
    tipo = "info"
) {

    const alerta =
        document.getElementById("alerta");

    if (!alerta) {
        return;
    }

    alerta.textContent = mensaje;

    alerta.className =
        "alert alert-" + tipo;
}


function ocultarAlerta() {

    const alerta =
        document.getElementById("alerta");

    if (!alerta) {
        return;
    }

    alerta.textContent = "";

    alerta.className = "d-none";
}


function mostrarAlertaMascotas(
    mensaje,
    tipo = "info"
) {

    if (!alertaMascotasDuenio) {
        return;
    }

    alertaMascotasDuenio.textContent =
        mensaje;

    alertaMascotasDuenio.className =
        "alert alert-" + tipo;
}


function ocultarAlertaMascotas() {

    if (!alertaMascotasDuenio) {
        return;
    }

    alertaMascotasDuenio.textContent = "";

    alertaMascotasDuenio.className =
        "d-none";
}


function limpiarErrorFormularioDuenio() {

    if (!errorFormDuenio) {
        return;
    }

    errorFormDuenio.textContent = "";

    errorFormDuenio.classList.add(
        "d-none"
    );
}


function mostrarErrorFormularioDuenio(
    mensaje
) {

    if (!errorFormDuenio) {
        return;
    }

    errorFormDuenio.textContent =
        mensaje;

    errorFormDuenio.classList.remove(
        "d-none"
    );
}


function limpiarErrorMascota() {

    if (!errorMascotaDuenio) {
        return;
    }

    errorMascotaDuenio.textContent = "";

    errorMascotaDuenio.classList.add(
        "d-none"
    );
}


function normalizarLista(resultado) {

    if (!resultado) {
        return [];
    }

    if (Array.isArray(resultado)) {
        return resultado;
    }

    return [resultado];
}


// ======================================================
// ERRORES DEL BACKEND
// ======================================================

async function extraerMensajeError(
    respuesta
) {

    const texto =
        await respuesta.text()
            .catch(() => "");


    if (!texto) {

        return (
            "El servidor respondió con estado "
            + respuesta.status
        );
    }


    try {

        const cuerpo =
            JSON.parse(texto);


        if (cuerpo.mensaje) {
            return cuerpo.mensaje;
        }


        if (cuerpo.message) {
            return cuerpo.message;
        }


        if (
            cuerpo.errors &&
            typeof cuerpo.errors === "object"
        ) {

            return Object.entries(
                cuerpo.errors
            )
                .map(
                    ([campo, mensaje]) =>
                        campo + ": " + mensaje
                )
                .join(" | ");
        }


        return texto;

    } catch {

        return texto;
    }
}


// ======================================================
// FETCH GENÉRICO
// ======================================================

async function api(
    url,
    opciones = {}
) {

    const respuesta =
        await fetch(url, opciones);


    if (!respuesta.ok) {

        const mensaje =
            await extraerMensajeError(
                respuesta
            );

        throw new Error(mensaje);
    }


    if (respuesta.status === 204) {
        return null;
    }


    const texto =
        await respuesta.text();


    if (!texto) {
        return null;
    }


    try {

        return JSON.parse(texto);

    } catch {

        return texto;
    }
}


// ======================================================
// LISTAR DUEÑOS
// ======================================================

async function cargarDuenios() {

    try {

        const resultado =
            await api(URL_DUENIOS);


        duenios =
            normalizarLista(resultado);


        pintarTablaDuenios(
            duenios
        );


    } catch (error) {

        mostrarAlerta(
            "No se pudieron cargar los dueños: "
            + error.message,
            "danger"
        );
    }
}


// ======================================================
// PINTAR TABLA DUEÑOS
// ======================================================

function pintarTablaDuenios(
    lista = duenios
) {

    if (!tablaDuenios) {
        return;
    }


    tablaDuenios.innerHTML = "";


    if (
        !lista ||
        lista.length === 0
    ) {

        if (sinDuenios) {

            sinDuenios.classList.remove(
                "d-none"
            );
        }

        return;
    }


    if (sinDuenios) {

        sinDuenios.classList.add(
            "d-none"
        );
    }


    lista.forEach(duenio => {

        const fila =
            document.createElement("tr");


        fila.innerHTML = `

            <td>
                ${escapar(duenio.id)}
            </td>

            <td>
                ${escapar(duenio.nombre)}
            </td>

            <td>
                ${escapar(duenio.apellido)}
            </td>

            <td>
                ${escapar(duenio.cedula)}
            </td>

            <td>
                ${escapar(duenio.telefono)}
            </td>

            <td>
                ${escapar(duenio.email)}
            </td>

            <td class="text-end text-nowrap">

                <button
                    type="button"
                    class="btn btn-sm btn-info me-1"
                    onclick="verMascotasDuenio(${Number(duenio.id)})"
                >
                    Ver mascotas
                </button>

                <button
                    type="button"
                    class="btn btn-sm btn-warning me-1"
                    onclick="editarDuenio(${Number(duenio.id)})"
                >
                    Editar
                </button>

                <button
                    type="button"
                    class="btn btn-sm btn-danger"
                    onclick="eliminarDuenio(${Number(duenio.id)})"
                >
                    Eliminar
                </button>

            </td>
        `;


        tablaDuenios.appendChild(
            fila
        );
    });
}


// ======================================================
// NUEVO DUEÑO
// ======================================================

function prepararNuevoDuenio() {

    duenioEditandoId = null;


    if (formDuenio) {
        formDuenio.reset();
    }


    limpiarErrorFormularioDuenio();


    const titulo =
        document.getElementById(
            "tituloModalDuenio"
        );


    const boton =
        document.getElementById(
            "btnGuardarDuenio"
        );


    if (titulo) {

        titulo.textContent =
            "Registrar nuevo dueño";
    }


    if (boton) {

        boton.textContent =
            "Guardar";
    }


    if (modalDuenio) {

        modalDuenio.show();
    }
}


// ======================================================
// EDITAR DUEÑO
// ======================================================

async function editarDuenio(id) {

    try {

        const duenio =
            await api(
                URL_DUENIOS
                + "/"
                + id
            );


        duenioEditandoId =
            Number(id);


        document.getElementById(
            "tituloModalDuenio"
        ).textContent =
            "Editar dueño";


        document.getElementById(
            "btnGuardarDuenio"
        ).textContent =
            "Guardar cambios";


        document.getElementById(
            "duenioNombre"
        ).value =
            duenio.nombre ?? "";


        document.getElementById(
            "duenioApellido"
        ).value =
            duenio.apellido ?? "";


        document.getElementById(
            "duenioCedula"
        ).value =
            duenio.cedula ?? "";


        document.getElementById(
            "duenioTelefono"
        ).value =
            duenio.telefono ?? "";


        document.getElementById(
            "duenioEmail"
        ).value =
            duenio.email ?? "";


        limpiarErrorFormularioDuenio();


        if (modalDuenio) {
            modalDuenio.show();
        }


    } catch (error) {

        mostrarAlerta(
            "No se pudo cargar el dueño: "
            + error.message,
            "danger"
        );
    }
}


// ======================================================
// GUARDAR DUEÑO
// POST / PUT
// ======================================================

async function guardarDuenio(
    evento
) {

    evento.preventDefault();


    limpiarErrorFormularioDuenio();


    const telefonoValor =
        document.getElementById(
            "duenioTelefono"
        ).value;


    const duenioDTO = {

        nombre:
            document.getElementById(
                "duenioNombre"
            ).value.trim(),

        apellido:
            document.getElementById(
                "duenioApellido"
            ).value.trim(),

        cedula:
            document.getElementById(
                "duenioCedula"
            ).value.trim(),

        telefono:
            telefonoValor
                ? Number(telefonoValor)
                : null,

        email:
            document.getElementById(
                "duenioEmail"
            ).value.trim()
    };


    const editando =
        duenioEditandoId !== null;


    const url =
        editando

            ? URL_DUENIOS
                + "/"
                + duenioEditandoId

            : URL_DUENIOS;


    try {

        await api(
            url,
            {

                method:
                    editando
                        ? "PUT"
                        : "POST",

                headers: {
                    "Content-Type":
                        "application/json"
                },

                body:
                    JSON.stringify(
                        duenioDTO
                    )
            }
        );


        if (modalDuenio) {

            modalDuenio.hide();
        }


        duenioEditandoId = null;


        mostrarAlerta(

            editando
                ? "Dueño actualizado correctamente."
                : "Dueño creado correctamente.",

            "success"
        );


        await cargarDuenios();


    } catch (error) {

        mostrarErrorFormularioDuenio(
            error.message
        );
    }
}


// ======================================================
// ELIMINAR DUEÑO
// ======================================================

async function eliminarDuenio(id) {

    const duenio =
        duenios.find(
            d =>
                Number(d.id)
                ===
                Number(id)
        );


    const nombre =
        duenio

            ? `${duenio.nombre ?? ""} ${duenio.apellido ?? ""}`.trim()

            : "este dueño";


    const confirmar =
        window.confirm(
            `¿Seguro que querés eliminar a ${nombre}?`
        );


    if (!confirmar) {
        return;
    }


    try {

        await api(

            URL_DUENIOS
            + "/"
            + id,

            {
                method: "DELETE"
            }
        );


        mostrarAlerta(
            "Dueño eliminado correctamente.",
            "success"
        );


        await cargarDuenios();


    } catch (error) {

        mostrarAlerta(
            "No se pudo eliminar el dueño: "
            + error.message,
            "danger"
        );
    }
}


// ======================================================
// BÚSQUEDA DE DUEÑOS
// ======================================================

async function ejecutarBusquedaDuenios(
    url
) {

    try {

        const resultado =
            await api(url);


        const lista =
            normalizarLista(
                resultado
            );


        pintarTablaDuenios(lista);


        if (lista.length === 0) {

            mostrarAlerta(
                "No se encontraron dueños.",
                "warning"
            );

        } else {

            mostrarAlerta(
                `Se encontraron ${lista.length} resultado(s).`,
                "success"
            );
        }


    } catch (error) {

        pintarTablaDuenios([]);


        mostrarAlerta(
            "No se pudo realizar la búsqueda: "
            + error.message,
            "danger"
        );
    }
}


async function buscarDuenio() {

    if (
        !tipoBusquedaDuenio ||
        !valorBusquedaDuenio
    ) {
        return;
    }


    const tipo =
        tipoBusquedaDuenio.value;


    const valor =
        valorBusquedaDuenio
            .value
            .trim();


    if (!valor) {

        mostrarAlerta(
            "Ingresá un valor para buscar.",
            "warning"
        );

        return;
    }


    if (tipo === "id") {

        const id =
            Number(valor);


        if (
            !Number.isInteger(id) ||
            id <= 0
        ) {

            mostrarAlerta(
                "El ID debe ser válido.",
                "warning"
            );

            return;
        }


        await ejecutarBusquedaDuenios(
            URL_DUENIOS
            + "/"
            + id
        );

        return;
    }


    if (tipo === "nombre") {

        await ejecutarBusquedaDuenios(

            URL_DUENIOS
            + "/nombre/"
            + encodeURIComponent(valor)
        );

        return;
    }


    if (tipo === "apellido") {

        await ejecutarBusquedaDuenios(

            URL_DUENIOS
            + "/apellido/"
            + encodeURIComponent(valor)
        );

        return;
    }


    if (tipo === "email") {

        await ejecutarBusquedaDuenios(

            URL_DUENIOS
            + "/email/"
            + encodeURIComponent(valor)
        );

        return;
    }


    if (tipo === "nombreApellido") {

        const apellido =
            valorBusquedaApellido
                ?.value
                .trim();


        if (!apellido) {

            mostrarAlerta(
                "Ingresá también el apellido.",
                "warning"
            );

            return;
        }


        const parametros =
            new URLSearchParams({
                nombre: valor,
                apellido: apellido
            });


        await ejecutarBusquedaDuenios(

            URL_DUENIOS
            + "/buscar?"
            + parametros.toString()
        );
    }
}


function actualizarFormularioBusqueda() {

    if (
        !tipoBusquedaDuenio ||
        !valorBusquedaDuenio
    ) {
        return;
    }


    const tipo =
        tipoBusquedaDuenio.value;


    if (tipo === "nombreApellido") {

        grupoApellidoBusqueda
            ?.classList
            .remove("d-none");


        valorBusquedaDuenio.placeholder =
            "Ingrese el nombre";

        return;
    }


    grupoApellidoBusqueda
        ?.classList
        .add("d-none");


    if (valorBusquedaApellido) {

        valorBusquedaApellido.value = "";
    }


    const placeholders = {

        id:
            "Ingrese el ID",

        nombre:
            "Ingrese el nombre",

        apellido:
            "Ingrese el apellido",

        email:
            "Ingrese el email"
    };


    valorBusquedaDuenio.placeholder =
        placeholders[tipo]
        ?? "Ingrese el dato";
}


async function mostrarTodosDuenios() {

    if (valorBusquedaDuenio) {

        valorBusquedaDuenio.value = "";
    }


    if (valorBusquedaApellido) {

        valorBusquedaApellido.value = "";
    }


    ocultarAlerta();


    await cargarDuenios();
}


// ======================================================
// MASCOTAS - LISTAR
// ======================================================

async function cargarMascotasDelDuenio() {

    if (!duenioMascotasActualId) {
        return;
    }


    try {

        const resultado =
            await api(

                URL_DUENIOS
                + "/"
                + duenioMascotasActualId
                + "/mascotas"
            );


        mascotasDuenioActuales =
            normalizarLista(
                resultado
            );


        pintarMascotasDuenio();


    } catch (error) {

        mascotasDuenioActuales = [];


        pintarMascotasDuenio();


        mostrarAlertaMascotas(
            "No se pudieron cargar las mascotas: "
            + error.message,
            "danger"
        );
    }
}


function pintarMascotasDuenio() {

    if (!listaMascotasDuenio) {
        return;
    }


    listaMascotasDuenio.innerHTML =
        "";


    if (
        mascotasDuenioActuales.length
        === 0
    ) {

        sinMascotasDuenio
            ?.classList
            .remove("d-none");

        return;
    }


    sinMascotasDuenio
        ?.classList
        .add("d-none");


    mascotasDuenioActuales.forEach(
        mascota => {

            const fila =
                document.createElement(
                    "tr"
                );


            fila.innerHTML = `

                <td>
                    ${escapar(mascota.id)}
                </td>

                <td>
                    ${escapar(mascota.nombre)}
                </td>

                <td>
                    ${escapar(mascota.especie)}
                </td>

                <td>
                    ${escapar(mascota.raza)}
                </td>

                <td>
                    ${escapar(
                        mascota.fechaNacimiento
                        ?? ""
                    )}
                </td>

                <td class="text-end text-nowrap">

                    <button
                        type="button"
                        class="btn btn-sm btn-warning me-1"
                        onclick="editarMascotaDuenio(${Number(mascota.id)})"
                    >
                        Editar
                    </button>

                    <button
                        type="button"
                        class="btn btn-sm btn-danger"
                        onclick="eliminarMascotaDuenio(${Number(mascota.id)})"
                    >
                        Eliminar
                    </button>

                </td>
            `;


            listaMascotasDuenio
                .appendChild(fila);
        }
    );
}


// ======================================================
// ABRIR MASCOTAS DEL DUEÑO
// ======================================================

async function verMascotasDuenio(id) {

    duenioMascotasActualId =
        Number(id);

    mascotaEditandoId = null;


    ocultarFormularioMascota();

    ocultarAlertaMascotas();


    try {

        const duenio =
            await api(

                URL_DUENIOS
                + "/"
                + id
            );


        duenioMascotasActualNombre =
            `${duenio.nombre ?? ""} ${duenio.apellido ?? ""}`
                .trim();


        if (tituloMascotasDuenio) {

            tituloMascotasDuenio.textContent =
                "Mascotas de "
                + duenioMascotasActualNombre;
        }


        await cargarMascotasDelDuenio();


        if (modalMascotasDuenioEl) {

            bootstrap.Modal
                .getOrCreateInstance(
                    modalMascotasDuenioEl
                )
                .show();
        }


    } catch (error) {

        mostrarAlerta(
            "No se pudieron cargar las mascotas: "
            + error.message,
            "danger"
        );
    }
}


// ======================================================
// FORMULARIO MASCOTA
// ======================================================

function mostrarFormularioMascota() {

    panelFormMascotaDuenio
        ?.classList
        .remove("d-none");
}


function ocultarFormularioMascota() {

    panelFormMascotaDuenio
        ?.classList
        .add("d-none");


    formMascotaDuenio
        ?.reset();


    limpiarErrorMascota();


    mascotaEditandoId = null;
}


function nuevaMascotaDuenio() {

    mascotaEditandoId = null;


    formMascotaDuenio
        ?.reset();


    limpiarErrorMascota();


    if (tituloFormMascotaDuenio) {

        tituloFormMascotaDuenio.textContent =
            "Nueva mascota";
    }


    if (btnGuardarMascotaDuenio) {

        btnGuardarMascotaDuenio.textContent =
            "Guardar";
    }


    mostrarFormularioMascota();
}


// ======================================================
// EDITAR MASCOTA
// ======================================================

async function editarMascotaDuenio(id) {

    ocultarAlertaMascotas();

    limpiarErrorMascota();


    try {

        const mascota =
            await api(

                URL_MASCOTAS
                + "/"
                + id
            );


        mascotaEditandoId =
            Number(id);


        if (tituloFormMascotaDuenio) {

            tituloFormMascotaDuenio.textContent =
                "Editar mascota";
        }


        if (btnGuardarMascotaDuenio) {

            btnGuardarMascotaDuenio.textContent =
                "Guardar cambios";
        }


        document.getElementById(
            "mascotaDuenioNombre"
        ).value =
            mascota.nombre ?? "";


        document.getElementById(
            "mascotaDuenioEspecie"
        ).value =
            mascota.especie ?? "";


        document.getElementById(
            "mascotaDuenioRaza"
        ).value =
            mascota.raza ?? "";


        document.getElementById(
            "mascotaDuenioFecha"
        ).value =
            mascota.fechaNacimiento ?? "";


        mostrarFormularioMascota();


    } catch (error) {

        mostrarAlertaMascotas(
            "No se pudo cargar la mascota: "
            + error.message,
            "danger"
        );
    }
}


// ======================================================
// GUARDAR MASCOTA
// POST / PUT
// ======================================================

async function guardarMascotaDuenio(
    evento
) {

    evento.preventDefault();


    limpiarErrorMascota();


    if (!duenioMascotasActualId) {

        mostrarAlertaMascotas(
            "No hay un dueño seleccionado.",
            "danger"
        );

        return;
    }


    const mascotaDTO = {

        nombre:
            document.getElementById(
                "mascotaDuenioNombre"
            ).value.trim(),

        especie:
            document.getElementById(
                "mascotaDuenioEspecie"
            ).value.trim(),

        raza:
            document.getElementById(
                "mascotaDuenioRaza"
            ).value.trim(),

        fechaNacimiento:
            document.getElementById(
                "mascotaDuenioFecha"
            ).value,

        duenioId:
            duenioMascotasActualId
    };


    const editando =
        mascotaEditandoId !== null;


   const url =
    editando

        ? URL_MASCOTAS
            + "/"
            + mascotaEditandoId

        : URL_MASCOTAS
            + "?duenioId="
            + encodeURIComponent(
                duenioMascotasActualId
            );


    try {

        await api(
            url,
            {

                method:
                    editando
                        ? "PUT"
                        : "POST",

                headers: {

                    "Content-Type":
                        "application/json"
                },

                body:
                    JSON.stringify(
                        mascotaDTO
                    )
            }
        );


        ocultarFormularioMascota();


        await cargarMascotasDelDuenio();


        mostrarAlertaMascotas(

            editando
                ? "Mascota actualizada correctamente."
                : "Mascota registrada correctamente.",

            "success"
        );


    } catch (error) {

        if (errorMascotaDuenio) {

            errorMascotaDuenio.textContent =
                error.message;

            errorMascotaDuenio.classList.remove(
                "d-none"
            );
        }
    }
}


// ======================================================
// ELIMINAR MASCOTA
// ======================================================

async function eliminarMascotaDuenio(id) {

    const mascota =
        mascotasDuenioActuales.find(

            m =>
                Number(m.id)
                ===
                Number(id)
        );


    const nombre =
        mascota?.nombre
        ?? "esta mascota";


    const confirmar =
        window.confirm(

            `¿Seguro que querés eliminar a ${nombre}?`
        );


    if (!confirmar) {
        return;
    }


    try {

        await api(

            URL_MASCOTAS
            + "/"
            + id,

            {
                method: "DELETE"
            }
        );


        await cargarMascotasDelDuenio();


        mostrarAlertaMascotas(
            "Mascota eliminada correctamente.",
            "success"
        );


    } catch (error) {

        mostrarAlertaMascotas(
            "No se pudo eliminar la mascota: "
            + error.message,
            "danger"
        );
    }
}


// ======================================================
// EVENTOS DUEÑOS
// ======================================================

if (formDuenio) {

    formDuenio.addEventListener(
        "submit",
        guardarDuenio
    );
}


if (btnNuevoDuenio) {

    btnNuevoDuenio.addEventListener(
        "click",
        prepararNuevoDuenio
    );
}


if (modalDuenioEl) {

    modalDuenioEl.addEventListener(
        "hidden.bs.modal",
        () => {

            limpiarErrorFormularioDuenio();

            duenioEditandoId = null;
        }
    );
}


// ======================================================
// EVENTOS BUSCADOR
// ======================================================

if (tipoBusquedaDuenio) {

    tipoBusquedaDuenio.addEventListener(
        "change",
        actualizarFormularioBusqueda
    );
}


if (btnBuscarDuenio) {

    btnBuscarDuenio.addEventListener(
        "click",
        buscarDuenio
    );
}


if (btnMostrarTodosDuenios) {

    btnMostrarTodosDuenios.addEventListener(
        "click",
        mostrarTodosDuenios
    );
}


if (valorBusquedaDuenio) {

    valorBusquedaDuenio.addEventListener(
        "keydown",
        event => {

            if (event.key === "Enter") {

                event.preventDefault();

                buscarDuenio();
            }
        }
    );
}


if (valorBusquedaApellido) {

    valorBusquedaApellido.addEventListener(
        "keydown",
        event => {

            if (event.key === "Enter") {

                event.preventDefault();

                buscarDuenio();
            }
        }
    );
}


// ======================================================
// EVENTOS MASCOTAS
// ======================================================

if (btnNuevaMascotaDuenio) {

    btnNuevaMascotaDuenio.addEventListener(
        "click",
        nuevaMascotaDuenio
    );
}


if (btnCancelarMascotaDuenio) {

    btnCancelarMascotaDuenio.addEventListener(
        "click",
        ocultarFormularioMascota
    );
}


if (formMascotaDuenio) {

    formMascotaDuenio.addEventListener(
        "submit",
        guardarMascotaDuenio
    );
}


// ======================================================
// FUNCIONES GLOBALES
// ======================================================

window.editarDuenio =
    editarDuenio;

window.eliminarDuenio =
    eliminarDuenio;

window.verMascotasDuenio =
    verMascotasDuenio;

window.editarMascotaDuenio =
    editarMascotaDuenio;

window.eliminarMascotaDuenio =
    eliminarMascotaDuenio;


// ======================================================
// INICIO
// ======================================================

document.addEventListener(
    "DOMContentLoaded",
    async () => {

        actualizarFormularioBusqueda();

        await cargarDuenios();
    }
);