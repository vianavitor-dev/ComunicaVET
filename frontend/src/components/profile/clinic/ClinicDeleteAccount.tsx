import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useToast } from "@/hooks/use-toast";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import axios from "axios";
import { useTranslation } from "react-i18next";

const ClinicDeleteAccount = () => {
  const { t } = useTranslation();
  const [password, setPassword] = useState("");
  const [isDeleting, setIsDeleting] = useState(false);
  const navigate = useNavigate();
  const { toast } = useToast();

  const handleDelete = async () => {
    if (!password) {
      toast({
        title: t("clinicDelete.error"),
        description: t("clinicDelete.enterPassword"),
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
          title: t("clinicDelete.error"),
          description: message,
          variant: "destructive",
        });
        return;
      }

      toast({
        title: t("clinicDelete.success"),
        description: t("clinicDelete.accountDeactivated"),
      });
      
      localStorage.clear();
      navigate("/");
    } catch (error: any) {
      toast({
        title: t("clinicDelete.error"),
        description: error.response?.data?.message || t("clinicDelete.errorDeleting"),
        variant: "destructive",
      });
    } finally {
      setIsDeleting(false);
    }
  };

  return (
    <div className="max-w-2xl">
      <h2 className="text-2xl font-bold mb-6">{t("clinicDelete.title")}</h2>

      <div className="space-y-6">
        <p className="text-center font-semibold">
          {t("clinicDelete.confirmMessage")}
        </p>

        <div>
          <Label htmlFor="password">{t("clinicDelete.password")}</Label>
          <Input
            id="password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder={t("clinicDelete.passwordPlaceholder")}
          />
        </div>

        <div className="flex justify-center pt-4">
          <Button
            variant="destructive"
            onClick={handleDelete}
            disabled={isDeleting}
          >
            {isDeleting ? t("clinicDelete.deleting") : t("clinicDelete.confirm")}
          </Button>
        </div>
      </div>
    </div>
  );
};

export default ClinicDeleteAccount;
