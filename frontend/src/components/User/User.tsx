import clsx from "clsx";
import style from "./User.module.css";

interface UserProps {
  avatar: string;
  name: string;
  department: string;
  role: string;
  inList?: boolean;
}

export const User = ({
  avatar,
  name,
  department,
  role,
  inList = false,
}: UserProps) => {
  return (
    <div className={clsx(style.card, inList && style.cardWithBackground)}>
      <div className={style.avatarWrapper}>
        <img
          src={avatar}
          alt={`${name} avatar`}
          className={style.avatarImage}
        />
      </div>
      <div className={style.details}>
        <div className={style.userName}>{name}</div>
        <div className={style.userMeta}>
          {department}, {role}
        </div>
      </div>
    </div>
  );
};
