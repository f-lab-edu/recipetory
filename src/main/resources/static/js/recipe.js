const deleteRecipe = () => {
  if (confirm("정말 삭제하시겠습니까?") != true) {
    return;
  }

  const path = window.location.pathname.split("/");
  const recipeId = path[path.length - 1];

  axios
    .delete("/recipes/" + recipeId)
    .then((response) => {
      alert("레시피가 삭제되었습니다.");
      window.history.back();
    })
    .catch((error) => {
      console.log("error deleting recipe id " + recipeId, error);
    });
};

const getRecipeOfUser = (userId) => {
  axios
    .get("/" + userId + "/recipes")
    .then(function (response) {
      const wrote = document.createElement("h3");
      wrote.textContent = "작성 레시피";
      document.querySelector("#content-title").appendChild(wrote);

      const recipes = response.data.recipes;
      const container = document.querySelector("#recipe-container");

      recipes.forEach((recipe) => {
        const recipeCard = createRecipeCard(recipe);
        container.appendChild(recipeCard);
      });
    })
    .catch(function (error) {
      console.log("error fetching data:", error);
    });
};
