(function () {
  function openShare(url) {
    globalThis.open(url, "_blank", "noopener,noreferrer,width=600,height=600");
  }

  function share(platform, pageUrl, text) {
    const u = encodeURIComponent(pageUrl);
    const t = encodeURIComponent(text);
    let shareUrl = "";
    switch (platform) {
      case "facebook":
        shareUrl = "https://www.facebook.com/sharer/sharer.php?u=" + u;
        break;
      case "twitter":
        shareUrl = "https://twitter.com/intent/tweet?url=" + u + "&text=" + t;
        break;
      case "whatsapp":
        shareUrl = "https://api.whatsapp.com/send?text=" + t + "%20" + u;
        break;
    }
    if (shareUrl) openShare(shareUrl);
  }

  document.addEventListener("click", function (e) {
    const btn = e.target.closest("[data-share]");
    if (!btn) return;
    const platform = btn.dataset.share;
    const pageUrl = globalThis.location.href.replace(
      "/machinerydetail",
      "/public/machinerydetail"
    ); 
    const titleElem = document.querySelector(".machinery-detail h1");
    const title =
      (titleElem ? titleElem.textContent.trim() : document.title) +
      " — Arriendo de Maquinaria";
    share(platform, pageUrl, title);
  });
})();
