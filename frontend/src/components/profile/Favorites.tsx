import { useState, useEffect } from "react";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { MapPin, Star } from "lucide-react";
import axios from "axios";
import { useToast } from "@/hooks/use-toast";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";

interface Clinic {
  id: number;
  name: string;
  contacts: any[];
  countryName: string;
  stateName: string;
  cityName: string;
  street: string;
  stars: number;
  viewsCount: number;
  wasFavorited: boolean;
}

const Favorites = () => {
  const { t } = useTranslation();
  const [favoriteClinics, setFavoriteClinics] = useState<Clinic[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const { toast } = useToast();
  const navigate = useNavigate();

  useEffect(() => {
    fetchFavorites();
  }, []);

  const fetchFavorites = async () => {
    setIsLoading(true);
    try {
      const userId = localStorage.getItem("userId");
      const response = await axios.post(`${import.meta.env.VITE_API_URL}/api/v1/clinics/filter`, {
        userId: userId ? parseInt(userId) : null,
        tagNames: null,
        newAddressDto: null,
      });

      const { error, message, data } = response.data;

      if (error) {
        toast({
          title: t("login.error"),
          description: message,
          variant: "destructive",
        });
        setFavoriteClinics([]);
      } else {
        // Filter only favorited clinics
        const favorites = (data || []).filter((clinic: Clinic) => clinic.wasFavorited);
        setFavoriteClinics(favorites);
      }
    } catch (error: any) {
      toast({
        title: t("login.error"),
        description: error.response?.data?.message || t("login.errorMessage"),
        variant: "destructive",
      });
      setFavoriteClinics([]);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="max-w-5xl">
      <h1 className="text-3xl font-bold mb-6">{t("favorites.title")}</h1>

      {isLoading ? (
        <div className="text-center py-12">
          <p className="text-muted-foreground">{t("favorites.loading")}</p>
        </div>
      ) : favoriteClinics.length === 0 ? (
        <div className="text-center py-12">
          <p className="text-muted-foreground">{t("favorites.noFavorites")}</p>
        </div>
      ) : (
        <div className="grid md:grid-cols-2 gap-6">
          {favoriteClinics.map((clinic, index) => (
            <Card key={index} className="overflow-hidden hover:shadow-lg transition-shadow">
              <div className="relative h-48">
                <img
                  src="https://images.unsplash.com/photo-1628009368231-7bb7cfcb0def?w=400&h=300&fit=crop"
                  alt={clinic.name}
                  className="w-full h-full object-cover"
                />
              </div>
              <CardContent className="p-4 space-y-2">
                <h3 className="font-bold text-lg">{clinic.name}</h3>
                <p className="text-sm text-muted-foreground">({clinic.viewsCount} {t("favorites.views")})</p>
                <div className="flex items-center gap-1 text-sm">
                  <MapPin className="h-4 w-4 text-muted-foreground" />
                  <span className="text-muted-foreground">{clinic.street}</span>
                </div>
                <p className="text-sm font-medium">
                  {clinic.cityName} - {clinic.stateName}
                </p>
                <div className="flex items-center gap-1">
                  {[...Array(5)].map((_, i) => (
                    <Star
                      key={i}
                      className={`h-4 w-4 ${
                        i < Math.floor(clinic.stars)
                          ? "fill-primary text-primary"
                          : "text-muted"
                      }`}
                    />
                  ))}
                </div>
                <Button 
                  variant="outline" 
                  className="w-full mt-2"
                  onClick={() => navigate(`/clinica-perfil/${clinic.id}`)}
                >
                  {t("favorites.seeMore")}
                </Button>
              </CardContent>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
};

export default Favorites;
