import { Link } from "react-router-dom";
import { ArrowUp } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useTranslation } from "react-i18next";
import { useEffect, useState } from "react";

const Footer = () => {
  const { t } = useTranslation();
  const [isClinic, setIsClinic] = useState(false);
  const [userId, setUserId] = useState<string>("");

  useEffect(() => {
    const clinicStatus = localStorage.getItem("isClinic") === "true";
    const storedUserId = localStorage.getItem("userId") || "";
    setIsClinic(clinicStatus);
    setUserId(storedUserId);
  }, []);

  const scrollToTop = () => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const homeLink = isClinic ? `/clinica-perfil/${userId}` : "/";
  const profileLink = isClinic ? "/perfil-clinica" : "/perfil";

  return (
    <footer className="bg-primary text-primary-foreground py-12">
      <div className="container mx-auto px-4">
        {/* Main Navigation */}
        <div className="flex flex-wrap justify-center gap-8 mb-8">
          <Link to={homeLink} className="hover:underline transition-all">
            {t("footer.main")}
          </Link>
          <Link to={profileLink} className="hover:underline transition-all">
            {t("footer.profile")}
          </Link>
          <Link to="/cadastro" className="hover:underline transition-all">
            {t("footer.signup")}
          </Link>
          <Link to="/login" className="hover:underline transition-all">
            {t("footer.login")}
          </Link>
        </div>

        {/* Scroll to Top Button */}
        <div className="flex justify-center mb-8">
          <Button 
            onClick={scrollToTop}
            variant="outline" 
            className="bg-primary-foreground/10 border-primary-foreground/20 text-primary-foreground hover:bg-primary-foreground/20"
          >
            <ArrowUp className="mr-2 h-4 w-4" />
            {t("footer.scrollToTop")}
          </Button>
        </div>

        {/* Legal Links */}
        <div className="flex flex-wrap justify-center gap-6 text-sm">
          <Link to="/politica-privacidade" className="hover:underline transition-all">
            {t("footer.privacy")}
          </Link>
          <Link to="/termos-uso" className="hover:underline transition-all">
            {t("footer.terms")}
          </Link>
          <Link to="/aviso-legal" className="hover:underline transition-all">
            {t("footer.legal")}
          </Link>
          <Link to="/politica-cookies" className="hover:underline transition-all">
            {t("footer.cookies")}
          </Link>
        </div>

        {/* Copyright */}
        <div className="text-center mt-8 text-sm opacity-80">
          © {new Date().getFullYear()} ComunicaVET. {t("footer.rights")}.
        </div>
      </div>
    </footer>
  );
};

export default Footer;
