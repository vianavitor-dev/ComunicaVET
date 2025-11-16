import Footer from "@/components/Footer";

const AvisoLegal = () => {
  return (
    <div className="min-h-screen bg-background">
      <div className="container mx-auto px-4 py-16 max-w-4xl">
        <h1 className="text-4xl font-bold mb-8">Aviso Legal</h1>
        <div className="prose prose-lg">
          <p className="text-muted-foreground">
            Esta é a página de Aviso Legal do ComunicaVET.
          </p>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default AvisoLegal;
