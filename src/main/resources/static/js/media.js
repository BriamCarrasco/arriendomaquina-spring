document.addEventListener("DOMContentLoaded", () => {
  const imgForm = document.getElementById("mediaImageForm");
  const vidForm = document.getElementById("mediaVideoForm");
  const uploadForm = document.getElementById("mediaImageUploadForm");
  const fileInput = document.getElementById("imageFile");
  const mediaList = document.querySelector(".machinery-media-list");

  function getCsrf() {
    const tokenMeta = document.querySelector('meta[name="_csrf"]');
    const headerMeta = document.querySelector('meta[name="_csrf_header"]');
    return tokenMeta && headerMeta
      ? { header: headerMeta.content, token: tokenMeta.content }
      : null;
  }

  async function postMedia(urlEndpoint, urlValue, type) {
    const csrf = getCsrf();
    const headers = { "Content-Type": "application/json" };
    if (csrf) headers[csrf.header] = csrf.token;
    const resp = await fetch(urlEndpoint, {
      method: "POST",
      headers,
      credentials: "include",
      body: JSON.stringify({ url: urlValue }),
    });
    if (!resp.ok) {
      alert("Error al agregar " + type);
      return null;
    }
    return resp.json();
  }

  function appendMedia(media) {
    if (!mediaList) return;
    const wrapper = document.createElement("div");
    if (media.imgUrl) {
      const img = document.createElement("img");
      img.src = media.imgUrl;
      img.alt = "Maquinaria";
      img.style.maxWidth = "300px";
      wrapper.appendChild(img);
    }
    if (media.vidUrl) {
      const iframe = document.createElement("iframe");
      iframe.src = media.vidUrl;
      iframe.width = "400";
      iframe.height = "300";
      iframe.setAttribute("allowfullscreen", "");
      iframe.setAttribute("title", "Video de maquinaria");
      wrapper.appendChild(iframe);
    }
    mediaList.prepend(wrapper);
  }

  if (imgForm) {
    imgForm.addEventListener("submit", async (e) => {
      e.preventDefault();
      const machineryId = imgForm.dataset.machineryId;
      const input = document.getElementById("imageUrl");
      const value = input.value.trim();
      if (!value) {
        alert("Ingrese URL de imagen");
        return;
      }
      const endpoint = `/api/machinery-media/machinery/${machineryId}/image`;
      const created = await postMedia(endpoint, value, "imagen");
      if (created) {
        appendMedia(created);
        input.value = "";
        alert("Imagen agregada");
        location.reload();
      }
    });
  }

  if (uploadForm) {
    uploadForm.addEventListener("submit", async (e) => {
      e.preventDefault();
      const machineryId = uploadForm.dataset.machineryId;
      if (!fileInput?.files?.length) {
        alert("Seleccione un archivo de imagen");
        return;
      }
      const csrf = getCsrf();
      const fd = new FormData();
      fd.append("file", fileInput.files[0]);
      const headers = {};
      if (csrf) headers[csrf.header] = csrf.token;
      try {
        const resp = await fetch(
          `/api/machinery-media/machinery/${machineryId}/image-upload`,
          {
            method: "POST",
            credentials: "include",
            headers,
            body: fd,
          }
        );
        if (!resp.ok) {
          let err = null;
          try {
            err = await resp.json();
          } catch (e) {
            console.error("Error parsing error response:", e);
            err = { message: "Error desconocido del servidor" };
          }
          alert(err?.message || "Error al subir imagen");
          return;
        }
        const created = await resp.json();
        appendMedia(created);
        fileInput.value = "";
        alert("Imagen subida");
        location.reload();
      } catch (err) {
        console.error(err);
        alert("Fallo de red al subir imagen");
      }
    });
  }

  if (vidForm) {
    function normalizeVideoUrl(raw) {
      if (!raw) return raw;
      try {
        const u = new URL(raw);
        const host = u.hostname.replace("www.", "");
        if (host === "youtube.com" || host === "youtube-nocookie.com") {
          const v = u.searchParams.get("v");
          if (v) return `https://www.youtube.com/embed/${v}`;
          if (u.pathname.startsWith("/embed/")) return raw;
        }
        if (host === "youtu.be") {
          // short URL youtu.be/VIDEOID
          const id = u.pathname.replace(/^\//, "");
          if (id) return `https://www.youtube.com/embed/${id}`;
        }
        if (host === "vimeo.com" || host === "www.vimeo.com") {
          const id = u.pathname.replace(/^\//, "");
          if (id) return `https://player.vimeo.com/video/${id}`;
        }
      } catch (err) {
        console.debug("normalizeVideoUrl: not a URL", raw, err?.message || err);
      }
      return raw;
    }

    vidForm.addEventListener("submit", async (e) => {
      e.preventDefault();
      const machineryId = vidForm.dataset.machineryId;
      const input = document.getElementById("videoUrl");
      let value = input.value.trim();
      value = normalizeVideoUrl(value);
      if (!value) {
        alert("Ingrese URL de video");
        return;
      }
      const endpoint = `/api/machinery-media/machinery/${machineryId}/video`;
      const created = await postMedia(endpoint, value, "video");
      if (created) {
        appendMedia(created);
        input.value = "";
        alert("Video agregado");
      }
    });
  }

  const modal = document.getElementById("image-modal");
  const modalImg = document.getElementById("modal-img");
  const closeModal = document.getElementById("close-modal");

  const images = document.querySelectorAll(".machinery-media-list img");
  for (const img of images) {
    img.addEventListener("click", () => {
      modalImg.src = img.src;
      modal.style.display = "flex";
    });
  }

  closeModal.addEventListener("click", () => {
    modal.style.display = "none";
    modalImg.src = "";
  });

  modal.addEventListener("click", (e) => {
    if (e.target === modal) {
      modal.style.display = "none";
      modalImg.src = "";
    }
  });
});
