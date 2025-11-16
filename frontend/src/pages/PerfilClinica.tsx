import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useToast } from "@/hooks/use-toast";
import pataImage from "@/assets/pata.png";
import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import { Settings, Menu, X } from "lucide-react";
import ClinicAccountInfo from "@/components/profile/clinic/ClinicAccountInfo";
import ClinicPreferences from "@/components/profile/clinic/ClinicPreferences";
import ClinicImages from "@/components/profile/clinic/ClinicImages";
import ClinicDeleteAccount from "@/components/profile/clinic/ClinicDeleteAccount";
import ClinicNavbar from "@/components/ClinicNavbar";
import { useTranslation } from "react-i18next";

type Section = "account" | "preferences" | "images" | "delete";

const PerfilClinica = () => {
  const { t } = useTranslation();
  const [activeSection, setActiveSection] = useState<Section>("account");
  const [clinicName, setClinicName] = useState("");
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const navigate = useNavigate();
  const { toast } = useToast();

  useEffect(() => {
    // const userId = localStorage.getItem("userId");
    // const isClinic = localStorage.getItem("isClinic") === "true";
    
    // if (!userId || !isClinic) {
    //   toast({
    //     title: "Acesso negado",
    //     description: "Você precisa estar logado como clínica para acessar esta página.",
    //     variant: "destructive",
    //   });
    //   navigate("/login");
    //   return;
    // }

    // Get clinic name from localStorage or fetch from API
    const storedName = localStorage.getItem("userName");
    if (storedName) {
      setClinicName(storedName);
    }
  }, [navigate, toast]);

  const handleLogout = () => {
    localStorage.clear();
    toast({
      title: t("nav.logoutSuccess"),
      description: t("nav.logoutDescription"),
    });
    navigate("/login");
  };

  const renderContent = () => {
    switch (activeSection) {
      case "account":
        return <ClinicAccountInfo />;
      case "preferences":
        return <ClinicPreferences />;
      case "images":
        return <ClinicImages />;
      case "delete":
        return <ClinicDeleteAccount />;
      default:
        return <ClinicAccountInfo />;
    }
  };

  return (
    <div className="min-h-screen bg-background">
      {/* Header */}
      <ClinicNavbar />

      {/* Main Content */}
      <div className="flex min-h-[calc(100vh-4rem)] relative">
        {/* Mobile Overlay */}
        {mobileMenuOpen && (
          <div 
            className="fixed inset-0 bg-black/50 z-40 md:hidden"
            onClick={() => setMobileMenuOpen(false)}
          />
        )}
        
        {/* Sidebar */}
        <aside className={`
          fixed md:static
          top-0 left-0 h-full
          w-64 bg-card border-r border-border p-6
          z-50 md:z-0
          transform transition-transform duration-300 ease-in-out
          ${mobileMenuOpen ? 'translate-x-0' : '-translate-x-full md:translate-x-0'}
        `}>
          <div className="space-y-6">
            {/* Sua Conta Section */}
            <div>
              <div className="flex items-center gap-2 mb-4">
                <div className="p-1 bg-foreground rounded">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" className="text-background">
                    <rect x="3" y="3" width="18" height="18" rx="2" stroke="currentColor" strokeWidth="2"/>
                  </svg>
                </div>
                <h2 className="font-semibold text-foreground">{t("profile.yourAccount")}</h2>
              </div>
              <div className="space-y-1">
                <button
                  onClick={() => {
                    setActiveSection("account");
                    setMobileMenuOpen(false);
                  }}
                  className={`w-full text-left px-4 py-2 rounded transition-colors ${
                    activeSection === "account"
                      ? "text-primary font-medium"
                      : "text-foreground hover:bg-muted"
                  }`}
                >
                  {t("profile.yourProfile")}
                </button>
              </div>
            </div>

            <Separator />

            {/* Configuração Section */}
            <div>
              <div className="flex items-center gap-2 mb-4">
                <Settings className="h-4 w-4" />
                <h2 className="font-semibold text-foreground">{t("profile.configuration")}</h2>
              </div>
              <div className="space-y-1">
                <button
                  onClick={() => {
                    setActiveSection("account");
                    setMobileMenuOpen(false);
                  }}
                  className={`w-full text-left px-4 py-2 rounded transition-colors ${
                    activeSection === "account"
                      ? "text-primary font-medium"
                      : "text-foreground hover:bg-muted"
                  }`}
                >
                  {t("profile.editAccount")}
                </button>
                <button
                  onClick={() => {
                    setActiveSection("images");
                    setMobileMenuOpen(false);
                  }}
                  className={`w-full text-left px-4 py-2 rounded transition-colors ${
                    activeSection === "images"
                      ? "text-primary font-medium"
                      : "text-foreground hover:bg-muted"
                  }`}
                >
                  {t("profile.images")}
                </button>
                <button
                  onClick={() => {
                    setActiveSection("preferences");
                    setMobileMenuOpen(false);
                  }}
                  className={`w-full text-left px-4 py-2 rounded transition-colors ${
                    activeSection === "preferences"
                      ? "text-primary font-medium"
                      : "text-foreground hover:bg-muted"
                  }`}
                >
                  {t("profile.preferences")}
                </button>
                <button
                  onClick={() => {
                    setActiveSection("delete");
                    setMobileMenuOpen(false);
                  }}
                  className={`w-full text-left px-4 py-2 rounded transition-colors ${
                    activeSection === "delete"
                      ? "text-primary font-medium"
                      : "text-foreground hover:bg-muted"
                  }`}
                >
                  {t("profile.delete")}
                </button>
                <button
                  onClick={() => {
                    handleLogout();
                    setMobileMenuOpen(false);
                  }}
                  className="w-full text-left px-4 py-2 rounded text-destructive hover:bg-muted transition-colors"
                >
                  {t("profile.logout")}
                </button>
              </div>
            </div>
          </div>
        </aside>

        {/* Content Area */}
        <main className="flex-1 p-4 md:p-8">
          {renderContent()}
        </main>
      </div>
    </div>
  );
};

export default PerfilClinica;
