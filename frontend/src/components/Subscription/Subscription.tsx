import style from "./Subscription.module.css";
import { type User as UserType } from "../../types/user";
import { User } from "../User/User";
import { CardWithTitle } from "../CardWithTitle/CardWithTitle";

interface SubscriptionProps {
  users: UserType[];
}

export const Subscription = ({ users }: SubscriptionProps) => {
  return (
    <CardWithTitle title="Подписки">
      <ul className={style.list}>
        {users.map((user, index) => (
          <li key={index}>
            <User
              avatar={user.avatar}
              name={user.name}
              department={user.department}
              role={user.role}
              inList={true}
            />
          </li>
        ))}
      </ul>
    </CardWithTitle>
  );
};
