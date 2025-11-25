import { Button } from "@/components/ui/button";
import { useNavigate } from "react-router-dom";
import { useToast } from "@/hooks/use-toast";
import { useTranslation } from "react-i18next";

const PetOwnerNavbar = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { toast } = useToast();

  const handleLogout = () => {
    localStorage.clear();
    toast({
      title: t("nav.logoutSuccess"),
      description: t("nav.logoutDescription"),
    });
    navigate("/login");
  };

  return (
    <header className="bg-primary py-4">
      <div className="container mx-auto px-4 flex items-center justify-between">
        <div className="text-2xl font-bold text-primary-foreground">ComunicaVET</div>
        <div className="flex items-center gap-4">
          <Button 
            variant="ghost" 
            className="text-primary-foreground hover:bg-primary-foreground/10"
            onClick={() => navigate("/")}
          >
            {t("nav.home")}
          </Button>
          <Button 
            variant="ghost" 
            className="text-primary-foreground hover:bg-primary-foreground/10"
            onClick={() => navigate("/meus-pets")}
          >
            {t("nav.myPets")}
          </Button>
          <Button 
            variant="ghost" 
            className="text-primary-foreground hover:bg-primary-foreground/10"
            onClick={() => navigate("/perfil")}
          >
            {t("nav.account")}
          </Button>
          <Button 
            variant="secondary"
            onClick={handleLogout}
          >
            {t("nav.logout")}
          </Button>
        </div>
      </div>
    </header>
  );
};

export default PetOwnerNavbar;
