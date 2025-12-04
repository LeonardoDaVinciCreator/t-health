import style from "./InputNumeric.module.css";

import plus from "../../assets/Plus.svg";
import minus from "../../assets/Minus.svg";
import { useState } from "react";

export const InputNumeric = () => {
  const [num, setNum] = useState(0);

  const handleIncrement = () => {
    setNum((prev) => prev + 1);
  };

  const handleDecrement = () => {
    setNum((prev) => Math.max(0, prev - 1));
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = parseInt(e.target.value) || 0;
    setNum(Math.max(0, value));
  };

  return (
    <div className={style.inputBlock}>
      <button className="btnIcon" onClick={handleDecrement}>
        <img src={minus} alt="minus" />
      </button>
      <input
        type="number"
        className={style.input}
        value={num}
        onChange={handleChange}
        min="0"
      />
      <button className="btnIcon" onClick={handleIncrement}>
        <img src={plus} alt="plus" />
      </button>
    </div>
  );
};
