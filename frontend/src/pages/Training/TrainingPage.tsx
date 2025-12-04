import { AddForm } from "../../components/AddForm/AddForm";
import style from "./TrainingPage.module.css";

export const TrainingPage = () => {
  return (
    <>
      <div className="container">
        <div className={style.flex}>
          <div>Компонент добавления тренировки</div>
          <div className={style.flexCol}>
            <div className={style.flex}>
              <div>График</div>
              <div>календарь</div>
            </div>
            <AddForm />
          </div>
        </div>
      </div>
    </>
  );
};
