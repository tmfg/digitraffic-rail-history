module.exports = {
  plugins: ["./node_modules/prettier-plugin-packagejson"],

  printWidth: 110,
  endOfLine: "auto",
  singleQuote: false,

  // For ES5, trailing commas cannot be used in function parameters
  trailingComma: "none",
  overrides: [
    {
      files: ["*.json", "*.yml", "*.yaml"],
      options: {
        tabWidth: 2
      }
    }
  ]
};
