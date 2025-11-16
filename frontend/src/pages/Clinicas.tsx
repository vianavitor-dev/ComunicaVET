import { useTranslation } from "react-i18next";
import Footer from "@/components/Footer";

const Clinicas = () => {
  const { t } = useTranslation();

  return (
    <div className="min-h-screen bg-background">
      <div className="container mx-auto px-4 py-16">
        <h1 className="text-4xl font-bold text-center mb-8">{t("clinics.title")}</h1>
        <p className="text-center text-muted-foreground text-lg">
          {t("clinics.description")}
        </p>
      </div>
      <Footer />
    </div>
  );
};

export default Clinicas;
