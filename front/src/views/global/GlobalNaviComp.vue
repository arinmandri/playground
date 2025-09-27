<template>
  <nav id="globalNavi">
    <ul id="globalMenu">
      <li><router-link to="/">홈</router-link></li>
      <li><router-link to="/board">게시판</router-link></li>
    </ul>
  </nav>
</template>

<style>
#globalMenu {
  background-color: #fff;
  padding: 10px;
  top: 0;
  font-family: "Do Hyeon", sans-serif;
  font-weight: 400;
  font-style: normal;
  font-size: 160%;
}

#globalMenu a {
  color: unset;
}

#globalMenu {
  list-style: none;
  display: flex;
  gap: 10px;
}
</style>
