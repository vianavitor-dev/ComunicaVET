import os
import logging
import time
import json
import hashlib
from datetime import datetime, timedelta
from typing import Dict, List, Optional, Any

# Configurar logging compatível com Windows
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    handlers=[logging.StreamHandler()]  # Apenas StreamHandler
)
logger = logging.getLogger(__name__)

# Importação segura do Gemini
try:
    import google.generativeai as genai
    GEMINI_AVAILABLE = True
    logger.info("Biblioteca Gemini disponivel")
except ImportError as e:
    logger.warning(f"Google Generative AI nao disponivel: {e}")
    GEMINI_AVAILABLE = False

class GeminiAIService:
    def __init__(self):
        self.api_key = os.environ.get('GEMINI_API_KEY', '').strip()
        self.is_available = GEMINI_AVAILABLE and bool(self.api_key)
        self.cache = {}
        self.cache_duration = timedelta(hours=24)
        self.model = None
        
        if self.is_available:
            self._initialize_gemini()
        else:
            logger.warning("GEMINI_API_KEY nao encontrada - usando apenas regras locais")
    
    def _initialize_gemini(self):
        """Inicialização segura do Gemini com fallback de modelos"""
        try:
            genai.configure(api_key=self.api_key)
            
            # Lista de modelos para tentar (em ordem de preferência)
            models_to_try = [
                'gemini-2.5-flash',     # Alternativa
                'gemini-2.0-flash',     # Pode não estar disponível para todos
                'gemini-pro',           # Modelo mais comum e estável
                'models/gemini-pro',    # Formato completo
            ]
            
            for model_name in models_to_try:
                try:
                    self.model = genai.GenerativeModel(model_name)
                    # Teste rápido
                    response = self.model.generate_content("Test")
                    logger.info(f"Modelo configurado: {model_name}")
                    break
                except Exception as e:
                    logger.warning(f"Modelo {model_name} falhou: {e}")
                    continue
            
            if self.model is None:
                raise Exception("Nenhum modelo do Gemini funcionou")
                
        except Exception as e:
            logger.error(f"Falha na configuracao do Gemini: {e}")
            self.is_available = False
            self.model = None
    
    def get_cached_recommendations(self, cache_key):
        """Busca recomendações em cache"""
        if cache_key in self.cache:
            cached_data, timestamp = self.cache[cache_key]
            if datetime.now() - timestamp < self.cache_duration:
                logger.debug(f"Usando cache para: {cache_key}")
                return cached_data
        return None
    
    def set_cached_recommendations(self, cache_key, data):
        """Armazena recomendações em cache"""
        self.cache[cache_key] = (data, datetime.now())
    
    def get_recommendations(self, pet_data):
        """
        Gera recomendações usando Gemini AI com fallback para regras locais
        """
        try:
            cache_key = self._create_cache_key(pet_data)
            
            # Tentar cache primeiro
            cached = self.get_cached_recommendations(cache_key)
            if cached:
                cached['source'] = 'gemini_cached'
                return cached
            
            # Se Gemini disponível, tentar usar
            if self.is_available and self.model:
                try:
                    gemini_result = self._call_gemini_api(pet_data)
                    if gemini_result:
                        self.set_cached_recommendations(cache_key, gemini_result)
                        gemini_result['source'] = 'gemini_live'
                        logger.info("Recomendacoes Gemini geradas com sucesso")
                        return gemini_result
                    else:
                        logger.warning("Gemini retornou resultado vazio")
                except Exception as e:
                    logger.warning(f"Gemini API falhou: {e}")
            
            # Fallback para regras locais
            logger.info("Usando regras locais (fallback)")
            return self._generate_fallback_recommendations(pet_data)
            
        except Exception as e:
            logger.error(f"Erro em get_recommendations: {e}")
            return self._generate_fallback_recommendations(pet_data)
    
    def _create_cache_key(self, pet_data):
        """Cria chave de cache baseada nos dados do pet"""
        key_parts = [
            pet_data.get('pet_type', ''),
            pet_data.get('pet_size', ''),
            str(pet_data.get('pet_age_months', 0)),
            pet_data.get('pet_breed', '')[:20]
        ]
        key_str = json.dumps(key_parts, sort_keys=True)
        return hashlib.md5(key_str.encode()).hexdigest()
    
    def _call_gemini_api(self, pet_data):
        """Chama a API do Gemini para recomendações"""
        try:
            prompt = self._create_gemini_prompt(pet_data)
            
            # Verifica se o modelo está inicializado antes de chamar a API
            if self.model is None:
                logger.warning("Modelo Gemini nao inicializado ao chamar API")
                return None
            
            # Chamada segura para API
            response = self.model.generate_content(prompt)
            
            # Algumas versões do SDK retornam objetos diferentes; normalize para texto
            response_text = None
            if hasattr(response, 'text') and isinstance(response.text, str):
                response_text = response.text
            else:
                try:
                    response_text = json.dumps(response) if not isinstance(response, str) else response
                except Exception:
                    response_text = str(response)
            
            return self._parse_gemini_response(response_text, pet_data)
            
        except Exception as e:
            logger.error(f"Erro na chamada Gemini API: {e}")
            return None
    
    def _create_gemini_prompt(self, pet_data):
        """Cria prompt otimizado para o Gemini"""
        pet_type = pet_data.get('pet_type', 'DOG')
        pet_size = pet_data.get('pet_size', 'MEDIUM')
        age_months = pet_data.get('pet_age_months', 12)
        breed = pet_data.get('pet_breed', '')
        secondary_breed = pet_data.get('pet_secondary_breed', '')
        
        age_years = age_months / 12.0
        
        return f"""
Você é um veterinário especialista. Forneça recomendações PRÁTICAS baseadas no perfil abaixo.

PERFIL DO PET:
- Nome: {pet_data.get('pet_name', 'Nao informado')}
- Tipo: {pet_type}
- Tamanho: {pet_size}
- Idade: {age_months} meses (~{age_years:.1f} anos)
- Raça primária: {breed if breed else 'Nao especificada'}
- Raça secundária: {secondary_breed if secondary_breed else 'Nao especificada'}

INSTRUÇÕES:
- Seja PRÁTICO e ESPECÍFICO
- UTILIZE UM LINGUAGEM MAIS SIMPLES PARA QUE QUALQUER UM POSSA COMPREENDER
- Foque em CUIDADOS REAIS e SEGUROS
- Considere IDADE, TAMANHO e TIPO
- Máximo 4 recomendações e 5 dicas

FORMATO JSON:
{{
  "recommendations": [
    {{
      "type": "VACCINATION|NUTRITION|EXERCISE|HEALTH_CHECK|SPECIALIST",
      "priority": "HIGH|MEDIUM|LOW",
      "message": "Recomendação clara e prática",
      "reason": "Justificativa baseada no perfil"
    }}
  ],
  "tips": [
    "Dica prática 1",
    "Dica prática 2",
    "Dica prática 3", 
    "Dica prática 4",
    "Dica prática 5"
  ],
  "analysis": {{
    "age_category": "FILHOTE|ADULTO|IDOSO",
    "special_care_needed": true/false
  }}
}}

RESPONDA APENAS EM JSON:
"""
    
    def _parse_gemini_response(self, response_text, pet_data):
        """Parseia a resposta do Gemini extraindo JSON"""
        try:
            # Limpar e extrair JSON
            cleaned_text = response_text.strip()
            
            # Encontrar JSON
            start_idx = cleaned_text.find('{')
            end_idx = cleaned_text.rfind('}') + 1
            
            if start_idx == -1 or end_idx == 0:
                raise ValueError("JSON nao encontrado")
            
            json_str = cleaned_text[start_idx:end_idx]
            result = json.loads(json_str)
            
            # Validação básica
            if not all(key in result for key in ['recommendations', 'tips', 'analysis']):
                raise ValueError("Estrutura JSON invalida")
            
            logger.info("Resposta Gemini parseada com sucesso")
            return result
            
        except (json.JSONDecodeError, ValueError) as e:
            logger.warning(f"Falha ao parsear resposta Gemini: {e}")
            return None
    
    def _generate_fallback_recommendations(self, pet_data):
        """Gera recomendações usando apenas regras locais"""
        logger.info("Usando regras locais (fallback)")
        
        return {
            "recommendations": self._create_basic_recommendations(pet_data),
            "tips": self._create_basic_tips(pet_data),
            "analysis": self._create_basic_analysis(pet_data),
            "source": "local_rules"
        }
    
    def _create_basic_recommendations(self, pet_data):
        """Cria recomendações básicas baseadas em regras"""
        pet_type = pet_data.get('pet_type', 'DOG')
        pet_size = pet_data.get('pet_size', 'MEDIUM')
        age_months = pet_data.get('pet_age_months', 12)
        
        recommendations = []
        
        if age_months < 12:
            recommendations.append({
                "type": "VACCINATION",
                "priority": "HIGH",
                "message": "Mantenha o calendario de vacinacao em dia",
                "reason": "Filhotes precisam de protecao imunologica completa"
            })
        
        if pet_type == 'OTHER':
            recommendations.append({
                "type": "SPECIALIST",
                "priority": "HIGH",
                "message": "Procure veterinario especializado em animais exoticos",
                "reason": "Animais exoticos requerem cuidados especificos"
            })
        
        if age_months > 84:
            recommendations.append({
                "type": "GERIATRIC_CARE",
                "priority": "MEDIUM",
                "message": "Check-ups semestrais recomendados",
                "reason": "Pets idosos precisam de monitoramento constante"
            })
        
        return recommendations
    
    def _create_basic_tips(self, pet_data):
        """Cria dicas básicas baseadas em regras"""
        return [
            "Mantenha agua fresca sempre disponivel",
            "Visite o veterinario regularmente para check-ups",
            "Mantenha a vacinacao e vermifugacao em dia",
            "Forneca alimentacao de qualidade apropriada para a idade",
            "Pratique exercicios adequados para o tamanho e idade do pet"
        ]
    
    def _create_basic_analysis(self, pet_data):
        """Cria análise básica baseada em regras"""
        age_months = pet_data.get('pet_age_months', 12)
        pet_type = pet_data.get('pet_type', 'DOG')
        pet_size = pet_data.get('pet_size', 'MEDIUM')
        
        age_category = "FILHOTE" if age_months < 12 else "ADULTO" if age_months < 84 else "IDOSO"
        
        risks = []
        if age_months < 6:
            risks.append("Sistema imunologico em desenvolvimento")
        if age_months > 84:
            risks.append("Maior risco de doencas cronicas")
        if pet_size == 'BIG':
            risks.append("Problemas articulares")
        
        return {
            "age_category": age_category,
            "special_care_needed": age_months < 6 or age_months > 84 or pet_type == 'OTHER',
            "risk_factors": risks
        }