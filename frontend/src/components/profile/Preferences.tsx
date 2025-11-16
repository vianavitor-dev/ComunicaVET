import { useState } from "react";
import { Card, CardContent } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Button } from "@/components/ui/button";
import { useToast } from "@/hooks/use-toast";
import { useTranslation } from "react-i18next";

const Preferences = () => {
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
    const savedLanguage = localStorage.getItem("language");
    if (savedLanguage) {
      setLanguage(savedLanguage);
    }
  };

  return (
    <div className="max-w-3xl">
      <h1 className="text-3xl font-bold mb-6">{t("preferences.title")}</h1>
      
      <Card>
        <CardContent className="pt-6 space-y-6">
          <div className="space-y-2">
            <Label htmlFor="language">{t("preferences.language")}</Label>
            <Select value={language} onValueChange={setLanguage}>
              <SelectTrigger id="language" className="w-full">
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="pt-BR">{t("languages.pt-BR")}</SelectItem>
                <SelectItem value="en-US">{t("languages.en-US")}</SelectItem>
              </SelectContent>
            </Select>
          </div>

          <div className="flex justify-end gap-3 pt-4">
            <Button variant="outline" onClick={handleCancel}>
              {t("preferences.cancel")}
            </Button>
            <Button onClick={handleSave}>
              {t("preferences.save")}
            </Button>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default Preferences;
