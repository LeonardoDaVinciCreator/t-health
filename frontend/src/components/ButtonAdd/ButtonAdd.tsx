import style from "./ButtonAdd.module.css";

interface ButtonAddProps {
  text: string;
  number: string;
  icon: string;
  onClick: () => void;
}

export const ButtonAdd = ({ text, number, icon, onClick }: ButtonAddProps) => {
  return (
    <div className={style.btn}>
      <div>{text}</div>

      <div className={style.control}>
        <div className={style.textSecondary}>{number}</div>
        <button className="btnIcon" onClick={onClick}>
          <img src={icon} alt="icon" />
        </button>
      </div>
    </div>
  );
};
