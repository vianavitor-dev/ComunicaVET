import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Link, useNavigate } from "react-router-dom";
import { useToast } from "@/hooks/use-toast";
import { useTranslation } from "react-i18next";
import axios from "axios";
import pataImage from "@/assets/pata.png";

const Login = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { toast } = useToast();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);

    try {
      const response = await axios.post("http://localhost:8080/api/v1/users/login", {
        email,
        password,
      });

      const { error, message, data } = response.data;

      if (error) {
        toast({
          title: t("login.error"),
          description: message,
          variant: "destructive",
        });
        setEmail("");
        setPassword("");
      } else {
        // Store user ID and clinic status in localStorage
        localStorage.setItem("userId", data.id.toString());
        localStorage.setItem("isClinic", data.isClinic.toString());
        
        toast({
          title: t("login.success"),
          description: message || t("login.successMessage"),
        });
        
        // Redirect based on user type
        if (data.isClinic) {
          navigate(`/clinica-perfil/${data.id}`);
        } else {
          navigate("/");
        }
      }
    } catch (error: any) {
      toast({
        title: t("login.error"),
        description: error.response?.data?.message || t("login.errorMessage"),
        variant: "destructive",
      });
      setEmail("");
      setPassword("");
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
          <CardTitle className="text-3xl text-center font-bold">{t("login.title")}</CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="email">{t("login.email")}</Label>
              <Input 
                id="email" 
                type="email" 
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="senha">{t("login.password")}</Label>
              <Input 
                id="senha" 
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>
          
          <div className="text-sm">
            {t("login.forgotPassword")}{" "}
            <Link to="/recuperar-senha" className="text-[#00CED1] hover:underline font-semibold">
              {t("login.recoverHere")}
            </Link>
          </div>

            <div className="flex gap-4 pt-2">
              <Button
                type="button"
                variant="outline"
                className="flex-1"
                onClick={() => navigate("/")}
              >
                {t("login.back")}
              </Button>
              <Button 
                type="submit" 
                className="flex-1 bg-[#00CED1] hover:bg-[#00CED1]/90 text-white"
                disabled={isLoading}
              >
                {isLoading ? t("login.entering") : t("login.enter")}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  );
};

export default Login;
