// recipe card for recipe list
const createRecipeCard = (recipe) => {
  const card = document.createElement("div");
  card.classList.add("card", "col-10", "col-lg-7", "mb-3");

  const cardBody = document.createElement("div");
  cardBody.classList.add("card-body");

  const title = document.createElement("h5");
  const titleLink = document.createElement("a");
  titleLink.textContent = recipe.title;
  titleLink.setAttribute("href", "/view/recipes/" + recipe.id);
  titleLink.style.color = "#0c432a";
  title.classList.add("card-title");
  title.appendChild(titleLink);

  const description = document.createElement("p");
  description.classList.add("card-text");
  description.textContent = recipe.information.description;

  const details = document.createElement("ul");
  details.classList.add("list-group", "list-group-flush");
  const ratings = document.createElement("li");
  ratings.classList.add("list-group-item");
  ratings.innerHTML = `<strong>평점 : </strong> ${
    recipe.statistics.ratings / 10.0
  }`;

  const tags = document.createElement("li");
  tags.classList.add("list-group-item");
  recipe.tags.forEach((tag) => {
    tags.appendChild(getTagElement(tag));
  });

  details.appendChild(ratings);
  details.appendChild(tags);

  cardBody.appendChild(title);
  cardBody.appendChild(description);
  cardBody.appendChild(details);

  card.appendChild(cardBody);

  return card;
};
