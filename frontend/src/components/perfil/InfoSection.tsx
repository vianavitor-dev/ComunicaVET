import { MapPin, Heart } from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";
import { useState } from "react";

interface InfoSectionProps {
  city?: string;
  street?: string;
  description?: string;
  isFavorite?: boolean;
  onToggleFavorite?: () => void;
}

const InfoSection = ({ 
  city = "Sao Paulo – SP", 
  street = "Rua do Nycoli – Nº 123",
  description = "Eu sou o Nycolas, também conhecido como Nycoli aqui nas minhas quebradas apartamentos burgueses. Sou um veterinário apaixonado por animais de todos os tipos. Minha clínica oferece atendimento especializado para cães, gatos, aves, peixes e outros animais de estimação. Tenho mais de 10 anos de experiência e trabalho com muito amor e dedicação para garantir a saúde e bem-estar dos seus pets.",
  isFavorite = false,
  onToggleFavorite
}: InfoSectionProps) => {
  const isClinic = localStorage.getItem("isClinic") === "true";

  return (
    <Card className="relative">
      {!isClinic && (
        <button 
          onClick={onToggleFavorite}
          className="absolute top-4 right-4 text-2xl hover:scale-110 transition-transform"
        >
          <Heart 
            className={`w-6 h-6 ${isFavorite ? 'fill-red-500 text-red-500' : 'text-muted-foreground'}`}
          />
        </button>
      )}
      <CardContent className="p-6 space-y-6">
        <div className="space-y-2">
          <h2 className="text-xl font-bold flex items-center gap-2">
            <MapPin className="w-5 h-5" />
            Endereço
          </h2>
          <div className="text-muted-foreground space-y-1 pl-7">
            <p>{city}</p>
            <p>{street}</p>
          </div>
        </div>

        <div className="space-y-2">
          <h2 className="text-xl font-bold">Descrição</h2>
          <p className="text-muted-foreground leading-relaxed">
            {description && description.trim() !== "" 
              ? description 
              : "Esta clínica não possui uma descrição."}
          </p>
        </div>
      </CardContent>
    </Card>
  );
};

export default InfoSection;
