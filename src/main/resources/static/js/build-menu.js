// 로그인 되어있으면 세션유저를 반환받음
const getAuth = async () => {
  try {
    const response = await axios.get("/check-auth");
    return response.data;
  } catch (error) {
    if (error.response && error.response.status === 401) {
      return false;
    }
  }
};

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

const buildMenu = async () => {
  const ul = document.querySelector("#recipetory-menu");

  // search (TODO)
  ul.appendChild(buildNavItem("/view/recipes/tags", "태그 검색"));

  // ranking (TODO)
  ul.appendChild(buildNavItem("#", "순위"));

  const auth = await getAuth();
  if (auth) {
    // bookMark & createRecipe
    ul.appendChild(buildNavItem("/view/" + auth.id + "/bookmarks", "북마크"));
    ul.appendChild(buildNavItem("/view/recipes/new", "레시피 작성"));
    ul.appendChild(buildNavItem("/logout", "로그아웃"));
  } else {
    ul.appendChild(buildNavItem("/view/login", "로그인"));
  }

  return ul;
};

(() => {
  buildMenu();
})();
