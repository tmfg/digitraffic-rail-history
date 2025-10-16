import { fileURLToPath } from 'url';
import { readFile, copyFile } from 'fs/promises';

async function copyBootstrap() {
    const bootstrapPackageJson = await readFile(
        fileURLToPath(import.meta.resolve('bootstrap/package.json')),
        'utf-8'
    );
    const bootstrapStylePath = JSON.parse(bootstrapPackageJson).style;
    const bootstrapStyleFullPath = fileURLToPath(import.meta.resolve(`bootstrap/${bootstrapStylePath}`));

    await copyFile(bootstrapStyleFullPath, 'src/main/resources/static/css/bootstrap.css');
}

await copyBootstrap()