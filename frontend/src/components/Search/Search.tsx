import style from "./Search.module.css";
import search from "../../assets/Search.svg";

export const Search = () => {
  return (
    <div className={style.search}>
      <input
        type="search"
        className={style.input}
        placeholder="Теги, авторы..."
      />

      <button className="btnIcon">
        <img src={search} alt="" />
      </button>
    </div>
  );
};
