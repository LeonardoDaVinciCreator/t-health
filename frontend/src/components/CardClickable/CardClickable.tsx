import { Card } from "../Card/Card";
import style from "./CardClickable.module.css";

interface CardClickableProps {
  title: string;
  icon: string;
  onClick: () => void;
}

export const CardClickable = ({ title, icon, onClick }: CardClickableProps) => {
  return (
    <Card className={style.card}>
      <div className={style.header} onClick={onClick}>
        <button className="btnIcon">
          <img src={icon} alt="icon" />
        </button>
        <h3 className={style.title}>{title}</h3>
      </div>
    </Card>
  );
};
