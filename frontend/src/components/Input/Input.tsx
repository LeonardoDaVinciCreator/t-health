import style from "./Input.module.css";
import eye from "../../assets/Eye.svg";
import eyeNo from "../../assets/EyeNo.svg";

import edit from "../../assets/Edit.svg";
import { useState } from "react";
import clsx from "clsx";

interface InputProps {
  type: string;
  placeholder: string;
  text: string;
  nodes?: string;
  isVisible?: boolean;
  hasControl?: boolean;
}

export const Input = ({
  type,
  placeholder,
  text,
  nodes,
  hasControl,
}: InputProps) => {
  const [visible, setVisible] = useState(true);
  const [value, setValue] = useState("");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = e.target.value;
    setValue(newValue);

    if (newValue.length === 0) {
      setVisible(true);
    }
  };

  const shouldBlur = !visible && value.length > 0;

  return (
    <div className={style.inputBlock}>
      <label htmlFor={type} className={style.label}>
        {text}:
      </label>
      <div className={style.inputElement}>
        <input
          type={type}
          id={type}
          placeholder={placeholder}
          value={value}
          onChange={handleChange}
          className={clsx(style.input, shouldBlur && style.blur)}
        />

        {hasControl && (
          <>
            <button className="btnIcon" onClick={() => setVisible(!visible)}>
              <img src={visible ? eye : eyeNo} alt="" />
            </button>
            <button className="btnIcon">
              <img src={edit} alt="" />
            </button>
          </>
        )}
      </div>
      {nodes && <div className={style.nodes}>{nodes}</div>}
    </div>
  );
};
