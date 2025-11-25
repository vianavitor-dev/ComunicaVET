import { Phone } from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";

interface ProfileCardProps {
  name?: string;
  avatar?: string;
  email?: string;
  phone?: string;
  instagram?: string;
  whatsapp?: string;
  website?: string;
}

const ProfileCard = ({
  name = "Prédio de Nycolas",
  avatar = "/placeholder.svg",
  email = "nycolas.guedesdenycolas.com",
  phone = "(88) 9 7070-7070",
  instagram = "@nycolasvetoninio",
  whatsapp = "+5588970707070",
  website = "https://nycolasvet.com"
}: ProfileCardProps) => {
  return (
    <Card className="sticky top-24">
      <CardContent className="p-6 space-y-6">
        <div className="flex flex-col items-center space-y-4">
          <Avatar className="w-32 h-32">
            <AvatarImage src={avatar} alt={name} />
            <AvatarFallback className="text-2xl">{name.charAt(0)}</AvatarFallback>
          </Avatar>
          <h1 className="text-2xl font-bold text-center">{name}</h1>
        </div>

        <div className="space-y-4">
          <div className="flex items-center gap-3">
            <Phone className="w-5 h-5 text-muted-foreground flex-shrink-0" />
            <div className="text-sm">{phone}</div>
          </div>
        </div>
      </CardContent>
    </Card>
  );
};

export default ProfileCard;
