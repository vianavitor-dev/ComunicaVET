import { Button } from "@/components/ui/button";
import { useNavigate } from "react-router-dom";

const ProfileHeader = () => {
  const navigate = useNavigate();

  return (
    <header className="bg-primary py-4">
      <div className="container mx-auto px-4 flex items-center justify-between">
        <div className="text-2xl font-bold text-primary-foreground">COMUNICAVET.LOGO</div>
        <Button 
          variant="secondary" 
          onClick={() => navigate("/perfil")}
        >
          Perfil
        </Button>
      </div>
    </header>
  );
};

export default ProfileHeader;
