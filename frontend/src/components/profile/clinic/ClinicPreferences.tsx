import { useState } from "react";
import { useToast } from "@/hooks/use-toast";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { useTranslation } from "react-i18next";

const ClinicPreferences = () => {
  const { t, i18n } = useTranslation();
  const [language, setLanguage] = useState(i18n.language);
  const { toast } = useToast();

  const handleSave = () => {
    localStorage.setItem("language", language);
    i18n.changeLanguage(language);
    toast({
      title: t("preferences.saved"),
      description: t("preferences.savedDescription"),
    });
  };

  const handleCancel = () => {
    // Reset to saved value or default
    const savedLanguage = localStorage.getItem("language") || "pt-BR";
    setLanguage(savedLanguage);
  };

  return (
    <div className="max-w-2xl">
      <h2 className="text-2xl font-bold mb-6">{t("preferences.title")}</h2>

      <div className="space-y-4">
        <div>
          <Label htmlFor="language">{t("preferences.language")}</Label>
          <Select value={language} onValueChange={setLanguage}>
            <SelectTrigger id="language" className="w-full">
              <SelectValue placeholder={t("preferences.language")} />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="pt-BR">{t("languages.pt-BR")}</SelectItem>
              <SelectItem value="en-US">{t("languages.en-US")}</SelectItem>
            </SelectContent>
          </Select>
        </div>

        <div className="flex gap-2 pt-4">
          <Button variant="outline" onClick={handleCancel}>
            {t("preferences.cancel")}
          </Button>
          <Button onClick={handleSave}>
            {t("preferences.save")}
          </Button>
        </div>
      </div>
    </div>
  );
};

export default ClinicPreferences;
