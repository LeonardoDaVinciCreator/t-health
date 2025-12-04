import { Input } from "../Input/Input";
import { CardWithTitle } from "../CardWithTitle/CardWithTitle";

export const Contacts = () => {
  return (
    <CardWithTitle title="Контакты">
      <Input text="Email" type="email" placeholder="Email" hasControl={true} />
      <Input
        text="Телефон"
        type="tel"
        placeholder="Телефон"
        nodes="Данная информация не будет отображаться в профиле"
        hasControl={true}
      />
    </CardWithTitle>
  );
};
