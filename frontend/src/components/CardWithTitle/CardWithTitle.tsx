import type { ReactNode } from "react";
import { Card } from "../Card/Card";
import style from "./CardWithTitle.module.css";

interface CardWithTitleProps {
  title: string;
  children: ReactNode;
}

export const CardWithTitle = ({ title, children }: CardWithTitleProps) => {
  return (
    <Card>
      <h2 className={style.title}>{title}</h2>
      <div className={style.content}>{children}</div>
    </Card>
  );
};
