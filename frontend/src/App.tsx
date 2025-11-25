import { Toaster } from "@/components/ui/toaster";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { TooltipProvider } from "@/components/ui/tooltip";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Index from "./pages/Index";
import IndexOld from "./pages/IndexOld";
import Clinicas from "./pages/Clinicas";
import Recomendacoes from "./pages/Recomendacoes";
import Login from "./pages/Login";
import Cadastro from "./pages/Cadastro";
import EscolherTags from "./pages/EscolherTags";
import RecuperarSenha from "./pages/RecuperarSenha";
import VerificarCodigo from "./pages/VerificarCodigo";
import NovaSenha from "./pages/NovaSenha";
import PoliticaPrivacidade from "./pages/PoliticaPrivacidade";
import TermosUso from "./pages/TermosUso";
import AvisoLegal from "./pages/AvisoLegal";
import PoliticaCookies from "./pages/PoliticaCookies";
import Perfil from "./pages/Perfil";
import PerfilClinica from "./pages/PerfilClinica";
import ClinicaPerfil from "./pages/ClinicaPerfil";
import MeusPets from "./pages/MeusPets";
import NotFound from "./pages/NotFound";

const queryClient = new QueryClient();

const App = () => (
  <QueryClientProvider client={queryClient}>
    <TooltipProvider>
      <Toaster />
      <Sonner />
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Index />} />
          <Route path="/index" element={<IndexOld />} />
          <Route path="/clinicas" element={<Clinicas />} />
          <Route path="/recomendacoes" element={<Recomendacoes />} />
          <Route path="/login" element={<Login />} />
          <Route path="/cadastro" element={<Cadastro />} />
          <Route path="/escolher-tags" element={<EscolherTags />} />
          <Route path="/recuperar-senha" element={<RecuperarSenha />} />
          <Route path="/verificar-codigo" element={<VerificarCodigo />} />
          <Route path="/nova-senha" element={<NovaSenha />} />
          <Route path="/politica-privacidade" element={<PoliticaPrivacidade />} />
          <Route path="/termos-uso" element={<TermosUso />} />
          <Route path="/aviso-legal" element={<AvisoLegal />} />
          <Route path="/politica-cookies" element={<PoliticaCookies />} />
          <Route path="/perfil" element={<Perfil />} />
          <Route path="/perfil-clinica" element={<PerfilClinica />} />
          <Route path="/clinica-perfil/:id?" element={<ClinicaPerfil />} />
          <Route path="/meus-pets" element={<MeusPets />} />
          {/* ADD ALL CUSTOM ROUTES ABOVE THE CATCH-ALL "*" ROUTE */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </TooltipProvider>
  </QueryClientProvider>
);

export default App;
