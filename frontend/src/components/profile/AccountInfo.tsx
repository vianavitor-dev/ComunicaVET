import { useState, useEffect } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import axios from "axios";
import { useToast } from "@/hooks/use-toast";
import { useTranslation } from "react-i18next";

const AccountInfo = () => {
  const { t } = useTranslation();
  const [userData, setUserData] = useState({
    name: "",
    email: "",
    country: "",
    state: "",
    city: "",
    neighborhood: "",
    street: "",
    number: "",
    complement: "",
  });
  const [isEditing, setIsEditing] = useState(false);
  const [isSaving, setIsSaving] = useState(false);
  const { toast } = useToast();

  useEffect(() => {
    fetchUserData();
  }, []);

  const fetchUserData = async () => {
    try {
      const userId = localStorage.getItem("userId");
      const response = await axios.get(`${import.meta.env.VITE_API_URL}/api/v1/pet-owners/profile/${userId}`);
      
      const { error, message, data } = response.data;
      
      if (error) {
        toast({
          title: t("login.error"),
          description: message,
          variant: "destructive",
        });
      } else {
        setUserData({
          name: data.name || "",
          email: data.email || "",
          country: data.address?.country || "",
          state: data.address?.state || "",
          city: data.address?.city || "",
          neighborhood: data.address?.neighborhood || "",
          street: data.address?.street || "",
          number: data.address?.number?.toString() || "",
          complement: data.address?.complement || "",
        });
      }
    } catch (error: any) {
      toast({
        title: t("login.error"),
        description: error.response?.data?.message || t("profile.errorLoading"),
        variant: "destructive",
      });
    }
  };

  const handleSave = async () => {
    setIsSaving(true);
    try {
      const userId = localStorage.getItem("userId");
      await axios.put(`${import.meta.env.VITE_API_URL}/api/v1/pet-owners/profile/${userId}`, {
        name: userData.name,
        email: userData.email,
        address: {
          country: userData.country,
          state: userData.state,
          city: userData.city,
          neighborhood: userData.neighborhood,
          street: userData.street,
          number: userData.number ? parseInt(userData.number) : null,
          complement: userData.complement || null,
        },
      });
      
      toast({
        title: t("profile.success"),
        description: t("profile.dataUpdated"),
      });
      setIsEditing(false);
    } catch (error: any) {
      toast({
        title: t("login.error"),
        description: error.response?.data?.message || t("profile.errorSaving"),
        variant: "destructive",
      });
    } finally {
      setIsSaving(false);
    }
  };

  return (
    <div className="max-w-3xl">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-3xl font-bold">{t("profile.accountInfo")}</h1>
        <div className="flex gap-2">
          {isEditing ? (
            <>
              <Button variant="outline" onClick={() => setIsEditing(false)}>
                {t("profile.cancel")}
              </Button>
              <Button onClick={handleSave} disabled={isSaving}>
                {isSaving ? t("profile.saving") : t("profile.save")}
              </Button>
            </>
          ) : (
            <Button onClick={() => setIsEditing(true)}>{t("profile.edit")}</Button>
          )}
        </div>
      </div>
      
      <Card>
        <CardContent className="pt-6 space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <Label htmlFor="name">{t("profile.name")}</Label>
              <Input
                id="name"
                value={userData.name}
                onChange={(e) => setUserData({ ...userData, name: e.target.value })}
                readOnly={!isEditing}
                className="mt-1"
              />
            </div>
            <div>
              <Label htmlFor="email">{t("profile.email")}</Label>
              <Input
                id="email"
                value={userData.email}
                onChange={(e) => setUserData({ ...userData, email: e.target.value })}
                readOnly={!isEditing}
                className="mt-1"
              />
            </div>
          </div>

          <div>
            <Label className="text-lg font-semibold">{t("profile.address")}</Label>
            <div className="mt-3 space-y-3">
              <div>
                <Label htmlFor="country">{t("profile.country")}</Label>
                <Input
                  id="country"
                  value={userData.country}
                  onChange={(e) => setUserData({ ...userData, country: e.target.value })}
                  readOnly={!isEditing}
                  className="mt-1"
                />
              </div>
              <div>
                <Label htmlFor="state">{t("profile.state")}</Label>
                <Input
                  id="state"
                  value={userData.state}
                  onChange={(e) => setUserData({ ...userData, state: e.target.value })}
                  readOnly={!isEditing}
                  className="mt-1"
                />
              </div>
              <div>
                <Label htmlFor="city">{t("profile.city")}</Label>
                <Input
                  id="city"
                  value={userData.city}
                  onChange={(e) => setUserData({ ...userData, city: e.target.value })}
                  readOnly={!isEditing}
                  className="mt-1"
                />
              </div>
              <div>
                <Label htmlFor="neighborhood">{t("profile.neighborhood")}</Label>
                <Input
                  id="neighborhood"
                  value={userData.neighborhood}
                  onChange={(e) => setUserData({ ...userData, neighborhood: e.target.value })}
                  readOnly={!isEditing}
                  className="mt-1"
                />
              </div>
              <div>
                <Label htmlFor="street">{t("profile.street")}</Label>
                <Input
                  id="street"
                  value={userData.street}
                  onChange={(e) => setUserData({ ...userData, street: e.target.value })}
                  readOnly={!isEditing}
                  className="mt-1"
                />
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <Label htmlFor="number">{t("profile.number")}</Label>
                  <Input
                    id="number"
                    value={userData.number}
                    onChange={(e) => setUserData({ ...userData, number: e.target.value })}
                    readOnly={!isEditing}
                    className="mt-1"
                  />
                </div>
                <div>
                  <Label htmlFor="complement">{t("profile.complement")}</Label>
                  <Input
                    id="complement"
                    value={userData.complement}
                    onChange={(e) => setUserData({ ...userData, complement: e.target.value })}
                    readOnly={!isEditing}
                    className="mt-1"
                  />
                </div>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default AccountInfo;
