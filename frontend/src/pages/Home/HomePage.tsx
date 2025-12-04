import { User } from "../../components/User/User";
import avatar from "../../assets/Photo.png";
import { CardWithTitle } from "../../components/CardWithTitle/CardWithTitle";
import { ButtonAdd } from "../../components/ButtonAdd/ButtonAdd";
import plus from "../../assets/Plus.svg";
import { useNavigate } from "react-router-dom";

export const HomePage = () => {
  const navigate = useNavigate();

  return (
    <>
      <div className="container">
        <div style={{ marginTop: "32px" }}>
          <User
            avatar={avatar}
            name="Сергей"
            department="HR-отдел"
            role="руководитель отдела"
            inList={false}
          />
        </div>

        <div style={{ display: "flex", flexDirection: "column", gap: "8px" }}>
          <div style={{ display: "flex", justifyContent: "space-between" }}>
            <CardWithTitle title="Активность сегодня: ">
              Кольца активности
            </CardWithTitle>
            <CardWithTitle title="Совет дня">
              Начните тренировку с лёгкой разминки, а по окончании не забудьте о
              заминке. Это позволит снизить риск трвмы
            </CardWithTitle>
            <CardWithTitle title="Текст">Список уведомлений</CardWithTitle>
          </div>
          <div style={{ display: "flex", justifyContent: "space-between" }}>
            <CardWithTitle title="Шаги">График</CardWithTitle>
            <CardWithTitle title="Активность">График</CardWithTitle>
            <CardWithTitle title="Каллорий потрачено">График</CardWithTitle>
          </div>
          <div
            style={{
              display: "flex",
              justifyContent: "space-between",
              gap: "8px",
            }}
          >
            <ButtonAdd
              text="Шаги"
              number="1222"
              icon={plus}
              onClick={() => navigate("/training")}
            />
            <ButtonAdd
              text="Шаги"
              number="12"
              icon={plus}
              onClick={() => navigate("/eating")}
            />
            <ButtonAdd
              text="Обновить данные"
              number="124"
              icon={plus}
              onClick={() => navigate("/")}
            />
          </div>
        </div>
      </div>
    </>
  );
};
