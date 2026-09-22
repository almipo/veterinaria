function escapar(texto) {
    if (texto === null || texto === undefined) return "";

    return String(texto)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;");
}

async function extraerMensajeError(respuesta) {
    const texto = await respuesta.text().catch(() => "");

    try {
        const cuerpo = JSON.parse(texto);

        if (cuerpo.mensaje) return cuerpo.mensaje;
        if (cuerpo.message) return cuerpo.message;
        if (cuerpo.errors) return JSON.stringify(cuerpo.errors);

        return texto || `Error ${respuesta.status}`;
    } catch {
        return texto || `El servidor respondió con estado ${respuesta.status}`;
    }
}