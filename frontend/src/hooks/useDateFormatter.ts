import { useTranslation } from "react-i18next";
import { format } from "date-fns";
import { ptBR, enUS } from "date-fns/locale";

export const useDateFormatter = () => {
  const { i18n } = useTranslation();
  
  const getLocale = () => {
    return i18n.language === "pt-BR" ? ptBR : enUS;
  };

  const formatDate = (date: string | Date, formatStr: string = "P") => {
    let dateObj: Date;
    
    if (typeof date === "string") {
      // Parse as local date to avoid timezone issues
      const [year, month, day] = date.split("-").map(Number);
      dateObj = new Date(year, month - 1, day);
    } else {
      dateObj = date;
    }
    
    return format(dateObj, formatStr, { locale: getLocale() });
  };

  return { formatDate };
};
