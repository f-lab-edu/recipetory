const buildCommentCard = (comment) => {
  const card = document.createElement("div");
  card.classList.add("card", "col-md-8", "mb-3");

  const cardBody = document.createElement("div");
  cardBody.classList.add("card-body");

  const title = document.createElement("h5");
  const titleLink = document.createElement("a");
  titleLink.textContent = comment.author.name;
  titleLink.setAttribute("href", "/view/users/" + comment.author.id);
  titleLink.style.color = "black";
  title.classList.add("card-title");
  title.appendChild(titleLink);

  const content = document.createElement("p");
  content.classList.add("card-text");
  content.textContent = comment.content;

  cardBody.appendChild(title);
  cardBody.appendChild(content);
  card.appendChild(cardBody);

  return card;
};

const buildReviewCard = (review) => {
  const card = document.createElement("div");
  card.classList.add("card", "col-md-8", "mb-3");

  const cardBody = document.createElement("div");
  cardBody.classList.add("card-body");

  const title = document.createElement("h5");
  const titleLink = document.createElement("a");
  titleLink.textContent = review.author.name;
  titleLink.setAttribute("href", "/view/users/" + review.author.id);
  titleLink.style.color = "black";
  title.classList.add("card-title");
  title.appendChild(titleLink);

  const content = document.createElement("p");
  content.classList.add("card-text");
  content.textContent = review.content;

  const rating = document.createElement("p");
  rating.classList.add("card-text");
  rating.innerHTML = `<strong>평점 : </strong> ${review.rating}`;
  console.log(rating.innerHTML);

  cardBody.appendChild(title);
  cardBody.appendChild(content);
  cardBody.appendChild(rating);

  card.appendChild(cardBody);

  return card;
};

const buildCommentsOfRecipe = (recipeId) => {
  const replySection = document.querySelector("#reply-section");
  while (replySection.firstChild) {
    replySection.removeChild(replySection.firstChild);
  }

  const commentSection = document.createElement("div");
  commentSection.classList.add("row", "justify-content-center", "mt-4");
  commentSection.id = "comment-section";

  axios
    .get("/recipe/" + recipeId + "/comments")
    .then((response) => {
      const comments = response.data.comments;

      if (comments.length) {
        comments.forEach((comment) => {
          commentSection.appendChild(buildCommentCard(comment));
        });
      } else {
        commentSection.innerHTML = `
          <p class="text-body-secondary text-center">
            댓글이 존재하지 않습니다.
          </p>
          `;
      }
      replySection.appendChild(commentSection);
    })
    .catch((error) => {
      console.log("Error fetching comment " + error);
    });
};

const buildReviewsOfRecipe = (recipeId) => {
  const replySection = document.querySelector("#reply-section");
  while (replySection.firstChild) {
    replySection.removeChild(replySection.firstChild);
  }

  const reviewSection = document.createElement("div");
  reviewSection.classList.add("row", "justify-content-center", "mt-4");
  reviewSection.id = "review-section";

  axios
    .get("/recipe/" + recipeId + "/reviews")
    .then((response) => {
      const reviews = response.data.reviews;

      if (reviews.length) {
        reviews.forEach((review) => {
          reviewSection.appendChild(buildReviewCard(review));
        });
      } else {
        reviewSection.innerHTML = `
          <p class="text-body-secondary text-center">
            리뷰가 존재하지 않습니다.
          </p>
          `;
      }

      replySection.appendChild(reviewSection);
    })
    .catch((error) => {
      console.log("Error fetching review " + error);
    });
};
