import { fileURLToPath } from 'url';
import { readFile, copyFile } from 'fs/promises';

async function copyFintrafficCSS() {
    const fintrafficCSS = fileURLToPath(import.meta.resolve('@fintraffic/fds-coreui-css/dist/coreui.min.css'));

    await copyFile(fintrafficCSS, 'src/main/resources/static/css/fintraffic.min.css');
}

await copyFintrafficCSS()