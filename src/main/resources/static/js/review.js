document.addEventListener("DOMContentLoaded", function () {
  // JavaScript para enviar la reseña vía AJAX
  const reviewForm = document.getElementById("reviewForm");
  if (reviewForm) {
    reviewForm.addEventListener("submit", function (e) {
      e.preventDefault();
      const machineryId = reviewForm.dataset.machineryId;
      const rating = document.getElementById("rating").value;
      const comment = document.getElementById("comment").value;

      // Obtener token CSRF si existe (Spring Security)
      // Buscamos en el formulario de logout que suele tener el token
      const csrfTokenInput = document.querySelector('input[name="_csrf"]');
      const headers = {
        "Content-Type": "application/json",
      };

      if (csrfTokenInput) {
        headers["X-CSRF-TOKEN"] = csrfTokenInput.value;
      }
      const csrfTokenMeta = document.querySelector('meta[name="_csrf"]');
      const csrfHeaderMeta = document.querySelector(
        'meta[name="_csrf_header"]'
      );

      if (csrfTokenMeta && csrfHeaderMeta) {
        headers[csrfHeaderMeta.content] = csrfTokenMeta.content;
      }

      fetch(`/api/reviews/machinery/${machineryId}`, {
        method: "POST",
        headers: headers,
        body: JSON.stringify({
          rating: Number.parseInt(rating),
          comment: comment,
        }),
      })
        .then((response) => {
          if (response.ok) {
            alert("Reseña agregada exitosamente");
            location.reload(); // Recargar para mostrar la nueva reseña
          } else {
            alert("Error al agregar reseña. Asegúrate de estar logueado.");
          }
        })
        .catch((error) => console.error("Error:", error));
    });
  }
});
