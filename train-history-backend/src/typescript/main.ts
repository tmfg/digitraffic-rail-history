import 'htmx.org'
import 'htmx-ext-path-params'
import { createIcons, Loader } from 'lucide';

window.addEventListener("DOMContentLoaded", function() {
  createIcons({
    icons: {
      Loader
    }
  });
}, false)