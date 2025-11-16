import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { useNavigate } from "react-router-dom";
import { useToast } from "@/hooks/use-toast";
import { useTranslation } from "react-i18next";
import pataImage from "@/assets/pata.png";

const RecuperarSenha = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { toast } = useToast();
  const [email, setEmail] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    
    try {
      const response = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/email-senders?to=${encodeURIComponent(email)}`, {
        method: "POST"
      });
      
      const responseData = await response.json();
      
      if (!response.ok || responseData.error) {
        throw new Error(responseData.message || t("passwordRecovery.errorSending"));
      }
      
      localStorage.setItem("changePassword_userId", responseData.data);
      
      toast({
        title: t("passwordRecovery.emailSent"),
        description: t("passwordRecovery.checkInbox"),
      });
      
      navigate("/verificar-codigo");
    } catch (error) {
      toast({
        title: t("login.error"),
        description: t("passwordRecovery.errorSending"),
        variant: "destructive",
      });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-background flex items-center justify-center px-4 relative">
      <div 
        className="absolute inset-0 opacity-30"
        style={{
          backgroundImage: `url(${pataImage})`,
          backgroundSize: '80%',
          backgroundPosition: 'center',
          backgroundRepeat: 'no-repeat'
        }}
      />
      <Card className="w-full max-w-md shadow-xl relative z-10">
        <CardHeader>
          <CardTitle className="text-3xl font-bold text-center">{t("passwordRecovery.title")}</CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="space-y-2">
              <Input
                id="email"
                type="email"
                placeholder={t("passwordRecovery.placeholder")}
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                className="bg-muted"
              />
              <p className="text-sm text-center font-medium">
                {t("passwordRecovery.instruction")}
              </p>
            </div>

            <div className="flex justify-center gap-6 pt-4">
              <button
                type="button"
                className="text-foreground font-medium hover:underline"
                onClick={() => navigate("/login")}
              >
                {t("passwordRecovery.cancel")}
              </button>
              <Button 
                type="submit" 
                className="bg-[#00CED1] hover:bg-[#00CED1]/90 text-white px-8"
                disabled={isLoading}
              >
                {isLoading ? t("passwordRecovery.sending") : t("passwordRecovery.continue")}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  );
};

export default RecuperarSenha;
