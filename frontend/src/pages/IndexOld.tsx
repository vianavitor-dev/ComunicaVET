import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent } from "@/components/ui/card";
import { Monitor, ClipboardCheck, Sparkles } from "lucide-react";
import { useToast } from "@/hooks/use-toast";
import { Link, useNavigate } from "react-router-dom";
import heroPets from "@/assets/hero-pets.jpg";
import vetWithDog from "@/assets/vet-with-dog.jpg";
import pataImage from "@/assets/pata.png";
import Footer from "@/components/Footer";

const IndexOld = () => {
  const [email, setEmail] = useState("");
  const { toast } = useToast();
  const navigate = useNavigate();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (email.trim()) {
      navigate("/cadastro", { state: { email } });
    } else {
      toast({
        title: "Email obrigatório",
        description: "Por favor, preencha seu email antes de continuar.",
        variant: "destructive",
      });
    }
  };

  return (
    <div className="min-h-screen bg-background">
      {/* Hero Section */}
      <section className="container mx-auto px-4 py-12 md:py-20">
        <div className="grid md:grid-cols-2 gap-8 items-center">
          <div className="space-y-6">
            <h1 className="text-4xl md:text-5xl font-bold text-foreground leading-tight">
              Porque a Saúde do Seu Pet Não Pode Esperar
            </h1>
            <p className="text-lg text-muted-foreground">
              Está procurando uma clínica veterinária perto de você? Nós te ajudamos a encontrar a melhor opção em poucos cliques. Seu pet agradece!
            </p>
            <div className="flex flex-col sm:flex-row gap-4">
              <Button size="lg" className="font-semibold" asChild>
                <Link to="/cadastro">Começar Agora</Link>
              </Button>
              <Button size="lg" className="font-semibold" asChild>
                <Link to="/login">Entrar</Link>
              </Button>
            </div>
          </div>
          <div className="relative">
            <img 
              src={heroPets} 
              alt="Pets felizes e saudáveis" 
              className="w-full h-auto rounded-lg shadow-xl"
            />
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="bg-muted py-16 md:py-24">
        <div className="container mx-auto px-4">
          <h2 className="text-3xl md:text-4xl font-bold text-center mb-12 text-foreground">
            Sobre o ComunicaVET
          </h2>
          <div className="grid md:grid-cols-3 gap-8">
            {/* Acessibilidade */}
            <Card className="bg-primary text-primary-foreground border-0 hover:shadow-xl transition-shadow">
              <CardContent className="p-8 space-y-4">
                <div className="bg-primary-foreground/10 w-16 h-16 rounded-lg flex items-center justify-center mb-4">
                  <Monitor className="w-8 h-8" />
                </div>
                <h3 className="text-2xl font-bold">Acessibilidade</h3>
                <p className="text-primary-foreground/90">
                  Use nosso site em qualquer lugar e dispositivo. Encontre clínicas próximas de forma rápida, prática, esteja em casa ou na rua.
                </p>
              </CardContent>
            </Card>

            {/* Melhores Resultados */}
            <Card className="bg-primary text-primary-foreground border-0 hover:shadow-xl transition-shadow">
              <CardContent className="p-8 space-y-4">
                <div className="bg-primary-foreground/10 w-16 h-16 rounded-lg flex items-center justify-center mb-4">
                  <ClipboardCheck className="w-8 h-8" />
                </div>
                <h3 className="text-2xl font-bold">Melhores Resultados</h3>
                <p className="text-primary-foreground/90">
                  Receba recomendações personalizadas com base na sua localização e preferências. Resultados que fazem sentido pra você e seu pet.
                </p>
              </CardContent>
            </Card>

            {/* Simplicidade */}
            <Card className="bg-primary text-primary-foreground border-0 hover:shadow-xl transition-shadow">
              <CardContent className="p-8 space-y-4">
                <div className="bg-primary-foreground/10 w-16 h-16 rounded-lg flex items-center justify-center mb-4">
                  <Sparkles className="w-8 h-8" />
                </div>
                <h3 className="text-2xl font-bold">Simplicidade</h3>
                <p className="text-primary-foreground/90">
                  Design intuitivo, navegação fácil. Encontre o que precisa em poucos cliques, sem complicação.
                </p>
              </CardContent>
            </Card>
          </div>
        </div>
      </section>

      {/* Join Us Section */}
      <section className="container mx-auto px-4 py-16 md:py-24">
        <div className="grid md:grid-cols-2 gap-12 items-center">
          <div className="space-y-6">
            <h2 className="text-3xl md:text-4xl font-bold text-foreground">
              Junte-se a Nós
            </h2>
            <p className="text-lg text-muted-foreground">
              Cadastre-se para receber dicas de saúde pet, novidades e encontrar as melhores clínicas veterinárias.
            </p>
            <form onSubmit={handleSubmit} className="space-y-4">
              <Input
                type="email"
                placeholder="Seu melhor e-mail"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                className="text-lg py-6"
              />
              <Button type="submit" size="lg" className="w-full md:w-auto font-semibold">
                Cadastre-se
              </Button>
            </form>
          </div>
          <div className="relative">
            <img 
              src={vetWithDog} 
              alt="Veterinário com cachorro" 
              className="w-full h-auto rounded-lg shadow-xl"
            />
          </div>
        </div>
      </section>

      {/* Footer */}
      <Footer />
    </div>
  );
};

export default IndexOld;
