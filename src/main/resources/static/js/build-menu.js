const buildNavItem = (href, textContent) => {
  const li = document.createElement("li");
  li.classList.add("nav-item");

  const a = document.createElement("a");
  a.classList.add("nav-link");
  a.style.color = "#0c432a";
  a.href = href;
  a.textContent = textContent;

  li.appendChild(a);
  return li;
};

const buildMenu = (auth) => {
  const ul = document.querySelector("#recipetory-menu");

  ul.appendChild(buildNavItem("/view/recipes/tags", "태그 검색"));

  // ranking (TODO)
  ul.appendChild(buildNavItem("#", "순위"));

  // const auth = await getAuth();
  if (auth) {
    // bookMark & createRecipe
    ul.appendChild(buildNavItem("/view/users/" + auth.id, "내 정보"));
    ul.appendChild(buildNavItem("/view/recipes/new", "레시피 작성"));
    ul.appendChild(buildNavItem("/logout", "로그아웃"));
  } else {
    ul.appendChild(buildNavItem("/view/login", "로그인"));
  }

  return ul;
};
