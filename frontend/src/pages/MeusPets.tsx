import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useToast } from "@/hooks/use-toast";
import { useTranslation } from "react-i18next";
import { useDateFormatter } from "@/hooks/useDateFormatter";
import PetOwnerNavbar from "@/components/PetOwnerNavbar";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Plus, Trash2, Calendar, Ruler, Dog, Sparkles } from "lucide-react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Badge } from "@/components/ui/badge";
import { Skeleton } from "@/components/ui/skeleton";

interface Pet {
  id: number;
  name: string;
  firstBreed: string;
}

interface PetDetails {
  id: number;
  name: string;
  type: "DOG" | "CAT" | "OTHER";
  breed: string;
  secondBreed: string;
  size: "SMALL" | "MEDIUM" | "BIG";
  dateBirth: string;
  petOwnerId: number;
}

interface ClinicHistory {
  id: number;
  registeredAt: string;
  registeredClinic: {
    id: number;
    name: string;
  };
}

interface Recommendation {
  type: string;
  priority: "HIGH" | "MEDIUM" | "LOW";
  message: string;
  reason: string;
}

interface AIRecommendationResponse {
  analysis: {
    age_category: string;
    special_care_needed: boolean;
  };
  recommendations: Recommendation[];
  tips: string[];
}

const MeusPets = () => {
  const { t } = useTranslation();
  const { toast } = useToast();
  const { formatDate } = useDateFormatter();
  const navigate = useNavigate();
  const [pets, setPets] = useState<Pet[]>([]);
  const [selectedPet, setSelectedPet] = useState<PetDetails | null>(null);
  const [clinicHistory, setClinicHistory] = useState<ClinicHistory[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [isHistoryDialogOpen, setIsHistoryDialogOpen] = useState(false);
  const [isRecommendationsDialogOpen, setIsRecommendationsDialogOpen] = useState(false);
  const [aiRecommendations, setAiRecommendations] = useState<AIRecommendationResponse | null>(null);
  const [isLoadingRecommendations, setIsLoadingRecommendations] = useState(false);

  // Form state
  const [formData, setFormData] = useState({
    name: "",
    dateBirth: "",
    breed: "",
    secondBreed: "",
    type: "DOG" as "DOG" | "CAT" | "OTHER",
    size: "MEDIUM" as "SMALL" | "MEDIUM" | "BIG",
  });

  const userId = localStorage.getItem("userId");
  const isClinic = localStorage.getItem("isClinic") === "true";

  useEffect(() => {
    if (!userId || isClinic) {
      navigate("/login");
      return;
    }
    fetchPets();
  }, [userId, isClinic, navigate]);

  const fetchPets = async () => {
    try {
      setIsLoading(true);
      const response = await axios.get(
        `${import.meta.env.VITE_API_URL}/api/v1/pets/owner/${userId}`
      );
      
      if (!response.data.error) {
        setPets(response.data.data || []);
      }
    } catch (error: any) {
      toast({
        title: t("login.error"),
        description: error.response?.data?.message || t("pets.errorLoading"),
        variant: "destructive",
      });
    } finally {
      setIsLoading(false);
    }
  };

  const fetchClinicHistory = async (petId: number) => {
    try {
      const response = await axios.get(
        `${import.meta.env.VITE_API_URL}/api/v1/pet-clinic-history/pet/${petId}`
      );
      
      setClinicHistory(response.data.data || []);
    } catch (error: any) {
      toast({
        title: t("login.error"),
        description: error.response?.data?.message || t("pets.errorLoadingHistory"),
        variant: "destructive",
      });
    }
  };

  const handleCreatePet = async () => {
    if (!formData.name || !formData.dateBirth) {
      toast({
        title: t("login.error"),
        description: t("pets.fillRequired"),
        variant: "destructive",
      });
      return;
    }

    try {
      await axios.post(`${import.meta.env.VITE_API_URL}/api/v1/pets`, {
        ...formData,
        petOwnerId: parseInt(userId!),
      });

      toast({
        title: t("login.success"),
        description: t("pets.createSuccess"),
      });

      setIsDialogOpen(false);
      setFormData({
        name: "",
        dateBirth: "",
        breed: "",
        secondBreed: "",
        type: "DOG",
        size: "MEDIUM",
      });
      fetchPets();
    } catch (error: any) {
      toast({
        title: t("login.error"),
        description: error.response?.data?.message || t("pets.errorCreating"),
        variant: "destructive",
      });
    }
  };

  const handleDeletePet = async (petId: number) => {
    try {
      await axios.delete(
        `${import.meta.env.VITE_API_URL}/api/v1/pets/${petId}`
      );

      toast({
        title: t("login.success"),
        description: t("pets.deleteSuccess"),
      });

      fetchPets();
    } catch (error: any) {
      toast({
        title: t("login.error"),
        description: error.response?.data?.message || t("pets.errorDeleting"),
        variant: "destructive",
      });
    }
  };

  const handleDeleteFromHistory = async (historyId: number) => {
    try {
      await axios.delete(
        `${import.meta.env.VITE_API_URL}/api/v1/pet-clinic-history/${historyId}`
      );

      toast({
        title: t("login.success"),
        description: t("pets.deleteFromHistorySuccess"),
      });

      if (selectedPet) {
        fetchClinicHistory(selectedPet.id);
      }
    } catch (error: any) {
      toast({
        title: t("login.error"),
        description: error.response?.data?.message || t("pets.errorDeletingFromHistory"),
        variant: "destructive",
      });
    }
  };

  const handleViewPetDetails = async (pet: Pet) => {
    try {
      const response = await axios.get(
        `${import.meta.env.VITE_API_URL}/api/v1/pets/${pet.id}`
      );
      
      if (!response.data.error) {
        setSelectedPet(response.data.data);
        fetchClinicHistory(pet.id);
        setIsHistoryDialogOpen(true);
      }
    } catch (error) {
      console.error("Error fetching pet details:", error);
      toast({
        title: t("login.error"),
        description: t("pets.errorLoadingDetails"),
        variant: "destructive",
      });
    }
  };

  const calculateAgeInMonths = (birthDate: string): number => {
    const birth = new Date(birthDate);
    const today = new Date();
    const months = (today.getFullYear() - birth.getFullYear()) * 12 + 
                   (today.getMonth() - birth.getMonth());
    return months;
  };

  const handleGetRecommendations = async () => {
    if (!selectedPet) return;

    try {
      setIsLoadingRecommendations(true);
      const ageInMonths = calculateAgeInMonths(selectedPet.dateBirth);
      
      const response = await axios.post(
        `${import.meta.env.VITE_API_URL}/api/v1/ai/recommendation`,
        {
          pet_name: selectedPet.name,
          pet_type: selectedPet.type,
          pet_breed: selectedPet.breed,
          pet_size: selectedPet.size,
          pet_age_months: ageInMonths,
        }
      );

      if (!response.data.error && response.data.data) {
        setAiRecommendations(response.data.data);
        setIsRecommendationsDialogOpen(true);
      }
    } catch (error: any) {
      toast({
        title: t("login.error"),
        description: error.response?.data?.message || t("pets.errorLoadingRecommendations"),
        variant: "destructive",
      });
    } finally {
      setIsLoadingRecommendations(false);
    }
  };

  const getPriorityColor = (priority: string) => {
    switch (priority) {
      case "HIGH":
        return "destructive";
      case "MEDIUM":
        return "default";
      case "LOW":
        return "secondary";
      default:
        return "default";
    }
  };

  if (isLoading) {
    return (
      <div className="min-h-screen bg-background">
        <PetOwnerNavbar />
        <div className="container mx-auto px-4 py-8 flex items-center justify-center">
          <p className="text-muted-foreground">{t("pets.loading")}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background">
      <PetOwnerNavbar />
      
      <div className="container mx-auto px-4 py-8">
        <div className="flex items-center justify-between mb-8">
          <h1 className="text-4xl font-bold text-foreground">{t("pets.title")}</h1>
          
          <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
            <DialogTrigger asChild>
              <Button className="gap-2">
                <Plus className="h-4 w-4" />
                {t("pets.addNew")}
              </Button>
            </DialogTrigger>
            <DialogContent>
              <DialogHeader>
                <DialogTitle>{t("pets.registerNew")}</DialogTitle>
                <DialogDescription>{t("pets.fillPetInfo")}</DialogDescription>
              </DialogHeader>
              
              <div className="space-y-4 py-4">
                <div className="space-y-2">
                  <Label htmlFor="name">{t("pets.name")}</Label>
                  <Input
                    id="name"
                    value={formData.name}
                    onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                    placeholder={t("pets.namePlaceholder")}
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="dateBirth">{t("pets.birthDate")}</Label>
                  <Input
                    id="dateBirth"
                    type="date"
                    value={formData.dateBirth}
                    onChange={(e) => setFormData({ ...formData, dateBirth: e.target.value })}
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="type">{t("pets.type")}</Label>
                  <Select
                    value={formData.type}
                    onValueChange={(value) => setFormData({ ...formData, type: value as any })}
                  >
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="DOG">{t("pets.typeDog")}</SelectItem>
                      <SelectItem value="CAT">{t("pets.typeCat")}</SelectItem>
                      <SelectItem value="OTHER">{t("pets.typeOther")}</SelectItem>
                    </SelectContent>
                  </Select>
                </div>

                <div className="space-y-2">
                  <Label htmlFor="breed">{t("pets.breed")}</Label>
                  <Input
                    id="breed"
                    value={formData.breed}
                    onChange={(e) => setFormData({ ...formData, breed: e.target.value })}
                    placeholder={t("pets.breedPlaceholder")}
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="secondBreed">{t("pets.secondBreed")}</Label>
                  <Input
                    id="secondBreed"
                    value={formData.secondBreed}
                    onChange={(e) => setFormData({ ...formData, secondBreed: e.target.value })}
                    placeholder={t("pets.secondBreedPlaceholder")}
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="size">{t("pets.size")}</Label>
                  <Select
                    value={formData.size}
                    onValueChange={(value) => setFormData({ ...formData, size: value as any })}
                  >
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="SMALL">{t("pets.sizeSmall")}</SelectItem>
                      <SelectItem value="MEDIUM">{t("pets.sizeMedium")}</SelectItem>
                      <SelectItem value="BIG">{t("pets.sizeBig")}</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
              </div>

              <div className="flex justify-end gap-2">
                <Button variant="outline" onClick={() => setIsDialogOpen(false)}>
                  {t("profile.cancel")}
                </Button>
                <Button onClick={handleCreatePet}>{t("pets.register")}</Button>
              </div>
            </DialogContent>
          </Dialog>
        </div>

        {pets.length === 0 ? (
          <Card>
            <CardContent className="py-12 text-center">
              <Dog className="h-16 w-16 mx-auto mb-4 text-muted-foreground" />
              <p className="text-muted-foreground">{t("pets.noPets")}</p>
              <p className="text-sm text-muted-foreground mt-2">{t("pets.addFirstPet")}</p>
            </CardContent>
          </Card>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {pets.map((pet) => (
              <Card key={pet.id} className="hover:shadow-lg transition-shadow">
                <CardHeader>
                  <CardTitle className="flex items-center justify-between">
                    <span>{pet.name}</span>
                    <AlertDialog>
                      <AlertDialogTrigger asChild>
                        <Button variant="ghost" size="icon" className="text-destructive hover:text-destructive">
                          <Trash2 className="h-4 w-4" />
                        </Button>
                      </AlertDialogTrigger>
                      <AlertDialogContent>
                        <AlertDialogHeader>
                          <AlertDialogTitle>{t("pets.confirmDelete")}</AlertDialogTitle>
                          <AlertDialogDescription>
                            {t("pets.confirmDeleteDescription")}
                          </AlertDialogDescription>
                        </AlertDialogHeader>
                        <AlertDialogFooter>
                          <AlertDialogCancel>{t("profile.cancel")}</AlertDialogCancel>
                          <AlertDialogAction onClick={() => handleDeletePet(pet.id)}>
                            {t("pets.delete")}
                          </AlertDialogAction>
                        </AlertDialogFooter>
                      </AlertDialogContent>
                    </AlertDialog>
                  </CardTitle>
                </CardHeader>
                <CardContent className="space-y-3">
                  <div className="flex items-center gap-2 text-sm text-muted-foreground">
                    <Dog className="h-4 w-4" />
                    <span>{pet.firstBreed}</span>
                  </div>
                  <Button 
                    variant="outline" 
                    className="w-full mt-4"
                    onClick={() => handleViewPetDetails(pet)}
                  >
                    {t("pets.viewDetails")}
                  </Button>
                </CardContent>
              </Card>
            ))}
          </div>
        )}

        {/* Pet Details Dialog */}
        <Dialog open={isHistoryDialogOpen} onOpenChange={setIsHistoryDialogOpen}>
          <DialogContent className="max-w-2xl">
            <DialogHeader>
              <DialogTitle>{selectedPet?.name}</DialogTitle>
              <DialogDescription>{t("pets.petDetails")}</DialogDescription>
            </DialogHeader>
            
            {selectedPet && (
              <div className="space-y-6">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <Label className="text-muted-foreground">{t("pets.name")}</Label>
                    <p className="font-medium">{selectedPet.name}</p>
                  </div>
                  <div>
                    <Label className="text-muted-foreground">{t("pets.type")}</Label>
                    <p className="font-medium">{t(`pets.type${selectedPet.type}`)}</p>
                  </div>
                  <div>
                    <Label className="text-muted-foreground">{t("pets.breed")}</Label>
                    <p className="font-medium">{selectedPet.breed}</p>
                  </div>
                  {selectedPet.secondBreed && (
                    <div>
                      <Label className="text-muted-foreground">{t("pets.secondBreed")}</Label>
                      <p className="font-medium">{selectedPet.secondBreed}</p>
                    </div>
                  )}
                  <div>
                    <Label className="text-muted-foreground">{t("pets.size")}</Label>
                    <p className="font-medium">{t(`pets.size${selectedPet.size}`)}</p>
                  </div>
                  <div>
                    <Label className="text-muted-foreground">{t("pets.birthDate")}</Label>
                    <p className="font-medium">{formatDate(selectedPet.dateBirth)}</p>
                  </div>
                </div>

                <div className="flex justify-end">
                  <Button 
                    onClick={handleGetRecommendations}
                    disabled={isLoadingRecommendations}
                    className="gap-2"
                  >
                    <Sparkles className="h-4 w-4" />
                    {isLoadingRecommendations ? t("pets.loadingRecommendations") : t("pets.getRecommendations")}
                  </Button>
                </div>

                <div>
                  <h3 className="font-semibold text-lg mb-4">{t("pets.clinicHistory")}</h3>
                  {clinicHistory.length === 0 ? (
                    <p className="text-muted-foreground text-sm">{t("pets.noHistory")}</p>
                  ) : (
                    <div className="space-y-2">
                      {clinicHistory.map((history) => (
                        <Card key={history.id}>
                          <CardContent className="py-3 flex items-center justify-between">
                            <div>
                              <p className="font-medium">{history.registeredClinic.name}</p>
                              <p className="text-sm text-muted-foreground">
                                {formatDate(history.registeredAt)}
                              </p>
                            </div>
                            <div className="flex gap-2">
                              <Button
                                variant="outline"
                                size="sm"
                                onClick={() => {
                                  setIsHistoryDialogOpen(false);
                                  navigate(`/clinica-perfil/${history.registeredClinic.id}`);
                                }}
                              >
                                {t("pets.visitClinic")}
                              </Button>
                              <AlertDialog>
                                <AlertDialogTrigger asChild>
                                  <Button variant="ghost" size="sm" className="text-destructive hover:text-destructive">
                                    <Trash2 className="h-4 w-4" />
                                  </Button>
                                </AlertDialogTrigger>
                                <AlertDialogContent>
                                  <AlertDialogHeader>
                                    <AlertDialogTitle>{t("pets.confirmRemoveFromHistory")}</AlertDialogTitle>
                                    <AlertDialogDescription>
                                      {t("pets.confirmRemoveFromHistoryDescription")}
                                    </AlertDialogDescription>
                                  </AlertDialogHeader>
                                  <AlertDialogFooter>
                                    <AlertDialogCancel>{t("profile.cancel")}</AlertDialogCancel>
                                    <AlertDialogAction onClick={() => handleDeleteFromHistory(history.id)}>
                                      {t("pets.delete")}
                                    </AlertDialogAction>
                                  </AlertDialogFooter>
                                </AlertDialogContent>
                              </AlertDialog>
                            </div>
                          </CardContent>
                        </Card>
                      ))}
                    </div>
                  )}
                </div>
              </div>
            )}
          </DialogContent>
        </Dialog>

        {/* AI Recommendations Dialog */}
        <Dialog open={isRecommendationsDialogOpen} onOpenChange={setIsRecommendationsDialogOpen}>
          <DialogContent className="max-w-4xl max-h-[80vh] overflow-y-auto">
            <DialogHeader>
              <DialogTitle className="flex items-center gap-2">
                <Sparkles className="h-5 w-5" />
                {t("pets.aiRecommendationsTitle")}
              </DialogTitle>
              <DialogDescription>
                {t("pets.aiRecommendationsDescription")}
              </DialogDescription>
            </DialogHeader>

            {aiRecommendations && (
              <div className="space-y-4">
                <div className="flex gap-2 items-center text-sm">
                  <Badge variant="outline">
                    {t("pets.ageCategory")}: {aiRecommendations.analysis.age_category}
                  </Badge>
                  {aiRecommendations.analysis.special_care_needed && (
                    <Badge variant="secondary">
                      {t("pets.specialCareNeeded")}
                    </Badge>
                  )}
                </div>

                <Tabs defaultValue="recommendations" className="w-full">
                  <TabsList className="grid w-full grid-cols-2">
                    <TabsTrigger value="recommendations">
                      {t("pets.recommendations")} ({aiRecommendations.recommendations.length})
                    </TabsTrigger>
                    <TabsTrigger value="tips">
                      {t("pets.tips")} ({aiRecommendations.tips.length})
                    </TabsTrigger>
                  </TabsList>

                  <TabsContent value="recommendations" className="space-y-4 mt-4">
                    {aiRecommendations.recommendations.map((rec, index) => (
                      <Card key={index}>
                        <CardHeader>
                          <div className="flex items-start justify-between gap-2">
                            <CardTitle className="text-lg">{rec.message}</CardTitle>
                            <Badge variant={getPriorityColor(rec.priority)}>
                              {rec.priority && t(`pets.priorities.${rec.priority}`) !== `pets.priorities.${rec.priority}` 
                                ? t(`pets.priorities.${rec.priority}`) 
                                : rec.priority}
                            </Badge>
                          </div>
                        </CardHeader>
                        <CardContent>
                          <div className="space-y-2">
                            <div>
                              <Label className="text-xs text-muted-foreground">{t("pets.type")}</Label>
                              <p className="text-sm">
                                {rec.type && t(`pets.recommendationTypes.${rec.type}`) !== `pets.recommendationTypes.${rec.type}` 
                                  ? t(`pets.recommendationTypes.${rec.type}`) 
                                  : rec.type}
                              </p>
                            </div>
                            <div>
                              <Label className="text-xs text-muted-foreground">{t("pets.reason")}</Label>
                              <p className="text-sm">{rec.reason}</p>
                            </div>
                          </div>
                        </CardContent>
                      </Card>
                    ))}
                  </TabsContent>

                  <TabsContent value="tips" className="space-y-3 mt-4">
                    {aiRecommendations.tips.map((tip, index) => (
                      <Card key={index}>
                        <CardContent className="py-4">
                          <p className="text-sm">{tip}</p>
                        </CardContent>
                      </Card>
                    ))}
                  </TabsContent>
                </Tabs>
              </div>
            )}
          </DialogContent>
        </Dialog>
      </div>
    </div>
  );
};

export default MeusPets;
