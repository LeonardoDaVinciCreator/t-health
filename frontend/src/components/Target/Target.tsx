import { CardWithTitle } from "../CardWithTitle/CardWithTitle";
import { CardNested } from "../CardNested/CardNested";
import { InputNumeric } from "../InputNumeric/InputNumeric";
import style from "./Target.module.css";

export const Target = () => {
  return (
    <CardWithTitle title="Цели">
      <div className={style.row}>
        <CardNested title="Шаги">
          <InputNumeric />
        </CardNested>
        <CardNested title="Активность">
          <InputNumeric />
        </CardNested>
        <CardNested title="Расход калорий">
          <InputNumeric />
        </CardNested>
      </div>
    </CardWithTitle>
  );
};
