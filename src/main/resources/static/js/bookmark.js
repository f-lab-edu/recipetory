// BookMark와 관련된 기능들 (add, delete)
const addBookMark = () => {
  const path = window.location.pathname.split("/");
  const recipeId = path[path.length - 1];

  axios
    .post("/bookmarks/" + recipeId)
    .then((response) => {
      const bookMarked = response.data;
      alert("북마크 되었습니다!");
      window.location.href = "/view/recipes/" + bookMarked.recipeId;
    })
    .catch((error) => {
      if (error.response.status === 400) {
        alert("자신이 작성한 레시피는 북마크할 수 없습니다.");
      } else if (error.response.status === 401) {
        alert("로그인이 필요한 서비스입니다.");
        window.location.href = "/view/login";
      } else {
        console.log("failed to add bookmark:", error);
      }
    });
};

const deleteBookMark = () => {
  const path = window.location.pathname.split("/");
  const recipeId = path[path.length - 1];
  axios
    .delete("/bookmarks/" + recipeId)
    .then((response) => {
      alert("북마크 삭제되었습니다.");
      window.location.href = "/";
    })
    .catch((error) => {
      console.log("failed to delete bookmark:", error);
    });
};

const getBookMarksOfUser = async (userId) => {
  return await axios
    .get("/" + userId + "/bookmarks")
    .then((response) => {
      return response.data.recipes;
    })
    .catch((error) => {
      console.log("failed to get bookmark:", error);
    });
};
