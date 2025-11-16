import { useState, useEffect } from "react";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { useNavigate } from "react-router-dom";
import { useToast } from "@/hooks/use-toast";
import { useTranslation } from "react-i18next";
import axios from "axios";

interface Focus {
  id: number;
  name: string;
  description: string | null;
}

const EscolherTags = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { toast } = useToast();
  const [isClinic, setIsClinic] = useState(false);
  const [userId, setUserId] = useState<number | null>(null);
  const [categories, setCategories] = useState<Focus[]>([]);
  const [selectedCategories, setSelectedCategories] = useState<string[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const storedIsClinic = localStorage.getItem("isClinic") === "true";
    const storedUserId = localStorage.getItem("userId");
    
    setIsClinic(storedIsClinic);
    setUserId(storedUserId ? parseInt(storedUserId) : null);

    if (!storedUserId) {
      toast({
        title: t("login.error"),
        description: t("tags.needsRegistration"),
        variant: "destructive",
      });
      navigate("/cadastro");
      return;
    }

    fetchCategories();
  }, [navigate, t, toast]);

  const fetchCategories = async () => {
    try {
      const response = await axios.get(`${import.meta.env.VITE_API_URL}/api/v1/focuses`);
      const { error, message, data } = response.data;

      if (error) {
        toast({
          title: t("login.error"),
          description: message,
          variant: "destructive",
        });
        return;
      }

      setCategories(data);
    } catch (error: any) {
      toast({
        title: t("login.error"),
        description: error.response?.data?.message || t("login.errorMessage"),
        variant: "destructive",
      });
    }
  };

  const toggleCategory = (categoryName: string) => {
    setSelectedCategories(prev => {
      if (prev.includes(categoryName)) {
        return prev.filter(c => c !== categoryName);
      }
      if (prev.length < 3) {
        return [...prev, categoryName];
      }
      return prev;
    });
  };

  const handleSubmit = async () => {
    if (selectedCategories.length === 0) {
      toast({
        title: t("tags.attention"),
        description: t("tags.selectAtLeastOne"),
        variant: "destructive",
      });
      return;
    }

    setIsLoading(true);

    try {
      const endpoint = isClinic 
        ? `${import.meta.env.VITE_API_URL}/api/v1/clinic-focuses`
        : `${import.meta.env.VITE_API_URL}/api/v1/pet-owner-focuses`;

      const requestData = isClinic
        ? { clinicId: userId, focusNames: selectedCategories }
        : { petOwnerId: userId, focusNames: selectedCategories };

      const response = await axios.post(endpoint, requestData);

      const { error, message } = response.data;

      toast({
        title: error ? t("login.error") : t("login.success"),
        description: message,
        variant: error ? "destructive" : "default",
      });

      if (!error) {
        navigate("/login");
      }
    } catch (error: any) {
      toast({
        title: t("login.error"),
        description: error.response?.data?.message || t("login.errorMessage"),
        variant: "destructive",
      });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-background flex items-center justify-center px-4 py-12">
      <div className="w-full max-w-3xl">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold mb-2">
            {isClinic ? t("tags.title") : t("tags.titlePetOwner")}
          </h1>
          <p className="text-muted-foreground">
            {t("tags.subtitle")}
          </p>
        </div>

        <div className="flex flex-wrap gap-3 justify-center mb-12">
          {categories.map((category) => (
            <Badge
              key={category.id}
              variant={selectedCategories.includes(category.name) ? "default" : "outline"}
              className="cursor-pointer px-6 py-3 text-base transition-all hover:scale-105"
              onClick={(e) => {
                e.preventDefault();
                e.stopPropagation();
                toggleCategory(category.name);
              }}
            >
              {category.name}
            </Badge>
          ))}
        </div>

        <div className="flex justify-center">
          <Button
            onClick={handleSubmit}
            disabled={isLoading || selectedCategories.length === 0}
            className="px-12 py-6 text-lg bg-[#00CED1] hover:bg-[#00CED1]/90 text-white"
          >
            {isLoading ? t("tags.processing") : t("tags.proceed")}
          </Button>
        </div>
      </div>
    </div>
  );
};

export default EscolherTags;
