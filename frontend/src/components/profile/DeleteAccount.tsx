import { useState } from "react";
import { Card, CardContent } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { useToast } from "@/hooks/use-toast";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useTranslation } from "react-i18next";

const DeleteAccount = () => {
  const { t } = useTranslation();
  const [password, setPassword] = useState("");
  const [isDeleting, setIsDeleting] = useState(false);
  const { toast } = useToast();
  const navigate = useNavigate();

  const handleDelete = async () => {
    if (!password) {
      toast({
        title: t("deleteAccount.error"),
        description: t("deleteAccount.enterPassword"),
        variant: "destructive",
      });
      return;
    }

    setIsDeleting(true);
    try {
      const userId = localStorage.getItem("userId");
      const response = await axios.delete(
        `${import.meta.env.VITE_API_URL}/api/v1/users/deactivate/${userId}`,
        { data: { password } }
      );

      const { error, message } = response.data;

      if (error) {
        toast({
          title: t("deleteAccount.error"),
          description: message,
          variant: "destructive",
        });
        return;
      }

      toast({
        title: t("deleteAccount.success"),
        description: t("deleteAccount.accountDeactivated"),
      });
      
      localStorage.clear();
      navigate("/");
    } catch (error: any) {
      toast({
        title: t("deleteAccount.error"),
        description: error.response?.data?.message || t("deleteAccount.errorDeleting"),
        variant: "destructive",
      });
    } finally {
      setIsDeleting(false);
    }
  };

  return (
    <div className="max-w-3xl">
      <h1 className="text-3xl font-bold mb-6">{t("deleteAccount.title")}</h1>
      
      <Card>
        <CardContent className="pt-6 space-y-6">
          <div className="text-center space-y-2 py-4">
            <h2 className="text-xl font-semibold">
              {t("deleteAccount.confirmTitle")}
            </h2>
            <p className="text-lg">{t("deleteAccount.confirmDescription")}</p>
          </div>

          <div className="space-y-4">
            <div>
              <Label htmlFor="password">{t("deleteAccount.password")}</Label>
              <Input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder={t("deleteAccount.passwordPlaceholder")}
                className="mt-1"
              />
            </div>
          </div>

          <div className="flex justify-center pt-4">
            <Button 
              variant="destructive"
              onClick={handleDelete}
              disabled={isDeleting}
              className="px-8"
            >
              {isDeleting ? t("deleteAccount.deleting") : t("deleteAccount.confirm")}
            </Button>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default DeleteAccount;
