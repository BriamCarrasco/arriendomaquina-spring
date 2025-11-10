// Función para alternar entre búsqueda por nombre o categoría
function toggleSearch(tipo) {
    document.getElementById('nameInput').disabled = tipo !== 'nombre';
    document.getElementById('categoryInput').disabled = tipo !== 'categoria';
}

// Validación de formularios Bootstrap
(function () {
    'use strict'
    var forms = document.querySelectorAll('.needs-validation')
    Array.prototype.slice.call(forms).forEach(function (form) {
        form.addEventListener('submit', function (event) {
            if (!form.checkValidity()) {
                event.preventDefault()
                event.stopPropagation()
            }
            form.classList.add('was-validated')
        }, false)
    })
})()
