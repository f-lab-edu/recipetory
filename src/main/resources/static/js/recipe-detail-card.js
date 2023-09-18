const createStepElement = (step) => {
  const li = document.createElement("li");
  li.classList.add("list-group-item", "d-flex");

  const content = document.createElement("div");
  content.classList.add("ms-2", "me-auto");
  content.textContent = step.description;

  li.appendChild(content);
  return li;
};
const createIngredientElement = (ingredient) => {
  const li = document.createElement("li");
  li.classList.add(
    "list-group-item",
    "d-flex",
    "justify-content-between",
    "align-items-start"
  );

  const name = document.createElement("div");
  name.classList.add("ms-1", "me-auto");
  name.textContent = ingredient.name;

  const amount = document.createElement("div");
  amount.classList.add("me-1");
  amount.textContent = ingredient.amount;

  li.append(name, amount);
  return li;
};

const buildRecipeDetailCard = (auth) => {
  const path = window.location.pathname.split("/");
  const recipeId = path[path.length - 1];
  axios
    .get("/recipes/" + recipeId)
    .then(function (response) {
      const recipe = response.data;
      const recipeInfo = recipe.information;
      const recipeStatistics = recipe.statistics;

      // title
      document.querySelector("#recipe-title").textContent = recipe.title;

      // recipeInfo
      document.querySelector("#recipe-description").textContent =
        recipeInfo.description;
      document.querySelector("#cooking-time").textContent =
        recipeInfo.cookingTime;
      document.querySelector("#difficulty").textContent = recipeInfo.difficulty;
      document.querySelector("#serving").textContent = recipeInfo.serving;

      // recipeStatistics
      document.querySelector("#view-count").textContent =
        recipeStatistics.viewCount;
      document.querySelector("#rating").textContent =
        recipeStatistics.ratings / 10.0;

      // tags
      const tagSection = document.querySelector("#tags");
      recipe.tags.forEach((tag) => {
        tagSection.appendChild(getTagElement(tag));
      });

      // ingredients
      const ingredientSection = document.querySelector("#ingredients");
      recipe.ingredients.forEach((ingredient) => {
        ingredientSection.appendChild(createIngredientElement(ingredient));
      });

      // steps
      const stepSection = document.querySelector("#steps");
      recipe.steps.forEach((step) => {
        stepSection.appendChild(createStepElement(step));
      });

      const ul = document.querySelector("#additional-menu");

      // author-specific
      const isAuthor = auth && recipe.author.id === auth.id;
      if (isAuthor) {
        const del = document.createElement("li");
        const delBtn = document.createElement("button");
        delBtn.setAttribute("type", "submit");
        delBtn.setAttribute("onclick", "deleteRecipe()");
        delBtn.classList.add("dropdown-item");
        delBtn.textContent = "레시피 삭제";
        del.appendChild(delBtn);

        // TODO
        const edit = document.createElement("li");
        const editBtn = document.createElement("a");
        editBtn.setAttribute("type", "submit");
        editBtn.setAttribute("href", "#");
        editBtn.classList.add("dropdown-item", "button");
        editBtn.textContent = "레시피 수정";
        edit.appendChild(editBtn);

        ul.appendChild(del);
        ul.appendChild(edit);
      }

      const authorContent = document.querySelector("#author");
      authorContent.innerHTML = `
          by <a href="/view/users/${recipe.author.id}" style="color:#0c432a;">
            ${recipe.author.name}
          </a>
          `;
    })
    .catch(function (error) {
      if (error.respone && error.response.status === 404) {
        alert("존재하지 않는 레시피입니다!");
        const recipeCard = document.querySelector("#recipe-card");
        recipeCard.remove();
      } else {
        console.log("error fetching data : ", error);
      }
    });
};
