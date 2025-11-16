import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { useNavigate } from "react-router-dom";
import { useToast } from "@/hooks/use-toast";
import { useTranslation } from "react-i18next";
import pataImage from "@/assets/pata.png";
import {
  InputOTP,
  InputOTPGroup,
  InputOTPSlot,
} from "@/components/ui/input-otp";

const VerificarCodigo = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { toast } = useToast();
  const [code, setCode] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (code.length !== 6) {
      toast({
        title: t("verifyCode.invalidCode"),
        description: t("verifyCode.enterSixDigits"),
        variant: "destructive",
      });
      return;
    }
    
    setIsLoading(true);
    
    try {
      const response = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/recover-passwords/verify?code=${encodeURIComponent(code)}`, {
        method: "POST"
      });
      
      if (!response.ok) {
        throw new Error(t("verifyCode.invalidCode"));
      }
      
      toast({
        title: t("verifyCode.codeVerified"),
        description: t("verifyCode.redirecting"),
      });
      
      navigate("/nova-senha");
    } catch (error) {
      toast({
        title: t("login.error"),
        description: t("verifyCode.invalidOrExpired"),
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
          <CardTitle className="text-3xl font-bold text-center">{t("verifyCode.title")}</CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="space-y-4">
              <div className="flex justify-center">
                <InputOTP
                  maxLength={6}
                  value={code}
                  onChange={(value) => setCode(value)}
                >
                  <InputOTPGroup>
                    <InputOTPSlot index={0} />
                    <InputOTPSlot index={1} />
                    <InputOTPSlot index={2} />
                    <InputOTPSlot index={3} />
                    <InputOTPSlot index={4} />
                    <InputOTPSlot index={5} />
                  </InputOTPGroup>
                </InputOTP>
              </div>
              <p className="text-sm text-center font-medium">
                {t("verifyCode.instruction")}
              </p>
            </div>

            <div className="flex justify-center gap-6 pt-4">
              <button
                type="button"
                className="text-foreground font-medium hover:underline"
                onClick={() => navigate("/recuperar-senha")}
              >
                {t("verifyCode.cancel")}
              </button>
              <Button 
                type="submit" 
                className="bg-[#00CED1] hover:bg-[#00CED1]/90 text-white px-8"
                disabled={isLoading}
              >
                {isLoading ? t("verifyCode.verifying") : t("verifyCode.continue")}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  );
};

export default VerificarCodigo;
