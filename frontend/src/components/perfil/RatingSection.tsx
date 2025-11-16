import { Star } from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { useState } from "react";
import axios from "axios";
import { useToast } from "@/hooks/use-toast";

interface RatingSectionProps {
  stars?: number;
  clinicId?: string;
  userId?: string;
  onRatingSubmit?: () => void;
}

const RatingSection = ({ stars = 3.0, clinicId, userId, onRatingSubmit }: RatingSectionProps) => {
  const maxStars = 5;
  const filledStars = Math.floor(stars);
  const [isOpen, setIsOpen] = useState(false);
  const [hoveredStar, setHoveredStar] = useState<number | null>(null);
  const [selectedStar, setSelectedStar] = useState<number | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const { toast } = useToast();
  const isClinic = localStorage.getItem("isClinic") === "true";

  const handleStarClick = async (starNumber: number) => {
    if (!userId || !clinicId) {
      toast({
        title: "Erro",
        description: "Você precisa estar logado para avaliar.",
        variant: "destructive",
      });
      return;
    }

    setSelectedStar(starNumber);
    setIsSubmitting(true);

    try {
      await axios.post(
        `${import.meta.env.VITE_API_URL}/api/v1/clinics/${clinicId}/rate?userId=${userId}&stars=${starNumber}`
      );

      toast({
        title: "Sucesso",
        description: "Avaliação enviada com sucesso!",
      });

      setIsOpen(false);
      setSelectedStar(null);
      onRatingSubmit?.();
    } catch (error: any) {
      toast({
        title: "Erro",
        description: error.response?.data?.message || "Erro ao enviar avaliação.",
        variant: "destructive",
      });
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Card>
      <CardContent className="p-6">
        <h2 className="text-xl font-bold mb-4">Avaliação</h2>
        <div className="flex items-center gap-4">
          <span className="text-3xl font-bold">{stars.toFixed(1)}</span>
          {isClinic ? (
            <div className="flex gap-1">
              {Array.from({ length: maxStars }).map((_, index) => (
                <Star 
                  key={index}
                  className={`w-6 h-6 transition-colors ${
                    index < filledStars 
                      ? 'fill-primary text-primary' 
                      : 'text-muted-foreground'
                  }`}
                />
              ))}
            </div>
          ) : (
            <Dialog open={isOpen} onOpenChange={setIsOpen}>
              <DialogTrigger asChild>
                <div className="flex gap-1 cursor-pointer">
                  {Array.from({ length: maxStars }).map((_, index) => (
                    <Star 
                      key={index}
                      className={`w-6 h-6 transition-colors ${
                        index < filledStars 
                          ? 'fill-primary text-primary' 
                          : 'text-muted-foreground'
                      }`}
                    />
                  ))}
                </div>
              </DialogTrigger>
            <DialogContent className="sm:max-w-md">
              <DialogHeader>
                <DialogTitle>Avaliar Clínica</DialogTitle>
              </DialogHeader>
              <div className="flex flex-col items-center gap-6 py-4">
                <p className="text-sm text-muted-foreground">
                  Clique nas estrelas para avaliar
                </p>
                <div 
                  className="flex gap-2"
                  onMouseLeave={() => setHoveredStar(null)}
                >
                  {Array.from({ length: maxStars }).map((_, index) => {
                    const starNumber = index + 1;
                    const isActive = hoveredStar !== null 
                      ? starNumber <= hoveredStar 
                      : selectedStar !== null && starNumber <= selectedStar;

                    return (
                      <button
                        key={index}
                        onMouseEnter={() => setHoveredStar(starNumber)}
                        onClick={() => handleStarClick(starNumber)}
                        disabled={isSubmitting}
                        className="transition-transform hover:scale-110 disabled:opacity-50"
                      >
                        <Star 
                          className={`w-10 h-10 transition-all ${
                            isActive 
                              ? 'fill-primary text-primary' 
                              : 'text-muted-foreground'
                          }`}
                        />
                      </button>
                    );
                  })}
                </div>
              </div>
            </DialogContent>
          </Dialog>
          )}
        </div>
      </CardContent>
    </Card>
  );
};

export default RatingSection;
