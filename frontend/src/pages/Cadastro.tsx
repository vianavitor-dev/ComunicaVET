import { useState, useEffect } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Checkbox } from "@/components/ui/checkbox";
import { useNavigate, useLocation, Link } from "react-router-dom";
import { useToast } from "@/hooks/use-toast";
import { useTranslation } from "react-i18next";
import axios from "axios";
import pataImage from "@/assets/pata.png";

interface Country {
  id: number;
  name: string;
  abbreviation: string;
}

const Cadastro = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const location = useLocation();
  const { toast } = useToast();
  const [isLoading, setIsLoading] = useState(false);
  const [isClinic, setIsClinic] = useState(false);
  const [countries, setCountries] = useState<Country[]>([]);
  const [formData, setFormData] = useState({
    nomeCompleto: "",
    pais: "",
    estado: "",
    cidade: "",
    bairro: "",
    rua: "",
    numero: "",
    email: "",
    senha: "",
    confirmarSenha: "",
  });

  useEffect(() => {
    const emailFromState = (location.state as any)?.email;
    if (emailFromState) {
      setFormData(prev => ({ ...prev, email: emailFromState }));
    }
  }, [location]);

  useEffect(() => {
    const fetchCountries = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/v1/countries");
        
        if (!response.data.error && response.data.data) {
          setCountries(response.data.data);
        } else {
          toast({
            title: t("login.error"),
            description: response.data.message || t("login.errorMessage"),
            variant: "destructive",
          });
        }
      } catch (error: any) {
        toast({
          title: t("login.error"),
          description: error.response?.data?.message || t("login.errorMessage"),
          variant: "destructive",
        });
      }
    };

    fetchCountries();
  }, [toast, t]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (formData.senha !== formData.confirmarSenha) {
      toast({
        title: t("login.error"),
        description: t("signup.passwordMismatch"),
        variant: "destructive",
      });
      return;
    }

    setIsLoading(true);

    try {
      const requestData = {
        name: formData.nomeCompleto,
        email: formData.email,
        password: formData.senha,
        address: {
          id: null,
          street: formData.rua,
          city: formData.cidade,
          state: formData.estado,
          country: formData.pais,
          number: formData.numero,
          neighborhood: formData.bairro,
          location: null,
          complement: null
        },
        profileImagePath: null
      };

      const endpoint = isClinic 
        ? "http://localhost:8080/api/v1/clinics"
        : "http://localhost:8080/api/v1/pet-owners";

      const response = await axios.post(endpoint, requestData);

      toast({
        title: response.data.error ? t("login.error") : t("login.success"),
        description: response.data.message,
        variant: response.data.error ? "destructive" : "default",
      });

      if (!response.data.error) {
        localStorage.setItem("userId", response.data.data);
        localStorage.setItem("isClinic", isClinic.toString());
        navigate("/escolher-tags");
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

  const handleChange = (field: string, value: string) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  return (
    <div className="min-h-screen bg-background flex items-center justify-center px-4 py-12 relative">
      <div 
        className="absolute inset-0 opacity-30"
        style={{
          backgroundImage: `url(${pataImage})`,
          backgroundSize: '80%',
          backgroundPosition: 'center',
          backgroundRepeat: 'no-repeat'
        }}
      />
      <Card className="w-full max-w-md shadow-xl relative z-10">
        <CardHeader>
          <CardTitle className="text-3xl text-center font-bold">{t("signup.title")}</CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="nomeCompleto">{t("signup.fullName")}</Label>
              <Input
                id="nomeCompleto"
                type="text"
                value={formData.nomeCompleto}
                onChange={(e) => handleChange("nomeCompleto", e.target.value)}
                required
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="pais">{t("signup.country")}</Label>
              <Select value={formData.pais} onValueChange={(value) => handleChange("pais", value)} required>
                <SelectTrigger id="pais">
                  <SelectValue placeholder={t("signup.selectCountry")} />
                </SelectTrigger>
                <SelectContent>
                  {countries.map((country) => (
                    <SelectItem key={country.id} value={country.name}>
                      {country.name}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            <div className="space-y-2">
              <Label htmlFor="estado">{t("signup.state")}</Label>
              <Input
                id="estado"
                type="text"
                value={formData.estado}
                onChange={(e) => handleChange("estado", e.target.value)}
                required
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="cidade">{t("signup.city")}</Label>
              <Input
                id="cidade"
                type="text"
                value={formData.cidade}
                onChange={(e) => handleChange("cidade", e.target.value)}
                required
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="bairro">{t("signup.neighborhood")}</Label>
              <Input
                id="bairro"
                type="text"
                value={formData.bairro}
                onChange={(e) => handleChange("bairro", e.target.value)}
                required
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="rua">{t("signup.street")}</Label>
              <Input
                id="rua"
                type="text"
                value={formData.rua}
                onChange={(e) => handleChange("rua", e.target.value)}
                required
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="numero">{t("signup.number")}</Label>
              <Input
                id="numero"
                type="text"
                value={formData.numero}
                onChange={(e) => handleChange("numero", e.target.value)}
                required
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="email">{t("signup.email")}</Label>
              <Input
                id="email"
                type="email"
                value={formData.email}
                onChange={(e) => handleChange("email", e.target.value)}
                required
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="senha">{t("signup.password")}</Label>
              <Input
                id="senha"
                type="password"
                value={formData.senha}
                onChange={(e) => handleChange("senha", e.target.value)}
                required
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="confirmarSenha">{t("signup.confirmPassword")}</Label>
              <Input
                id="confirmarSenha"
                type="password"
                value={formData.confirmarSenha}
                onChange={(e) => handleChange("confirmarSenha", e.target.value)}
                required
              />
            </div>

            <div className="flex items-center space-x-2 py-2">
              <Checkbox 
                id="isClinic" 
                checked={isClinic}
                onCheckedChange={(checked) => setIsClinic(checked as boolean)}
              />
              <Label 
                htmlFor="isClinic" 
                className="text-sm font-normal cursor-pointer"
              >
                {t("signup.isClinic")}
              </Label>
            </div>

            <div className="text-center text-sm">
              {t("signup.haveAccount")}{" "}
              <Link to="/login" className="text-primary hover:underline font-semibold">
                {t("signup.loginNow")}
              </Link>
            </div>

            <div className="flex gap-4 pt-2">
              <Button
                type="button"
                variant="outline"
                className="flex-1"
                onClick={() => navigate("/")}
              >
                {t("signup.back")}
              </Button>
              <Button 
                type="submit" 
                className="flex-1 bg-[#00CED1] hover:bg-[#00CED1]/90 text-white"
                disabled={isLoading}
              >
                {isLoading ? t("signup.registering") : t("signup.register")}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  );
};

export default Cadastro;
