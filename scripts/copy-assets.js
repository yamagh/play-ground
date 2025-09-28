const fs = require('fs-extra');
const path = require('path');

const projectRoot = path.resolve(__dirname, '..');
const nodeModulesDir = path.join(projectRoot, 'node_modules');
const publicDir = path.join(projectRoot, 'public', 'vendors');

async function copyAssets() {
  try {
    console.log('Copying vendor assets...');

    // Ensure the destination directories exist
    await fs.ensureDir(path.join(publicDir, 'bootstrap', 'css'));
    await fs.ensureDir(path.join(publicDir, 'bootstrap', 'js'));
    await fs.ensureDir(path.join(publicDir, 'bootstrap-icons', 'fonts'));

    // Copy Bootstrap CSS
    await fs.copy(
      path.join(nodeModulesDir, 'bootstrap', 'dist', 'css', 'bootstrap.min.css'),
      path.join(publicDir, 'bootstrap', 'css', 'bootstrap.min.css')
    );

    // Copy Bootstrap JS
    await fs.copy(
      path.join(nodeModulesDir, 'bootstrap', 'dist', 'js', 'bootstrap.bundle.min.js'),
      path.join(publicDir, 'bootstrap', 'js', 'bootstrap.bundle.min.js')
    );

    // Copy Bootstrap Icons CSS
    await fs.copy(
      path.join(nodeModulesDir, 'bootstrap-icons', 'font', 'bootstrap-icons.css'),
      path.join(publicDir, 'bootstrap-icons', 'bootstrap-icons.css')
    );

    // Copy Bootstrap Icons fonts
    await fs.copy(
      path.join(nodeModulesDir, 'bootstrap-icons', 'font', 'fonts'),
      path.join(publicDir, 'bootstrap-icons', 'fonts')
    );

    console.log('Successfully copied vendor assets.');
  } catch (err) {
    console.error('Error copying vendor assets:', err);
    process.exit(1);
  }
}

copyAssets();
