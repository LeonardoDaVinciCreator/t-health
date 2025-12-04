import style from "./Navigation.module.css";
import IconLike from "../../assets/IconLike.svg";
import iconLenta from "../../assets/iconLenta.svg";
import iconChat from "../../assets/iconChat.svg";
import iconUser from "../../assets/iconUser.svg";
import { useNavigate } from "react-router-dom";

export const MyNavigation = () => {
  const navigate = useNavigate();

  return (
    <div className={style.wrapper}>
      <ul className={style.list}>
        <li className={style.list__item}>
          <a className={style.list__link} onClick={() => navigate("/")}>
            <img src={IconLike} alt="Главная" />
            <div>Главная</div>
          </a>
        </li>
        <li className={style.list__item}>
          <a href="#" className={style.list__link}>
            <img src={iconLenta} alt="Лента" />
            <div>Лента</div>
          </a>
        </li>
        <li className={style.list__item}>
          <a href="#" className={style.list__link}>
            <img src={iconChat} alt="Чат" />
            <div>Чат</div>
          </a>
        </li>
        <li className={style.list__item}>
          <a className={style.list__link} onClick={() => navigate("/profile")}>
            <img src={iconUser} alt="Профиль" />
            <div>Профиль</div>
          </a>
        </li>
      </ul>
    </div>
  );
};
