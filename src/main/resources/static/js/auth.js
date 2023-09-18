/**
 * 로그인한 유저의 세션 정보를 반환한다.
 * 로그인하지 않았을 경우 false를 반환한다.
 * @returns
 */
const getAuth = async () => {
  try {
    const response = await axios.get("/check-auth");
    return response.data;
  } catch (error) {
    return;
  }
};
