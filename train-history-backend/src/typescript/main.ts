import "htmx-ext-path-params";
import "htmx.org";
import { createIcons, Loader } from "lucide";

document.body.addEventListener(
  "htmx:configRequest",
  function (event: CustomEvent) {
    event.detail.headers["Digitraffic-User"] = "digitraffic-rail-history-ui";
  },
);

window.addEventListener(
  "DOMContentLoaded",
  function () {
    createIcons({
      icons: {
        Loader,
      },
    });
  },
  false,
);
