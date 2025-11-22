function toggleSearch(tipo) {
  document.getElementById("nameInput").disabled = tipo !== "nombre";
  document.getElementById("categoryInput").disabled = tipo !== "categoria";
}
(function () {
  "use strict";
  const forms = document.querySelectorAll(".needs-validation");
  for (const form of forms) {
    form.addEventListener(
      "submit",
      function (event) {
        if (!form.checkValidity()) {
          event.preventDefault();
          event.stopPropagation();
        }
        form.classList.add("was-validated");
      },
      false
    );
  }
})();
