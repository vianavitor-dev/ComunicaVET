import { useState, useEffect } from "react";
import { useToast } from "@/hooks/use-toast";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Badge } from "@/components/ui/badge";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { X } from "lucide-react";
import axios from "axios";
import { useTranslation } from "react-i18next";

interface Contact {
  id?: number | null;
  contactTypeName: string;
  value: string;
}

interface Focus {
  name: string;
  description: string | null;
}

interface ClinicData {
  name: string;
  email: string;
  address: {
    country: string;
    state: string;
    city: string;
    neighborhood: string;
    street: string;
    number: string;
    complement: string;
  };
  description: string;
  contacts: Contact[];
  focuses: Focus[];
}

const CONTACT_TYPES = [
  { name: "social", label: "Social" },
  { name: "phone-number", label: "Phone Number" },
  { name: "e-mail", label: "E-mail" },
];

const ClinicAccountInfo = () => {
  const { t } = useTranslation();
  const [isEditing, setIsEditing] = useState(false);
  const [isSaving, setIsSaving] = useState(false);
  const [modifyAddress, setModifyAddress] = useState(false);
  const [modifyContacts, setModifyContacts] = useState(false);
  const [clinicData, setClinicData] = useState<ClinicData>({
    name: "",
    email: "",
    address: {
      country: "",
      state: "",
      city: "",
      neighborhood: "",
      street: "",
      number: "",
      complement: "",
    },
    description: "",
    contacts: [
      { id: null, contactTypeName: "social", value: "" },
      { id: null, contactTypeName: "phone-number", value: "" },
      { id: null, contactTypeName: "e-mail", value: "" },
    ],
    focuses: [],
  });
  const [newFocus, setNewFocus] = useState("");
  const { toast } = useToast();

  useEffect(() => {
    fetchClinicData();
  }, []);

  const fetchClinicData = async () => {
    try {
      const userId = localStorage.getItem("userId");
      const response = await axios.get(`${import.meta.env.VITE_API_URL}/api/v1/clinics/profile/${userId}`);
      
      if (response.data && response.data.data) {
        const clinic = response.data.data;
        setClinicData({
          name: clinic.name || "",
          email: clinic.email || "",
          address: {
            country: clinic.address?.country || "",
            state: clinic.address?.state || "",
            city: clinic.address?.city || "",
            neighborhood: clinic.address?.neighborhood || "",
            street: clinic.address?.street || "",
            number: clinic.address?.number?.toString() || "",
            complement: clinic.address?.complement || "",
          },
          description: clinic.description || "",
          contacts: clinic.contacts && clinic.contacts.length > 0 
            ? clinic.contacts 
            : [
                { id: null, contactTypeName: "social", value: "" },
                { id: null, contactTypeName: "phone-number", value: "" },
                { id: null, contactTypeName: "e-mail", value: "" },
              ],
          focuses: clinic.focuses || [],
        });
      }
    } catch (error) {
      toast({
        title: t("login.error"),
        description: t("clinicProfile.errorLoading"),
        variant: "destructive",
      });
    }
  };

  const handleSave = async () => {
    setIsSaving(true);
    try {
      const userId = localStorage.getItem("userId");
      const queryParams = new URLSearchParams({
        modifyAddress: modifyAddress.toString(),
        modifyContacts: modifyContacts.toString(),
      });
      await axios.put(`${import.meta.env.VITE_API_URL}/api/v1/clinics/profile/${userId}?${queryParams}`, {
        name: clinicData.name,
        email: clinicData.email,
        address: {
          country: clinicData.address.country,
          state: clinicData.address.state,
          city: clinicData.address.city,
          neighborhood: clinicData.address.neighborhood,
          street: clinicData.address.street,
          number: clinicData.address.number ? parseInt(clinicData.address.number) : null,
          complement: clinicData.address.complement || null,
        },
        description: clinicData.description,
        contacts: clinicData.contacts.filter(c => c.value.trim() !== "").map(c => ({
          id: c.id || null,
          contactTypeName: c.contactTypeName,
          value: c.value
        })),
        focuses: clinicData.focuses,
      });

      toast({
        title: t("clinicProfile.success"),
        description: t("clinicProfile.dataUpdated"),
      });
      setIsEditing(false);
      setModifyAddress(false);
      setModifyContacts(false);
    } catch (error) {
      toast({
        title: t("login.error"),
        description: t("clinicProfile.errorSaving"),
        variant: "destructive",
      });
    } finally {
      setIsSaving(false);
    }
  };

  const handleAddFocus = () => {
    if (newFocus.trim() && !clinicData.focuses.find(f => f.name === newFocus.trim())) {
      setClinicData({
        ...clinicData,
        focuses: [...clinicData.focuses, { name: newFocus.trim(), description: null }],
      });
      setNewFocus("");
    }
  };

  const handleRemoveFocus = (focusToRemove: string) => {
    setClinicData({
      ...clinicData,
      focuses: clinicData.focuses.filter(focus => focus.name !== focusToRemove),
    });
  };

  return (
    <div className="max-w-2xl">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold">{t("clinicProfile.accountInfo")}</h2>
        {!isEditing ? (
          <Button onClick={() => setIsEditing(true)}>{t("clinicProfile.edit")}</Button>
        ) : (
          <div className="flex gap-2">
            <Button variant="outline" onClick={() => setIsEditing(false)}>
              {t("clinicProfile.cancel")}
            </Button>
            <Button onClick={handleSave} disabled={isSaving}>
              {isSaving ? t("clinicProfile.saving") : t("clinicProfile.save")}
            </Button>
          </div>
        )}
      </div>

      <div className="space-y-4">
        <div>
          <Label htmlFor="name">{t("clinicProfile.name")}</Label>
          <Input
            id="name"
            value={clinicData.name}
            onChange={(e) => setClinicData({ ...clinicData, name: e.target.value })}
            disabled={!isEditing}
          />
        </div>

        <div>
          <Label htmlFor="email">{t("clinicProfile.email")}</Label>
          <Input
            id="email"
            type="email"
            value={clinicData.email}
            onChange={(e) => setClinicData({ ...clinicData, email: e.target.value })}
            disabled={!isEditing}
          />
        </div>

        <div className="space-y-2">
          <Label>{t("clinicProfile.address")}</Label>
          <div className="grid grid-cols-2 gap-2">
            <div>
              <Label htmlFor="country" className="text-sm">{t("clinicProfile.country")}</Label>
              <Input
                id="country"
                value={clinicData.address.country}
                onChange={(e) => {
                  setClinicData({
                    ...clinicData,
                    address: { ...clinicData.address, country: e.target.value }
                  });
                  setModifyAddress(true);
                }}
                disabled={!isEditing}
              />
            </div>
            <div>
              <Label htmlFor="state" className="text-sm">{t("clinicProfile.state")}</Label>
              <Input
                id="state"
                value={clinicData.address.state}
                onChange={(e) => {
                  setClinicData({
                    ...clinicData,
                    address: { ...clinicData.address, state: e.target.value }
                  });
                  setModifyAddress(true);
                }}
                disabled={!isEditing}
              />
            </div>
          </div>
          <div className="grid grid-cols-2 gap-2">
            <div>
              <Label htmlFor="city" className="text-sm">{t("clinicProfile.city")}</Label>
              <Input
                id="city"
                value={clinicData.address.city}
                onChange={(e) => {
                  setClinicData({
                    ...clinicData,
                    address: { ...clinicData.address, city: e.target.value }
                  });
                  setModifyAddress(true);
                }}
                disabled={!isEditing}
              />
            </div>
            <div>
              <Label htmlFor="neighborhood" className="text-sm">{t("clinicProfile.neighborhood")}</Label>
              <Input
                id="neighborhood"
                value={clinicData.address.neighborhood}
                onChange={(e) => {
                  setClinicData({
                    ...clinicData,
                    address: { ...clinicData.address, neighborhood: e.target.value }
                  });
                  setModifyAddress(true);
                }}
                disabled={!isEditing}
              />
            </div>
          </div>
          <div className="grid grid-cols-3 gap-2">
            <div className="col-span-2">
              <Label htmlFor="street" className="text-sm">{t("clinicProfile.street")}</Label>
              <Input
                id="street"
                value={clinicData.address.street}
                onChange={(e) => {
                  setClinicData({
                    ...clinicData,
                    address: { ...clinicData.address, street: e.target.value }
                  });
                  setModifyAddress(true);
                }}
                disabled={!isEditing}
              />
            </div>
            <div>
              <Label htmlFor="number" className="text-sm">{t("clinicProfile.number")}</Label>
              <Input
                id="number"
                value={clinicData.address.number}
                onChange={(e) => {
                  setClinicData({
                    ...clinicData,
                    address: { ...clinicData.address, number: e.target.value }
                  });
                  setModifyAddress(true);
                }}
                disabled={!isEditing}
              />
            </div>
          </div>
          <div>
            <Label htmlFor="complement" className="text-sm">{t("clinicProfile.complement")}</Label>
            <Input
              id="complement"
              value={clinicData.address.complement}
              onChange={(e) => {
                setClinicData({
                  ...clinicData,
                  address: { ...clinicData.address, complement: e.target.value }
                });
                setModifyAddress(true);
              }}
              disabled={!isEditing}
            />
          </div>
        </div>

        <div>
          <Label htmlFor="description">{t("clinicProfile.description")}</Label>
          <Textarea
            id="description"
            value={clinicData.description}
            onChange={(e) => setClinicData({ ...clinicData, description: e.target.value })}
            disabled={!isEditing}
            rows={6}
            className="resize-none"
          />
        </div>

        <div className="space-y-2">
          <Label>{t("clinicProfile.contact")}</Label>
          {clinicData.contacts.map((contact, index) => (
            <div key={index} className="space-y-2">
              <Label htmlFor={`contact-${index}`} className="text-sm">{t("clinicProfile.contactLabel")} {index + 1}</Label>
              <div className="grid grid-cols-2 gap-2">
                <Input
                  id={`contact-${index}`}
                  value={contact.value}
                  onChange={(e) => {
                    const newContacts = [...clinicData.contacts];
                    newContacts[index] = { ...newContacts[index], value: e.target.value };
                    setClinicData({ ...clinicData, contacts: newContacts });
                    setModifyContacts(true);
                  }}
                  disabled={!isEditing}
                  placeholder={t("clinicProfile.contactPlaceholder")}
                />
                <Select
                  value={contact.contactTypeName}
                  onValueChange={(value) => {
                    const newContacts = [...clinicData.contacts];
                    newContacts[index] = { ...newContacts[index], contactTypeName: value };
                    setClinicData({ ...clinicData, contacts: newContacts });
                    setModifyContacts(true);
                  }}
                  disabled={!isEditing}
                >
                  <SelectTrigger>
                    <SelectValue placeholder={t("clinicProfile.contactType")} />
                  </SelectTrigger>
                  <SelectContent>
                    {CONTACT_TYPES.map((type) => (
                      <SelectItem key={type.name} value={type.name}>
                        {type.label}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
            </div>
          ))}
        </div>

        <div>
          <Label>{t("clinicProfile.focuses")}</Label>
          <div className="flex flex-wrap gap-2 mb-2 min-h-[40px] p-2 border rounded">
            {clinicData.focuses.map((focus, index) => (
              <Badge key={index} variant="secondary" className="flex items-center gap-1">
                {focus.name}
                {isEditing && (
                  <button
                    onClick={() => handleRemoveFocus(focus.name)}
                    className="ml-1 hover:text-destructive"
                  >
                    <X className="h-3 w-3" />
                  </button>
                )}
              </Badge>
            ))}
          </div>
          {isEditing && (
            <div className="flex gap-2">
              <Input
                value={newFocus}
                onChange={(e) => setNewFocus(e.target.value)}
                onKeyPress={(e) => e.key === 'Enter' && handleAddFocus()}
                placeholder={t("clinicProfile.focusPlaceholder")}
              />
              <Button onClick={handleAddFocus} variant="secondary">
                {t("clinicProfile.addFocus")}
              </Button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ClinicAccountInfo;
