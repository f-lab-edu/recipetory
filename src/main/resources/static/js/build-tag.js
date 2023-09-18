let allTags;

const getAllTags = async () => {
  const tags = await axios.get("/tags/all");
  return tags.data;
};

const getDescriptionOfTag = (tagName) => {
  return allTags.find((tag) => {
    return tag.name === tagName;
  }).description;
};

const getTagElement = (tag) => {
  const tagElement = document.createElement("a");
  tagElement.classList.add("fw-bold", "me-1");
  tagElement.textContent = "#" + getDescriptionOfTag(tag);
  tagElement.style.color = "#0c432a";
  tagElement.style.textDecoration = "none";
  tagElement.setAttribute("href", "/view/recipes/tags?t=" + tag);

  return tagElement;
};
