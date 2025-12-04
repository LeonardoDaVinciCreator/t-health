import { Card } from "../Card/Card";
import style from "./AddForm.module.css";

import { Input } from "../Input/Input";

export const AddForm = () => {
  return (
    <Card>
      <div className={style.header}>
        <div className={style.headerTitle}>Добавление тренировки</div>
      </div>
      <form action="" className={style.form}>
        <Input placeholder="Жимовая" type="text" text="Название тренировки:" />
        <Input placeholder="Анаэробная" type="text" text="Тип нагрузки" />
        <Input placeholder="ч:мм" type="time" text="Продолжительность :" />
        <Input placeholder="300" type="text" text="Потрачено каллорий:" />
        <Input placeholder="дд.мм.гггг" type="date" text="Дата:" />

        {/* Сделать общие стили */}
        <button className={style.btn}>Сохранить</button>
      </form>
    </Card>
  );
};
