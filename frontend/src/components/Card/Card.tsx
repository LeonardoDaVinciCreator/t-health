import type { ReactNode } from "react";
import style from "./Card.module.css";
import clsx from "clsx";

interface CardProps {
  children: ReactNode;
  className?: string;
}

export const Card = ({ children, className }: CardProps) => {
  return <div className={clsx(style.card, className)}>{children}</div>;
};
