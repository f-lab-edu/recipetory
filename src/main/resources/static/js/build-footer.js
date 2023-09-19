const buildFooter = () => {
  const footer = document.querySelector("#footer");
  footer.innerHTML = `
      <p>&copy; 2023 Recipetory</p>
      <p>
        <a href="https://github.com/f-lab-edu/recipetory" style="color: #0c432a"
          >BuchuKim</a
        >
      </p>
    `;
};

(() => {
  buildFooter();
})();
