import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { useNavigate } from "react-router-dom";
import { useToast } from "@/hooks/use-toast";
import { useTranslation } from "react-i18next";
import pataImage from "@/assets/pata.png";

const NovaSenha = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { toast } = useToast();
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    const userId = localStorage.getItem("changePassword_userId");
    
    if (!userId) {
      toast({
        title: t("login.error"),
        description: t("newPassword.sessionExpired"),
        variant: "destructive",
      });
      navigate("/recuperar-senha");
      return;
    }
    
    setIsLoading(true);
    
    try {
      const response = await fetch(
        `${import.meta.env.VITE_API_URL}/api/v1/users/${userId}/password?newPassword=${encodeURIComponent(password)}`,
        { method: "PATCH" }
      );
      
      if (!response.ok) {
        throw new Error(t("newPassword.errorChanging"));
      }
      
      localStorage.removeItem("changePassword_userId");
      
      toast({
        title: t("newPassword.passwordChanged"),
        description: t("newPassword.redirectingToLogin"),
      });
      
      navigate("/login");
    } catch (error) {
      toast({
        title: t("login.error"),
        description: t("newPassword.errorChanging"),
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
          <CardTitle className="text-3xl font-bold text-center">{t("newPassword.title")}</CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="space-y-2">
              <label htmlFor="password" className="text-sm font-medium">
                {t("newPassword.newPassword")}
              </label>
              <Input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                className="bg-muted"
              />
            </div>

            <div className="flex justify-center gap-6 pt-4">
              <button
                type="button"
                className="text-foreground font-medium hover:underline"
                onClick={() => navigate("/verificar-codigo")}
              >
                {t("newPassword.cancel")}
              </button>
              <Button 
                type="submit" 
                className="bg-[#00CED1] hover:bg-[#00CED1]/90 text-white px-8"
                disabled={isLoading}
              >
                {isLoading ? t("newPassword.changing") : t("newPassword.continue")}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  );
};

export default NovaSenha;
