import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import ptBR from "./locales/pt-BR.json";
import enUS from "./locales/en-US.json";

// Get saved language or default to pt-BR
const savedLanguage = localStorage.getItem("language") || "pt-BR";

i18n
  .use(initReactI18next)
  .init({
    resources: {
      "pt-BR": { translation: ptBR },
      "en-US": { translation: enUS },
    },
    lng: savedLanguage,
    fallbackLng: "pt-BR",
    interpolation: {
      escapeValue: false,
    },
    react: {
      useSuspense: false,
    },
  });

export default i18n;
