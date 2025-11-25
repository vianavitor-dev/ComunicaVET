import { useState, useEffect } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent } from "@/components/ui/card";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog";
import { Label } from "@/components/ui/label";
import { Search, MapPin, Star, Plus } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { useToast } from "@/hooks/use-toast";
import { useTranslation } from "react-i18next";
import axios from "axios";
import pataImage from "@/assets/pata.png";
import PetOwnerNavbar from "@/components/PetOwnerNavbar";
import Footer from "@/components/Footer";

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

interface Focus {
  name: string;
  description: string | null;
}

const Index = () => {
  const { t } = useTranslation();
  const [searchQuery, setSearchQuery] = useState("");
  const [clinics, setClinics] = useState<Clinic[]>([]);
  const [categories, setCategories] = useState<Focus[]>([]);
  const [selectedCategories, setSelectedCategories] = useState<string[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isAddressDialogOpen, setIsAddressDialogOpen] = useState(false);
  const [newAddress, setNewAddress] = useState({
    street: "",
    city: "",
    state: "",
    country: "brazil"
  });
  const { toast } = useToast();
  const navigate = useNavigate();

  useEffect(() => {
    const userId = localStorage.getItem("userId");
    if (!userId) {
      navigate("/index");
      return;
    }
    
    fetchCategories();
    fetchClinics();
  }, [navigate]);

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
        setCategories([]);
      } else {
        setCategories(data || []);
      }
    } catch (error: any) {
      toast({
        title: t("login.error"),
        description: error.response?.data?.message || t("login.errorMessage"),
        variant: "destructive",
      });
      setCategories([]);
    }
  };

  const fetchClinics = async (addressDto?: any) => {
    setIsLoading(true);
    try {
      const userId = localStorage.getItem("userId");
      let response;

      // If there's a search query, use the search endpoint
      if (searchQuery.trim()) {
        response = await axios.get(`${import.meta.env.VITE_API_URL}/api/v1/clinics/search?name=${encodeURIComponent(searchQuery)}`);
      } else {
        // Otherwise, use the filter endpoint with categories
        const requestData = {
          userId: userId ? parseInt(userId) : null,
          tagNames: selectedCategories.length > 0 ? selectedCategories : null,
          newAddressDto: addressDto || null
        };

        response = await axios.post(`${import.meta.env.VITE_API_URL}/api/v1/clinics/filter`, requestData);
      }

      const { error, message, data } = response.data;

      if (error) {
        toast({
          title: t("login.error"),
          description: message,
          variant: "destructive",
        });
        setClinics([]);
      } else {
        setClinics(data || []);
      }
    } catch (error: any) {
      toast({
        title: t("login.error"),
        description: error.response?.data?.message || t("login.errorMessage"),
        variant: "destructive",
      });
      setClinics([]);
      setSelectedCategories([]);
    } finally {
      setIsLoading(false);
    }
  };

  const handleAddressSubmit = () => {
    if (!newAddress.street || !newAddress.city || !newAddress.state) {
      toast({
        title: t("home.error"),
        description: t("home.fillAllAddressFields"),
        variant: "destructive",
      });
      return;
    }

    const addressDto = {
      amenity: null,
      street: newAddress.street,
      city: newAddress.city,
      state: newAddress.state,
      county: null,
      country: newAddress.country,
      postalCode: null
    };

    fetchClinics(addressDto);
    setIsAddressDialogOpen(false);
  };

  const toggleCategory = (categoryName: string) => {
    setSelectedCategories(prev => {
      if (prev.includes(categoryName)) {
        return prev.filter(c => c !== categoryName);
      } else {
        return [...prev, categoryName];
      }
    });
  };

  const userId = localStorage.getItem("userId");
  const isLoggedIn = !!userId;

  return (
    <div className="min-h-screen bg-background">
      {/* Header */}
      {isLoggedIn ? (
        <PetOwnerNavbar />
      ) : (
        <header className="bg-primary py-4">
          <div className="container mx-auto px-4 flex items-center justify-between">
            <div className="flex items-center gap-2">
              <img src={pataImage} alt="ComunicaVET" className="h-8 w-8" />
              <h1 className="text-2xl font-bold text-primary-foreground">ComunicaVET</h1>
            </div>
            <div className="flex items-center gap-4">
              <Button variant="ghost" className="text-primary-foreground hover:bg-primary-foreground/10" asChild>
                <Link to="/login">{t("home.enter")}</Link>
              </Button>
              <Button variant="secondary" asChild>
                <Link to="/cadastro">{t("home.register")}</Link>
              </Button>
            </div>
          </div>
        </header>
      )}

      {/* Search Section */}
      <section className="bg-background py-12">
        <div className="container mx-auto px-4">
          <div className="max-w-4xl mx-auto space-y-8">
            <div className="text-center space-y-4">
              <h2 className="text-3xl md:text-4xl font-bold text-foreground">
                {t("home.searchTitle")}
              </h2>
              <p className="text-muted-foreground">
                {t("home.searchSubtitle")}
              </p>
            </div>

            {/* Categories */}
            <div className="flex flex-wrap gap-3 justify-center">
              {categories.map((category, index) => (
                <Button
                  key={index}
                  variant={selectedCategories.includes(category.name) ? "default" : "outline"}
                  className="rounded-full"
                  onClick={() => toggleCategory(category.name)}
                >
                  {category.name}
                </Button>
              ))}
            </div>

            {/* Search Bar */}
            <div className="relative max-w-2xl mx-auto">
              <Search className="absolute left-4 top-1/2 -translate-y-1/2 h-5 w-5 text-muted-foreground" />
              <Input
                type="text"
                placeholder={t("home.searchPlaceholder")}
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-12 py-6 text-lg"
              />
            </div>

            <div className="flex gap-4 justify-center items-center">
              <Button size="lg" className="px-12" onClick={() => fetchClinics()} disabled={isLoading}>
                {isLoading ? t("home.searching") : t("home.search")}
              </Button>
              <Button 
                size="lg" 
                variant="outline" 
                className="gap-2"
                onClick={() => setIsAddressDialogOpen(true)}
              >
                <Plus className="h-5 w-5" />
                {t("home.addAddress")}
              </Button>
            </div>
          </div>
        </div>
      </section>

      {/* Clinics Section */}
      <section className="bg-muted py-12">
        <div className="container mx-auto px-4">
          {isLoading ? (
            <div className="text-center py-12">
              <p className="text-muted-foreground">{t("home.loadingClinics")}</p>
            </div>
          ) : clinics.length === 0 ? (
            <div className="text-center py-12">
              <p className="text-muted-foreground">{t("home.noClinics")}</p>
            </div>
          ) : (
            <div className="grid md:grid-cols-3 gap-6 max-w-6xl mx-auto">
              {clinics.map((clinic, index) => (
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
                    <p className="text-sm text-muted-foreground">({clinic.viewsCount} {t("home.views")})</p>
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
                    <Button variant="outline" className="w-full mt-2" asChild>
                      <Link to={`/clinica-perfil/${clinic.id}`}>{t("home.seeMore")}</Link>
                    </Button>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </div>
      </section>

      {/* Footer */}
      <Footer />

      {/* Address Dialog */}
      <Dialog open={isAddressDialogOpen} onOpenChange={setIsAddressDialogOpen}>
        <DialogContent className="sm:max-w-md">
          <DialogHeader>
            <DialogTitle>{t("home.addNewAddress")}</DialogTitle>
          </DialogHeader>
          <div className="space-y-4 py-4">
            <div className="space-y-2">
              <Label htmlFor="street">{t("home.street")}</Label>
              <Input
                id="street"
                placeholder={t("home.streetPlaceholder")}
                value={newAddress.street}
                onChange={(e) => setNewAddress({ ...newAddress, street: e.target.value })}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="city">{t("home.city")}</Label>
              <Input
                id="city"
                placeholder={t("home.cityPlaceholder")}
                value={newAddress.city}
                onChange={(e) => setNewAddress({ ...newAddress, city: e.target.value })}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="state">{t("home.state")}</Label>
              <Input
                id="state"
                placeholder={t("home.statePlaceholder")}
                value={newAddress.state}
                onChange={(e) => setNewAddress({ ...newAddress, state: e.target.value })}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="country">{t("home.country")}</Label>
              <Input
                id="country"
                placeholder={t("home.countryPlaceholder")}
                value={newAddress.country}
                onChange={(e) => setNewAddress({ ...newAddress, country: e.target.value })}
              />
            </div>
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setIsAddressDialogOpen(false)}>
              {t("home.cancel")}
            </Button>
            <Button onClick={handleAddressSubmit}>
              {t("home.searchByAddress")}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default Index;
