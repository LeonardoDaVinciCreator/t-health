import style from "./Header.module.css";
import logo from "../../../public/Logo.svg";
import bell from "../../assets/Bell.svg";
import filter from "../../assets/Filter.svg";
import { Search } from "../Search/Search";
import clsx from "clsx";
export default function Header() {
  return (
    <div className="header__container">
      <header className={clsx(style.header, "container")}>
        <div className={style.logo}>
          <a href="#">
            <img src={logo} alt="logo" />
          </a>
        </div>
        <div className={style.input}>
          <Search />
        </div>
        <div className={style.controls}>
          <button className="btnIcon btnIconL">
            <img src={bell} alt="" />
          </button>
          <button className="btnIcon btnIconL">
            <img src={filter} alt="" />
          </button>
        </div>
      </header>
    </div>
  );
}
