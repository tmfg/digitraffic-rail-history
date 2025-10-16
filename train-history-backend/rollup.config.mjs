import typescript from '@rollup/plugin-typescript';
import resolve from '@rollup/plugin-node-resolve';

export default [{
  input: 'src/typescript/main.ts',
  output: {
    file: 'src/main/resources/static/js/main.js',
    format: 'iife',
    sourcemap: true,
    name: 'TrainHistory'
  },
  plugins: [
    resolve({
      browser: true
    }),
    //commonjs(),
    typescript({
      tsconfig: './tsconfig.json',
      sourceMap: true,
      inlineSources: true
    })
  ],
  onwarn: (entry, next) => {
    // Disable Rollup's eval warning from htmx
    // Here is some background info on HTMX's use of eval https://github.com/bigskysoftware/htmx/pull/1988#issuecomment-1806290317
    // We have disabled the HTMX usage of eval
    const isWarningOnHTMX = entry.loc?.file && entry.loc.file.includes('htmx.esm.js');
    if(isWarningOnHTMX && entry.message.includes('Use of eval in'))
      return;
    return next(entry);
  },
}];
