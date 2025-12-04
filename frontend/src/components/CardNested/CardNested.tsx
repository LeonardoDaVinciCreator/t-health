import type { ReactNode } from "react";
import { Card } from "../Card/Card";
import style from "./CardNested.module.css";

interface CardNestedProps {
  title: string;
  children: ReactNode;
}

export const CardNested = ({ title, children }: CardNestedProps) => {
  return (
    <Card className={style.card}>
      <h3 className={style.title}>{title}</h3>
      <div className={style.content}>{children}</div>
    </Card>
  );
};
