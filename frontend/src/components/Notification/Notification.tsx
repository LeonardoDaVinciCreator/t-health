import { useState } from "react";
import { CardClickable } from "../CardClickable/CardClickable";

interface NotificationState {
  text: string;
  icon: string;
}

interface NotificationProps {
  states: NotificationState[];
  initialState?: number;
}

export const Notification = ({
  states,
  initialState = 0,
}: NotificationProps) => {
  const [currentIndex, setCurrentIndex] = useState(initialState);

  const handleClick = () => {
    setCurrentIndex((prev) => (prev + 1) % states.length);
  };

  const current = states[currentIndex];

  return (
    <CardClickable
      title={current.text}
      icon={current.icon}
      onClick={handleClick}
    />
  );
};
