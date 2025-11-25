import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import { useToast } from "@/hooks/use-toast";
import PetOwnerNavbar from "@/components/PetOwnerNavbar";
import ClinicNavbar from "@/components/ClinicNavbar";
import CoverImage from "@/components/perfil/CoverImage";
import InfoSection from "@/components/perfil/InfoSection";
import ProfileCard from "@/components/perfil/ProfileCard";
import Gallery from "@/components/perfil/Gallery";
import TagsSection from "@/components/perfil/TagsSection";
import RatingSection from "@/components/perfil/RatingSection";
import CommentsSection from "@/components/perfil/CommentsSection";
import { useTranslation } from "react-i18next";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { PlusCircle } from "lucide-react";

interface Comment {
  id: number;
  text: string;
  writerName: string;
  writerEmail: string;
  writerId: number;
  createdAt: string;
  reportCount: number;
  likesCount: number;
}

interface Focus {
  name: string;
  description: string | null;
}

interface Address {
  city: string;
  state: string;
  neighborhood: string;
  street: string;
  number: number;
}

interface ClinicData {
  name: string;
  email: string;
  phone: string;
  description: string;
  stars: number;
  address: Address;
  focuses: Focus[];
  wasFavorited: boolean;
}

const ClinicaPerfil = () => {
  const { t } = useTranslation();
  const { id } = useParams<{ id: string }>();
  const { toast } = useToast();
  const [clinicData, setClinicData] = useState<ClinicData | null>(null);
  const [comments, setComments] = useState<Comment[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isFavorite, setIsFavorite] = useState(false);
  const [pets, setPets] = useState<any[]>([]);
  const [selectedPetId, setSelectedPetId] = useState<string>("");
  const [isAddHistoryDialogOpen, setIsAddHistoryDialogOpen] = useState(false);

  // Get user ID and clinic status from localStorage (set during login)
  const currentUserId = localStorage.getItem("userId");
  const isClinic = localStorage.getItem("isClinic") === "true";

  useEffect(() => {
    const fetchClinicData = async () => {
      if (!id) return;

      try {
        setIsLoading(true);
        const [clinicResponse, commentsResponse] = await Promise.all([
          axios.get(`${import.meta.env.VITE_API_URL}/api/v1/clinics/${id}`),
          axios.get(`${import.meta.env.VITE_API_URL}/api/v1/comments/clinic/${id}`)
        ]);

        const { error, message, data } = clinicResponse.data;

        if (error) {
          toast({
            title: t("login.error"),
            description: message,
            variant: "destructive",
          });
        } else {
          setClinicData(data);
          setIsFavorite(data.wasFavorited);
        }

        // Handle comments response
        if (commentsResponse.data && !commentsResponse.data.error) {
          setComments(commentsResponse.data.data || []);
        }
      } catch (error: any) {
        toast({
          title: t("login.error"),
          description: error.response?.data?.message || t("clinicProfile.errorLoading"),
          variant: "destructive",
        });
      } finally {
        setIsLoading(false);
      }
    };

    fetchClinicData();
    
    // Fetch pets if user is a pet owner
    if (currentUserId && !isClinic) {
      fetchPets();
    }
  }, [id, toast]);

  const fetchPets = async () => {
    try {
      const response = await axios.get(
        `${import.meta.env.VITE_API_URL}/api/v1/pets/owner/${currentUserId}`
      );
      
      if (!response.data.error) {
        setPets(response.data.data || []);
      }
    } catch (error) {
      console.error("Error fetching pets:", error);
    }
  };

  const handleToggleFavorite = async () => {
    if (!id || !currentUserId) return;

    try {
      if (isFavorite) {
        // Desfavoritar - usa DELETE
        await axios.delete(`${import.meta.env.VITE_API_URL}/api/v1/favorite-clinics/delete/${id}`);
      } else {
        // Favoritar - usa POST
        await axios.post(`${import.meta.env.VITE_API_URL}/api/v1/favorite-clinics`, {
          id: null,
          petOwnerId: parseInt(currentUserId),
          clinicId: parseInt(id)
        });
      }

      setIsFavorite(!isFavorite);
      toast({
        title: t("login.success"),
        description: isFavorite ? t("clinicPublicProfile.removedFromFavorites") : t("clinicPublicProfile.addedToFavorites"),
      });
    } catch (error: any) {
      toast({
        title: t("login.error"),
        description: error.response?.data?.message || t("clinicPublicProfile.errorUpdatingFavorites"),
        variant: "destructive",
      });
    }
  };

  const handleSubmitComment = async (text: string) => {
    if (!text.trim()) {
      toast({
        title: t("login.error"),
        description: t("clinicPublicProfile.commentEmpty"),
        variant: "destructive",
      });
      return;
    }

    if (!currentUserId) {
      toast({
        title: t("login.error"),
        description: t("clinicPublicProfile.needsLogin"),
        variant: "destructive",
      });
      return;
    }

    try {
      await axios.post(`${import.meta.env.VITE_API_URL}/api/v1/comments`, {
        text: text,
        writerId: parseInt(currentUserId),
        clinicId: parseInt(id)
      });

      toast({
        title: t("login.success"),
        description: t("clinicPublicProfile.commentSuccess"),
      });

      // Refresh comments
      if (id) {
        const commentsResponse = await axios.get(`${import.meta.env.VITE_API_URL}/api/v1/comments/clinic/${id}`);
        if (commentsResponse.data && !commentsResponse.data.error) {
          setComments(commentsResponse.data.data || []);
        }
      }
    } catch (error: any) {
      toast({
        title: t("login.error"),
        description: error.response?.data?.message || t("clinicPublicProfile.commentError"),
        variant: "destructive",
      });
    }
  };

  const handleAddToHistory = async () => {
    if (!selectedPetId) {
      toast({
        title: t("login.error"),
        description: t("pets.selectPet"),
        variant: "destructive",
      });
      return;
    }

    try {
      await axios.post(`${import.meta.env.VITE_API_URL}/api/v1/pet-clinic-history`, {
        petId: parseInt(selectedPetId),
        clinicId: parseInt(id!),
      });

      toast({
        title: t("login.success"),
        description: t("pets.addedToHistory"),
      });

      setIsAddHistoryDialogOpen(false);
      setSelectedPetId("");
    } catch (error: any) {
      toast({
        title: t("login.error"),
        description: error.response?.data?.message || t("pets.errorAddingHistory"),
        variant: "destructive",
      });
    }
  };

  if (isLoading) {
    return (
      <div className="min-h-screen bg-background flex items-center justify-center">
        <p className="text-muted-foreground">{t("clinicPublicProfile.loading")}</p>
      </div>
    );
  }

  if (!clinicData) {
    return (
      <div className="min-h-screen bg-background flex items-center justify-center">
        <p className="text-muted-foreground">{t("clinicPublicProfile.notFound")}</p>
      </div>
    );
  }

  const profileImageUrl = `${import.meta.env.VITE_API_URL}/api/v1/users/profile-image?id=${id}`;
  const backgroundImageUrl = `${import.meta.env.VITE_API_URL}/api/v1/clinics/${id}/background-image`;

  return (
    <div className="min-h-screen bg-background">
      {isClinic ? <ClinicNavbar /> : <PetOwnerNavbar />}
      <CoverImage imageUrl={backgroundImageUrl} />
      
      <div className="container mx-auto px-4 py-8">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {/* Coluna Esquerda - Conteúdo Principal */}
          <div className="md:col-span-2 space-y-6">
            <div className="space-y-4">
              {!isClinic && pets.length > 0 && (
                <Dialog open={isAddHistoryDialogOpen} onOpenChange={setIsAddHistoryDialogOpen}>
                  <DialogTrigger asChild>
                    <Button className="w-full gap-2">
                      <PlusCircle className="h-4 w-4" />
                      {t("pets.addToHistory")}
                    </Button>
                  </DialogTrigger>
                  <DialogContent>
                    <DialogHeader>
                      <DialogTitle>{t("pets.selectPetTitle")}</DialogTitle>
                      <DialogDescription>{t("pets.selectPetDescription")}</DialogDescription>
                    </DialogHeader>
                    <div className="space-y-4 py-4">
                      <Select value={selectedPetId} onValueChange={setSelectedPetId}>
                        <SelectTrigger>
                          <SelectValue placeholder={t("pets.selectPetPlaceholder")} />
                        </SelectTrigger>
                        <SelectContent>
                          {pets.map((pet) => (
                            <SelectItem key={pet.id} value={pet.id.toString()}>
                              {pet.name}
                            </SelectItem>
                          ))}
                        </SelectContent>
                      </Select>
                    </div>
                    <div className="flex justify-end gap-2">
                      <Button variant="outline" onClick={() => setIsAddHistoryDialogOpen(false)}>
                        {t("profile.cancel")}
                      </Button>
                      <Button onClick={handleAddToHistory}>{t("pets.add")}</Button>
                    </div>
                  </DialogContent>
                </Dialog>
              )}
              
              <InfoSection 
                city={clinicData.address.city}
                street={`${clinicData.address.street}, ${clinicData.address.number} - ${clinicData.address.neighborhood}`}
                description={clinicData.description}
                isFavorite={isFavorite}
                onToggleFavorite={handleToggleFavorite}
              />
            </div>
            <Gallery images={[]} />
            <TagsSection categories={clinicData.focuses.map(f => f.name)} />
            <RatingSection 
              stars={clinicData.stars} 
              clinicId={id} 
              userId={currentUserId || undefined}
              onRatingSubmit={() => {
                // Refresh clinic data after rating
                if (id) {
                  axios.get(`${import.meta.env.VITE_API_URL}/api/v1/clinics/${id}`).then(response => {
                    if (!response.data.error) {
                      setClinicData(response.data.data);
                    }
                  });
                }
              }}
            />
            <CommentsSection comments={comments} onSubmitComment={handleSubmitComment} userId={currentUserId || undefined} />
          </div>

          {/* Coluna Direita - Card de Perfil */}
          <div className="md:col-span-1">
            <ProfileCard 
              name={clinicData.name}
              avatar={profileImageUrl}
              email={clinicData.email}
              phone={clinicData.phone}
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default ClinicaPerfil;
