import Footer from "@/components/Footer";

const TermosUso = () => {
  return (
    <div className="min-h-screen bg-background">
      <div className="container mx-auto px-4 py-16 max-w-4xl">
        <h1 className="text-4xl font-bold mb-8">Termos de Uso</h1>
        <div className="prose prose-lg">
          <p className="text-muted-foreground">
            Esta é a página de Termos de Uso do ComunicaVET.
          </p>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default TermosUso;
