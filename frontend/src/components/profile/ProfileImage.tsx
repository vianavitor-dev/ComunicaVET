import { useState, useRef } from "react";
import { Card, CardContent } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { Upload } from "lucide-react";
import { useToast } from "@/hooks/use-toast";
import { useTranslation } from "react-i18next";

const ProfileImage = () => {
  const { t } = useTranslation();
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [previewUrl, setPreviewUrl] = useState<string>("");
  const { toast } = useToast();
  const fileInputRef = useRef<HTMLInputElement>(null);

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (file) {
      setSelectedFile(file);
      const url = URL.createObjectURL(file);
      setPreviewUrl(url);
    }
  };

  const handleSave = async () => {
    if (!selectedFile) {
      toast({
        title: t("profileImage.error"),
        description: t("profileImage.selectImage"),
        variant: "destructive",
      });
      return;
    }

    try {
      const petOwnerId = localStorage.getItem("userId");
      
      if (!petOwnerId) {
        toast({
          title: t("profileImage.error"),
          description: t("profileImage.userNotFound"),
          variant: "destructive",
        });
        return;
      }

      const formData = new FormData();
      formData.append("file", selectedFile);

      const response = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/pet-owners/${petOwnerId}/profile-image`, {
        method: "POST",
        body: formData,
      });

      const result = await response.json();

      if (result.error) {
        toast({
          title: t("profileImage.error"),
          description: result.message,
          variant: "destructive",
        });
      } else {
        toast({
          title: t("profileImage.success"),
          description: result.message,
        });
        setSelectedFile(null);
        setPreviewUrl("");
        if (fileInputRef.current) {
          fileInputRef.current.value = "";
        }
      }
    } catch (error) {
      toast({
        title: t("profileImage.error"),
        description: t("profileImage.couldNotConnect"),
        variant: "destructive",
      });
    }
  };

  const handleCancel = () => {
    setSelectedFile(null);
    setPreviewUrl("");
    if (fileInputRef.current) {
      fileInputRef.current.value = "";
    }
  };

  return (
    <div className="max-w-3xl">
      <h1 className="text-3xl font-bold mb-6">{t("profileImage.title")}</h1>
      
      <Card>
        <CardContent className="pt-6 space-y-6">
          <div className="space-y-4">
            <Label>{t("profileImage.profileImage")}</Label>
            
            <div className="flex flex-col items-center gap-4">
              <div className="w-32 h-32 rounded-full border-2 border-dashed border-border flex items-center justify-center overflow-hidden bg-muted">
                {previewUrl ? (
                  <img src={previewUrl} alt="Preview" className="w-full h-full object-cover" />
                ) : (
                  <Upload className="h-8 w-8 text-muted-foreground" />
                )}
              </div>
              
              <input
                type="file"
                id="profile-image"
                accept="image/*"
                onChange={handleFileChange}
                className="hidden"
                ref={fileInputRef}
              />
              <Button
                variant="outline"
                onClick={() => document.getElementById("profile-image")?.click()}
              >
                {t("profileImage.chooseFile")}
              </Button>
              {selectedFile && (
                <p className="text-sm text-muted-foreground">{selectedFile.name}</p>
              )}
            </div>
          </div>

          <div className="flex justify-end gap-3 pt-4">
            <Button variant="outline" onClick={handleCancel}>
              {t("profileImage.cancel")}
            </Button>
            <Button onClick={handleSave}>
              {t("profileImage.save")}
            </Button>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default ProfileImage;
