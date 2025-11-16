import { useState } from "react";
import { useToast } from "@/hooks/use-toast";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Upload } from "lucide-react";
import { useTranslation } from "react-i18next";

const ClinicImages = () => {
  const { t } = useTranslation();
  const [profileImage, setProfileImage] = useState<File | null>(null);
  const [backgroundImage, setBackgroundImage] = useState<File | null>(null);
  const [petImage, setPetImage] = useState<File | null>(null);
  const { toast } = useToast();

  const handleFileChange = (
    event: React.ChangeEvent<HTMLInputElement>,
    setter: (file: File | null) => void
  ) => {
    const file = event.target.files?.[0];
    if (file) {
      setter(file);
    }
  };

  const handleSave = async () => {
    try {
      const clinicId = localStorage.getItem("userId");
      
      if (!clinicId) {
        toast({
          title: t("clinicImages.error"),
          description: t("clinicImages.clinicNotFound"),
          variant: "destructive",
        });
        return;
      }

      let successCount = 0;
      let errorMessages: string[] = [];

      // Upload profile image
      if (profileImage) {
        try {
          const formData = new FormData();
          formData.append("file", profileImage);

          const response = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/clinics/${clinicId}/profile-image`, {
            method: "POST",
            body: formData,
          });

          const result = await response.json();

          if (result.error) {
            errorMessages.push(`Imagem de perfil: ${result.message}`);
          } else {
            successCount++;
          }
        } catch (error) {
          errorMessages.push("Imagem de perfil: Não foi possível conectar ao servidor.");
        }
      }

      // Upload background image
      if (backgroundImage) {
        try {
          const formData = new FormData();
          formData.append("file", backgroundImage);

          const response = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/clinics/${clinicId}/background-image`, {
            method: "POST",
            body: formData,
          });

          const result = await response.json();

          if (result.error) {
            errorMessages.push(`Imagem de fundo: ${result.message}`);
          } else {
            successCount++;
          }
        } catch (error) {
          errorMessages.push("Imagem de fundo: Não foi possível conectar ao servidor.");
        }
      }

      // Show results
      if (errorMessages.length > 0) {
        toast({
          title: t("clinicImages.error"),
          description: errorMessages.join(" | "),
          variant: "destructive",
        });
      }
      
      if (successCount > 0) {
        toast({
          title: t("clinicImages.success"),
          description: `${successCount} ${t("clinicImages.imagesUpdated")}`,
        });
        setProfileImage(null);
        setBackgroundImage(null);
      }

      if (!profileImage && !backgroundImage) {
        toast({
          title: t("clinicImages.warning"),
          description: t("clinicImages.noImageSelected"),
        });
      }
    } catch (error) {
      toast({
        title: t("clinicImages.error"),
        description: t("clinicImages.errorProcessing"),
        variant: "destructive",
      });
    }
  };

  const handleCancel = () => {
    setProfileImage(null);
    setBackgroundImage(null);
    setPetImage(null);
  };

  return (
    <div className="max-w-2xl">
      <h2 className="text-2xl font-bold mb-6">{t("clinicImages.title")}</h2>

      <div className="space-y-6">
        <div>
          <Label htmlFor="profile-image">{t("clinicImages.profileImage")}</Label>
          <div className="mt-2 flex items-center gap-4">
            <div className="w-16 h-16 rounded-full border-2 border-dashed border-border flex items-center justify-center">
              <Upload className="h-6 w-6 text-muted-foreground" />
            </div>
            <input
              id="profile-image"
              type="file"
              accept="image/*"
              onChange={(e) => handleFileChange(e, setProfileImage)}
              className="hidden"
            />
            <Button
              type="button"
              variant="outline"
              onClick={() => document.getElementById('profile-image')?.click()}
            >
              {t("clinicImages.chooseFile")}
            </Button>
          </div>
          {profileImage && (
            <p className="text-sm text-muted-foreground mt-1">
              {t("clinicImages.selected")} {profileImage.name}
            </p>
          )}
        </div>

        <div>
          <Label htmlFor="background-image">{t("clinicImages.backgroundImage")}</Label>
          <div className="mt-2 flex items-center gap-4">
            <div className="w-16 h-16 rounded border-2 border-dashed border-border flex items-center justify-center">
              <Upload className="h-6 w-6 text-muted-foreground" />
            </div>
            <input
              id="background-image"
              type="file"
              accept="image/*"
              onChange={(e) => handleFileChange(e, setBackgroundImage)}
              className="hidden"
            />
            <Button
              type="button"
              variant="outline"
              onClick={() => document.getElementById('background-image')?.click()}
            >
              {t("clinicImages.chooseFile")}
            </Button>
          </div>
          {backgroundImage && (
            <p className="text-sm text-muted-foreground mt-1">
              {t("clinicImages.selected")} {backgroundImage.name}
            </p>
          )}
        </div>

        <div>
          <Label htmlFor="pet-image">{t("clinicImages.pageImages")}</Label>
          <div className="mt-2 flex items-center gap-4">
            <div className="w-16 h-16 rounded border-2 border-dashed border-border flex items-center justify-center">
              <Upload className="h-6 w-6 text-muted-foreground" />
            </div>
            <input
              id="pet-image"
              type="file"
              accept="image/*"
              onChange={(e) => handleFileChange(e, setPetImage)}
              className="hidden"
            />
            <Button
              type="button"
              variant="outline"
              onClick={() => document.getElementById('pet-image')?.click()}
            >
              {t("clinicImages.chooseFile")}
            </Button>
          </div>
          {petImage && (
            <p className="text-sm text-muted-foreground mt-1">
              {t("clinicImages.selected")} {petImage.name}
            </p>
          )}
        </div>

        <div className="flex gap-2 pt-4">
          <Button variant="outline" onClick={handleCancel}>
            {t("clinicImages.cancel")}
          </Button>
          <Button onClick={handleSave}>
            {t("clinicImages.save")}
          </Button>
        </div>
      </div>
    </div>
  );
};

export default ClinicImages;
