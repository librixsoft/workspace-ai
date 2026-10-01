/**
 * Script de automatización para generar el PDF del Dossier LibrixSoft en 1 sola página sin cortes.
 * 
 * Este script utiliza el motor headless de Google Chrome para compilar `librixsoft_brochure_v2 2.html`
 * a un archivo PDF vectorial nativo (`brochure_single_page.pdf`).
 * 
 * Uso:
 *   node generate_pdf.js
 */

import { execSync } from 'child_process';
import { existsSync, statSync } from 'fs';
import { fileURLToPath } from 'url';
import { dirname, join } from 'path';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

const htmlPath = join(__dirname, 'librixsoft_brochure_v2 2.html');
const pdfPath = join(__dirname, 'brochure_single_page.pdf');
const chromePath = '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome';

if (!existsSync(chromePath)) {
  console.error('❌ Error: Google Chrome no se encuentra instalado en:', chromePath);
  process.exit(1);
}

console.log('🚀 Compilando HTML a PDF vectorial de 1 sola página...');

try {
  const cmd = `"${chromePath}" --headless --disable-gpu --no-pdf-header-footer --print-to-pdf="${pdfPath}" "file://${htmlPath}"`;
  execSync(cmd);
  
  if (existsSync(pdfPath)) {
    const bytes = statSync(pdfPath).size;
    console.log(`✅ ¡PDF generado con éxito! (${(bytes / 1024).toFixed(1)} KB)`);
    console.log(`📍 Ubicación: ${pdfPath}`);
  } else {
    throw new Error('El archivo PDF no fue creado.');
  }
} catch (error) {
  console.error('❌ Error al generar el PDF:', error.message);
  process.exit(1);
}
