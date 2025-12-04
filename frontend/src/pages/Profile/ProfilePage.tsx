import { User } from "../../components/User/User";
import { Subscription } from "../../components/Subscription/Subscription";

import avatar from "../../assets/Photo.png";
import avatar0 from "../../assets/Photo-0.png";
import avatar1 from "../../assets/Photo-1.png";
import avatar2 from "../../assets/Photo-2.png";
import avatar3 from "../../assets/Photo-3.png";
import { Contacts } from "../../components/Contacts/Contacts";
import { Target } from "../../components/Target/Target";
import { Notification } from "../../components/Notification/Notification";

import NotificationIconButtonDefault from "../../assets/NotificationIconButton_Default.svg";
import NotificationIconButtonOff from "../../assets/NotificationIconButton_Off.svg";
import NotificationIconButtonOnwithsound from "../../assets/NotificationIconButton_Onwithsound.svg";

export const ProfilePage = () => {
  const userList = [
    {
      name: "Артём Кустов",
      department: "Web разработка",
      role: "Web разработчик",
      avatar: avatar0,
    },
    {
      name: "Святослав Перминов",
      department: "Техническая поддержка",
      role: "специалист",
      avatar: avatar1,
    },
    {
      name: "Константин Константинопольский",
      department: "HR-отдел",
      role: "специалист",
      avatar: avatar2,
    },
    {
      name: "Виктория Коробова",
      department: "Web-разработка",
      role: "дизайнер",
      avatar: avatar3,
    },
  ];

  return (
    <>
      <div className="container">
        <div style={{ marginTop: "32px" }}>
          <User
            avatar={avatar}
            name="Сергей"
            department="HR-отдел"
            role="руководитель отдела"
            inList={true}
          />
        </div>

        <div style={{ marginTop: "12px" }}>
          <div className="flex">
            <Subscription users={userList} />
            <div className="flex-1-1">
              <div className="flex-col">
                <Contacts />
                <Target />
                <Notification
                  states={[
                    {
                      text: "Уведомления включены",
                      icon: NotificationIconButtonOnwithsound,
                    },
                    {
                      text: "Уведомления включены, без звука",
                      icon: NotificationIconButtonDefault,
                    },
                    {
                      text: "Уведомления выключены",
                      icon: NotificationIconButtonOff,
                    },
                  ]}
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
